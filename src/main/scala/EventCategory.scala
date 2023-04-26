package calendarapp

import scala.collection.mutable.Buffer

class EventCategory(val name: String, val color: String, val cantOverlapWith: Buffer[EventCategory])
