package CalendarApp

import java.time.LocalDateTime

class Event(
  var name: String, 
  var startTime: LocalDateTime, 
  var endTime: LocalDateTime, 
  var Location: Option[String] = None,
  var eventCategory: Option[EventCategory] = None):

  override def toString = s"Event \"$name\" at $startTime till $endTime"