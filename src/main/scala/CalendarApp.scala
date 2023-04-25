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
  private val _calendars = Buffer[Calendar]()

  private val _eventCategories = Buffer[EventCategory]()

  private val calendarFilters = Buffer[Calendar]()

  private val eventCategoryFilters = Buffer[EventCategory]()

  private var _dateCursor = LocalDateTime.now()

  private var currentView: CalendarView = MonthView(Month.getMonth(currentTime))

  private var currentViewEvents: Vector[Event] = fetchEvents()

  def dateCursor = _dateCursor

  def calendars = _calendars

  def eventCategories = _eventCategories


  //stub
  def startUp() =

    currentTime = LocalDateTime.now()

    this.addCalendar(Calendar("test"))

    this.addEvent(Event("event1", calendars(0), LocalDateTime.now(), LocalDateTime.now().plusHours(2)))

    this.addEvent(Event("Event2", calendars(0), LocalDateTime.now().plusWeeks(1), LocalDateTime.now().plusWeeks(1)))

    this.addEvent(Event("event3", calendars(0), LocalDateTime.now(), LocalDateTime.now().plusHours(1)))

    _eventCategories += EventCategory("category1", Buffer[EventCategory]())

    currentViewEvents = fetchEvents()


  private def fetchEvents(): Vector[Event] = 

    val retevents = Buffer[Event]()

    for c <- (calendars.diff(calendarFilters))
      e <- c.events if currentView.interval.contains(e) && !eventCategoryFilters.contains(e.eventCategory) 
    do retevents += e

    retevents.toVector

  /** Updates the currentViewEvents variable of this class with the events contained by the currently active calendarView.
  * Takes into account the currently applicable calendarFilters and eventCategoryFilters. (Uses fetchEvents as a helper
  * function for this)
  */
  private def update() = 
    
    currentViewEvents = fetchEvents()

  def addCalendar(calendar: Calendar) = 
    calendars += calendar
    update()


  def addEvent(event: Event) = 
    event.calendar.addEvent(event)
    update()

  def modifyEvent(event: Event) = ???

  def deleteEvent(event: Event) = 
    event.calendar.deleteEvent(event)
    update()


  def changeViewType(i: Int) = //TODO
    currentView = CalendarView.changeViewType(dateCursor, i)
    update()

  def nextView() = 
    currentView = currentView.next
    _dateCursor = currentView.interval.start
    update()

  def previousView() = 
    currentView = currentView.previous
    _dateCursor = currentView.interval.start
    update()

  def goToDate(date: LocalDateTime) = 

    currentView = currentView match
      case d: DayView => DayView(Day.getDay(date)) 
      case w: WeekView => WeekView(Week.getWeek(date))
      case m: MonthView => MonthView(Month.getMonth(date))

    _dateCursor = currentView.interval.start

  def getView: (CalendarView, Vector[Event]) = (currentView, currentViewEvents)


  def toggleCalendarFilter(calendar: Calendar) = 
    if !calendarFilters.contains(calendar) then
      calendarFilters += calendar
    else
      calendarFilters -= calendar

    update()

  def toggleEventCategoryFilter(eventCategory: EventCategory) = 
    if !eventCategoryFilters.contains(eventCategory) then
      eventCategoryFilters += eventCategory
    else
      eventCategoryFilters -= eventCategory

    update()

  
  def findCalendar(calendarName: String): Calendar = 
    
    calendars.find(_.name == calendarName).getOrElse(throw CantFindException("can't find calendar with given name"))

  