package CalendarApp

import scala.collection.mutable.Buffer

import java.util.Date

class CalendarApp:

  val calendars = Buffer[Calendar]()

  //stub
  def startUp() =

    this.addCalendar(Calendar("test", Buffer(Event("event1", new Date, new Date))))

  def addCalendar(calendar: Calendar) = calendars += calendar

  def addEvent(event: Event, calendar: Calendar) = calendar.addEvent(event) 

  def deleteEvent(event: Event, calendar: Calendar) = calendar.deleteEvent(event)

  //stub, actual implementation takes in an interval as parameter
  def fetchEvents: Vector[Event] = 

    val ret = Buffer[Event]()

    for c <- this.calendars
      e <- c.events
    do ret += e

    ret.toVector