package CalendarApp

import scala.collection.mutable.Buffer

class Calendar(var name: String):

  //constructor
  def this(name: String, events: Buffer[Event]) = 
    this(name)
    _events = events

  private var _events = Buffer[Event]()

  def events = _events.toVector
  

  def addEvent(event: Event) = _events += event

  def deleteEvent(event: Event) = _events -= event