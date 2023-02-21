package CalendarApp

import scala.collection.mutable.Buffer

import java.time.LocalDateTime

class CalendarApp:

  //may need updating
  var currentTime = LocalDateTime.now()

  val calendars = Buffer[Calendar]()

  //stub
  def startUp() =

    currentTime = LocalDateTime.now()

    this.addCalendar(Calendar("test", Buffer(Event("event1", LocalDateTime.now(), LocalDateTime.now().plusHours(1)))))

    this.addEvent(calendars(0), Event("Event2", LocalDateTime.now().plusWeeks(2), LocalDateTime.now().plusWeeks(2)))

  def addCalendar(calendar: Calendar) = calendars += calendar

  def addEvent(calendar: Calendar, event: Event) = calendar.addEvent(event) 

  def deleteEvent(calendar: Calendar, event: Event) = calendar.deleteEvent(event)


  def fetchEvents(between: Interval) : Vector[Event] = 

    val retevents = Buffer[Event]()

    for c <- this.calendars
      e <- c.events if between.contains(e)
    do retevents += e

    retevents.toVector

  