package com.soundcloud.followermaze.event.handler

import java.net.Socket
import java.util.concurrent.Executors
import scala.util.{ Success, Try }
import org.slf4j.LoggerFactory.getLogger
import com.soundcloud.followermaze.event.Event
import com.soundcloud.followermaze.transport.RichSocket._
import com.soundcloud.followermaze.transport.route.Router

class EventSourceHandler(port: Int, router: Router, poolSize: Int = 10) extends Runnable {

  private val LOGGER = getLogger(this.getClass)

  private val pool = Executors.newFixedThreadPool(poolSize)

  override def run() {
    val server = createServerFor(port)
    while (true) {
      pool.execute(Handler(server.accept))
    }
  }

  private case class Handler(client: Socket) extends Runnable {
    
    override def run {
      try {
        val buffer = client.createBuffer
        var payload = buffer.readLine

        while (payload != null) {
          Try(Event(payload)) match {
            case Success(event) => router.tryDeliver(event)
            case _              => LOGGER.error("Droped malformated paylod {}", payload)
          }
          payload = buffer.readLine
        }
      } finally {
        client.close
      }
    }
  }
}