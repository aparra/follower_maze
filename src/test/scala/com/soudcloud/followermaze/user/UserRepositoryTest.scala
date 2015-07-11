package com.soudcloud.followermaze.user

import org.specs2.mutable.Specification
import com.soundcloud.followermaze.user.{ User, UserRepository }

class UserRepositoryTest extends Specification {

  "User repository" should {
    val userRepository = new UserRepository()

    "returns 'None' for unknown user" in {
      userRepository.findById(1) must beNone
    }
    
    "returns 'Some(User)' for known user" in {
      val user = User(42)
      userRepository.save(user)
      userRepository.findById(42).get must beEqualTo(user)
    }
  }
}