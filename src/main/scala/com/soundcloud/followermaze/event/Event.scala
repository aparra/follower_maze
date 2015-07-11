package com.soundcloud.followermaze.event

import scala.util.parsing.combinator.RegexParsers

abstract class Event(sequence: Long)

case class Follow(sequence: Long, fromUser: Int, toUser: Int) extends Event(sequence)

case class Unfollow(sequence: Long, fromUser: Int, toUser: Int) extends Event(sequence)

case class Broadcast(sequence: Long) extends Event(sequence)

case class PrivateMessage(sequence: Long, fromUser: Int, toUser: Int) extends Event(sequence)

case class StatusEvent(sequence: Long, fromUser: Int) extends Event(sequence)

object Event extends RegexParsers {

  def sequence = """(\d+)""".r ^^ { _.toLong }

  def user = """(\d+)""".r ^^ { _.toInt }

  def follow: Parser[Follow] = sequence ~ "|F|" ~ user ~ "|" ~ user ^^ {
    case sequence ~ _ ~ fromUser ~ _ ~ toUser => Follow(sequence, fromUser, toUser)
  }

  def unfollow: Parser[Unfollow] = sequence ~ "|U|" ~ user ~ "|" ~ user ^^ {
    case sequence ~ _ ~ fromUser ~ _ ~ toUser => Unfollow(sequence, fromUser, toUser)
  }

  def broadcast: Parser[Broadcast] = sequence ~ "|B" ^^ {
    case sequence ~ _ => Broadcast(sequence)
  }

  def privateMessage: Parser[PrivateMessage] = sequence ~ "|P|" ~ user ~ "|" ~ user ^^ { 
    case sequence ~ _ ~ fromUser ~ _ ~ toUser => PrivateMessage(sequence, fromUser, toUser)
  }

  def statusEvent: Parser[StatusEvent] = sequence ~ "|S|" ~ user ^^ { 
    case sequence ~ _ ~ fromUser => StatusEvent(sequence, fromUser)
  }

  def event = follow | unfollow | broadcast | privateMessage | statusEvent

  def apply(payload: String): Event = parseAll(event, payload) match {
    case Success(event, _) => event
    case failure: NoSuccess    => throw new IllegalArgumentException("event")
  }
}