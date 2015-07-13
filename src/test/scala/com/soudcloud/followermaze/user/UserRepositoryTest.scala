package com.soudcloud.followermaze.user

import org.specs2.mutable.Specification
import com.soundcloud.followermaze.user.{ User, UserRepository }

class UserRepositoryTest extends Specification {

  "User repository" should {
    val userRepository = new UserRepository()

    "returns User even when user_id not exist" in {
      userRepository.get(1).id must beEqualTo(1)
    }
    
    "returns User for existing user_id" in {
      userRepository.save(42)
      userRepository.get(42).id must beEqualTo(42)
    }
  }
}