package CalendarApp

import scala.collection.mutable.Buffer

import java.time.{LocalDate, LocalDateTime}



/**This class defines a running instance of CalendarApp. It acts as an API for connecting 
 * the core logic of the App to a UI; UI's communicate with the core logic primarily through 
 * this class and exclusively to modify data.
  */
class CalendarApp:

  //may need updating
  private var currentTime = LocalDateTime.now()

  private val calendars = Buffer[Calendar]()

  private var dateCursor = LocalDate.now()

  private var currentView: CalendarView = MonthView(Month.getMonth(currentTime))

  private var currentViewEvents = fetchEvents(currentView)

  //stub
  def startUp() =

    currentTime = LocalDateTime.now()

    this.addCalendar(Calendar("test", Buffer(Event("event1", LocalDateTime.now(), LocalDateTime.now().plusHours(1)))))

    this.addEvent(calendars(0), Event("Event2", LocalDateTime.now().plusWeeks(1), LocalDateTime.now().plusWeeks(1)))

    currentViewEvents = fetchEvents(currentView)


  def addCalendar(calendar: Calendar) = calendars += calendar


  def addEvent(calendar: Calendar, event: Event) = calendar.addEvent(event) 

  def deleteEvent(calendar: Calendar, event: Event) = calendar.deleteEvent(event)


  def changeViewType(i: Int) =
    currentView = CalendarView.changeViewType(dateCursor.atStartOfDay(), i)
    currentViewEvents = fetchEvents(currentView)

  def nextView() = 
    currentView = currentView.next
    currentViewEvents = fetchEvents(currentView)

  def previousView() = 
    currentView = currentView.previous
    currentViewEvents = fetchEvents(currentView)


  def getView(): (CalendarView, Vector[Event]) = (currentView, currentViewEvents)

  private def fetchEvents(v :CalendarView): Vector[Event] = 

    val retevents = Buffer[Event]()

    for c <- this.calendars
      e <- c.events if v.interval.contains(e)
    do retevents += e

    retevents.toVector

  