package com.soundcloud.followermaze.event.handler

import com.soundcloud.followermaze.transport.RichSocket._
import com.soundcloud.followermaze.transport.route.Router
import com.soundcloud.followermaze.transport.CloseSocketHook.closeOnExit

class UserClientHandler(port: Int, router: Router) extends Runnable {
  
  override def run() {
    val server = createServerFor(port)
    while (true) {
      val client = server.accept()
      router.openSession(closeOnExit(client))
    }
  }
}