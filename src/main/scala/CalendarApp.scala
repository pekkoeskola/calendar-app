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

  private var currentTime = LocalDateTime.now()

  //designate as private eventually
  val calendars = Buffer[Calendar]()

  private val calendarFilters = Buffer[Calendar]()

  private val eventCategoryFilters = Buffer[EventCategory]()

  private var _dateCursor = LocalDateTime.now()

  private var currentView: CalendarView = MonthView(Month.getMonth(currentTime))

  private var currentViewEvents: Vector[Event] = fetchEvents()

  def dateCursor = _dateCursor


  //stub
  def startUp() =

    currentTime = LocalDateTime.now()

    this.addCalendar(Calendar("test", Buffer(Event("event1", LocalDateTime.now(), LocalDateTime.now().plusHours(1)))))

    this.addEvent(calendars(0), Event("Event2", LocalDateTime.now().plusWeeks(1), LocalDateTime.now().plusWeeks(1)))

    currentViewEvents = fetchEvents()


  def addCalendar(calendar: Calendar) = 
    calendars += calendar
    fetchEvents()


  def addEvent(calendar: Calendar, event: Event) = 
    calendar.addEvent(event)
    fetchEvents()

  def deleteEvent(calendar: Calendar, event: Event) = 
    calendar.deleteEvent(event)
    fetchEvents()


  def changeViewType(i: Int) = //TODO
    currentView = CalendarView.changeViewType(dateCursor, i)
    currentViewEvents = fetchEvents()

  def nextView() = 
    currentView = currentView.next
    _dateCursor = currentView.interval.start
    currentViewEvents = fetchEvents()

  def previousView() = 
    currentView = currentView.previous
    _dateCursor = currentView.interval.start
    currentViewEvents = fetchEvents()

  def goToDate(date: LocalDateTime) = 

    currentView = currentView match
      case d: DayView => DayView(Day.getDay(date)) 
      case w: WeekView => WeekView(Week.getWeek(date))
      case m: MonthView => MonthView(Month.getMonth(date))

    _dateCursor = currentView.interval.start

  def getView: (CalendarView, Vector[Event]) = (currentView, currentViewEvents)


  def addCalendarFilter(calendar: Calendar) = ???

  def addEventCategoryFilter() = ???

  
  def findCalendar(calendarName: String): Calendar = 
    
    calendars.find(_.name == calendarName).getOrElse(throw CantFindException("can't find calendar with given name"))

  /** Updates the currentViewEvents variable of this class with the events contained by the currently active calendarView.
    * Takes into account the currently applicable calendarFilters and eventCategoryFilters.
    *
    * @return a Vector containing the events
    */
  private def fetchEvents(): Vector[Event] = 

    val retevents = Buffer[Event]()

    for c <- (calendars.diff(calendarFilters))
      e <- c.events if currentView.interval.contains(e) && !eventCategoryFilters.contains(e.eventCategory) 
    do retevents += e

    retevents.toVector

  