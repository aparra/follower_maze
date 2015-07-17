package com.soudcloud.followermaze.route

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.net.Socket

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

import com.soundcloud.followermaze.event.Event
import com.soundcloud.followermaze.transport.route.BroadcastRoute
import com.soundcloud.followermaze.transport.route.Router
import com.soundcloud.followermaze.user.User
import com.soundcloud.followermaze.user.UserRepository

class RouterTest extends Specification with Mockito {

  "Router" should {
    "open a session" in {
      val router = new Router(userRepository, Set(new BroadcastRoute()))
      router.openSession(socketClient())
      router.session.containsKey(1) must beTrue
    }
    
    "store in buffer a message out of order" in {
      val router = new Router(userRepository, Set(new BroadcastRoute()))
      router.tryDeliver(Event("2|B"))
      router.deliver.buffer.size must beEqualTo(1)
      router.deliver.nextToDelivery must beEqualTo(1)
    }

    "deliver messages in order" in {
      val router = new Router(userRepository, Set(new BroadcastRoute()))
      val socket = socketClient()
      router.openSession(socket)

      val output = new ByteArrayOutputStream()
      socket.getOutputStream returns output       
    
      router.tryDeliver(Event("2|B"))
      router.tryDeliver(Event("1|B"))

      router.deliver.buffer.isEmpty must beTrue
      router.deliver.nextToDelivery must beEqualTo(3)
      
      output.toString("UTF-8") must beEqualTo("1|B\r\n2|B\r\n")
    }
  }
  
  def userRepository = {
    val userRepository = mock[UserRepository]
    userRepository.save(1) returns User(1)    
    userRepository
  }
  
  def socketClient() = {
    val client = mock[Socket]
    client.getInputStream returns new ByteArrayInputStream("1".getBytes())
    client
  }
}