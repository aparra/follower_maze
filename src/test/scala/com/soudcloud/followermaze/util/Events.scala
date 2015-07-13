package com.soudcloud.followermaze.util

import com.soundcloud.followermaze.event.Event
import com.soundcloud.followermaze.event.EventType

object Events {

  val sample: List[Event] = List(Event("1|F|1|2"), Event("2|U|1|3"), Event("3|B"), Event("4|P|1|2"), Event("5|S|1"))
 
  def event(eventType: EventType): Event = sample.find { _.is(eventType) }.get
  
  def except(eventType: EventType): List[Event] = sample.filterNot { _.is(eventType) }
  
}