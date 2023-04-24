package CalendarApp

import scala.collection.mutable.Buffer

import java.time.{LocalDate, LocalDateTime}



/**This class defines a running instance of CalendarApp. It acts as an API for connecting 
 * the core logic of the App to a UI; UI's communicate with the core logic primarily through 
 * this class and exclusively to modify data.
 * 
 * It is mutable through it's methods and essentially stores the state of the currently running CalendarApp.
  */
class CalendarApp:

  //may need updating
  private var currentTime = LocalDateTime.now()

  private val calendars = Buffer[Calendar]()

  private var _dateCursor = LocalDateTime.now()

  private var currentView: CalendarView = MonthView(Month.getMonth(currentTime))

  private var currentViewEvents = fetchEvents(currentView)

  def dateCursor = _dateCursor


  //stub
  def startUp() =

    currentTime = LocalDateTime.now()

    this.addCalendar(Calendar("test", Buffer(Event("event1", LocalDateTime.now(), LocalDateTime.now().plusHours(1)))))

    this.addEvent(calendars(0), Event("Event2", LocalDateTime.now().plusWeeks(1), LocalDateTime.now().plusWeeks(1)))

    currentViewEvents = fetchEvents(currentView)


  def addCalendar(calendar: Calendar) = 
    calendars += calendar
    fetchEvents(currentView)


  def addEvent(calendar: Calendar, event: Event) = 
    calendar.addEvent(event)
    fetchEvents(currentView)

  def deleteEvent(calendar: Calendar, event: Event) = 
    calendar.deleteEvent(event)
    fetchEvents(currentView)


  def changeViewType(i: Int) = //TODO
    currentView = CalendarView.changeViewType(dateCursor, i)
    currentViewEvents = fetchEvents(currentView)

  def nextView() = 
    currentView = currentView.next
    _dateCursor = currentView.interval.start
    currentViewEvents = fetchEvents(currentView)

  def previousView() = 
    currentView = currentView.previous
    _dateCursor = currentView.interval.start
    currentViewEvents = fetchEvents(currentView)


  def getView: (CalendarView, Vector[Event]) = (currentView, currentViewEvents)


  private def fetchEvents(v :CalendarView): Vector[Event] = 

    val retevents = Buffer[Event]()

    for c <- this.calendars
      e <- c.events if v.interval.contains(e)
    do retevents += e

    retevents.toVector

  