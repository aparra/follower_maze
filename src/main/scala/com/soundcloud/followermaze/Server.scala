package com.soundcloud.followermaze

import scala.util.{ Try, Success }
import org.slf4j.LoggerFactory.getLogger
import com.soundcloud.followermaze.event.handler.{ UserClientHandler, EventSourceHandler }
import com.soundcloud.followermaze.transport.route.{ Route, Router }
import com.soundcloud.followermaze.user.UserRepository

object Server {
  
  private val LOGGER = getLogger(this.getClass)
  
  val USER_CLIENT_PORT = 9099
  val EVENT_SOURCE_PORT = 9090
  
  def start(userClientPort: Int, eventSourcePort: Int) {
    val userRepository = new UserRepository()
    val router = new Router(userRepository, Route.buildAvailableRoutes(userRepository))

    LOGGER.info("User client port set to: {}", userClientPort)
    new Thread(new UserClientHandler(9099, router)).start()
    
    LOGGER.info("Event source port set to: {}", eventSourcePort)
    new Thread(new EventSourceHandler(9090, router)).start() 
  }
  
  def main(args: Array[String]): Unit = {
    val userClientPort = Try(args(0).toInt) match {
      case Success(port) => port
      case _ => USER_CLIENT_PORT 
    }
    
    val eventSourcePort = Try(args(1).toInt) match {
      case Success(port) => port
      case _ => EVENT_SOURCE_PORT
    }
    
    start(userClientPort, eventSourcePort)
  }
}