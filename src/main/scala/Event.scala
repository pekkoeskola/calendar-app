package CalendarApp

import java.util.Date

class Event(
  var name: String, 
  var startTime: Date, 
  var endTime: Date, 
  var Location: Option[String] = None,
  var eventCategory: Option[EventCategory] = None):

  override def toString = s"Event \"$name\" at $startTime till $endTime"