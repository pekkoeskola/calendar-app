package CalendarApp

import scala.collection.mutable.Buffer

class EventCategory(val name: String, val cantOverlapWith: Buffer[EventCategory])
