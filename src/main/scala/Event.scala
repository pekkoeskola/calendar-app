package calendarapp

import java.time.LocalDateTime

class Event(
  var name: String,
  var calendar: Calendar, 
  var startTime: LocalDateTime, 
  var endTime: LocalDateTime, 
  var location: Option[String] = None,
  var participants: Option[String] = None,
  var eventCategory: Option[EventCategory] = None,
  var reminder: Option[Reminder] = None):

  override def toString = s"Event \"$name\" at $startTime till $endTime"