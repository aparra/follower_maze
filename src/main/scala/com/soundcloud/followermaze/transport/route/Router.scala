package com.soundcloud.followermaze.transport.route

import java.net.Socket
import scala.collection.Set
import scala.collection.mutable.HashMap
import org.slf4j.LoggerFactory.getLogger
import com.soundcloud.followermaze.event.Event
import com.soundcloud.followermaze.transport.RichSocket._
import com.soundcloud.followermaze.user.UserRepository

class Router(userRepository: UserRepository, routes: Set[Route]) {
  private val LOGGER = getLogger(this.getClass)
  
  val session = new HashMap[Int, Socket]()
  val deliver = new SequenceBuffer()
  
  def openSession(client: Socket) {
    val user = userRepository.save(client.userId)
    session(user.id) = client
    LOGGER.info("User {} is connected", user.id);
  }

  def tryDeliver(event: Event) {
    deliver.tryDeliver(event)
  }

  private def routeTo(event: Event) {
    routes.find { route => route.accept(event) } match {
      case Some(route) => {
        val usersToNotify = route.process(event, usersConnected = session.keySet)
        notifyAll(usersToNotify, event)
      }
      case _ => LOGGER.error("No route to event {}", event)
    }
  }
  
  private def notifyAll(users: Set[Int], event: Event) {
    users.foreach(userId => {
      if (hasSessionActive(userId)) {
        session(userId).send(event.toString)
        LOGGER.debug("Sent event {} to user {}", event, userId)
      } else {
        LOGGER.debug("User {} disconnected, dropped event {}", userId, event)
      }
    })
  }
  
  private def hasSessionActive(userId: Int): Boolean = session.contains(userId)

  class SequenceBuffer(var nextToDelivery: Long = 1) {
    val buffer = new HashMap[Long, Event]

    def tryDeliver(event: Event) {
      buffer(event.sequence) = event
      while (buffer.contains(nextToDelivery)) {
        routeTo(buffer.remove(nextToDelivery).get)
        nextToDelivery += 1
      }
    }
  }
}