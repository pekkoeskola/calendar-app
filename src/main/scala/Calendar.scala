package CalendarApp

import scala.collection.mutable.Buffer

class Calendar(var name: String, val events: Buffer[Event]):

  def addEvent(event: Event) = events += event

  def deleteEvent(event: Event) = events -= event