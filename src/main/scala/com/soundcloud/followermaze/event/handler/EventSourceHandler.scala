package com.soundcloud.followermaze.event.handler

import java.net.Socket
import scala.util.{ Success, Try }
import org.slf4j.LoggerFactory.getLogger
import com.soundcloud.followermaze.event.Event
import com.soundcloud.followermaze.transport.RichSocket._
import com.soundcloud.followermaze.transport.route.Router

class EventSourceHandler(port: Int, router: Router) extends Runnable {

  private val LOGGER = getLogger(this.getClass)

  override def run() {
    val server = createServerFor(port)
    while (true) {
      handle(server.accept)
    }
  }

  private def handle(socket: Socket) {
    try {
      val buffer = socket.createBuffer
      var payload = buffer.readLine

      while (payload != null) {
        Try(Event(payload)) match {
          case Success(event) => router.tryDeliver(event)
          case _              => LOGGER.error("Droped malformated paylod {}", payload)
        }
        payload = buffer.readLine
      }
    } finally {
      socket.close
    }
  }
}