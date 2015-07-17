package com.soundcloud.followermaze.event.handler

import java.net.Socket
import java.util.concurrent.Executors
import com.soundcloud.followermaze.transport.RichSocket._
import com.soundcloud.followermaze.transport.route.Router
import com.soundcloud.followermaze.transport.CloseSocketHook.closeOnExit

class UserClientHandler(port: Int, router: Router, poolSize: Int = 10) extends Runnable {
  
  private val pool = Executors.newFixedThreadPool(poolSize)
  
  override def run() {
    val server = createServerFor(port)
    while (true) {
      val userClient = server.accept()
      pool.execute(new Runnable {
        override def run {
          router.openSession(closeOnExit(userClient))
        }
      })
    }
  }
}