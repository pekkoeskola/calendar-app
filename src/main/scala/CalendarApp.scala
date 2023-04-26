package calendarapp

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

  private val _availableColors = Buffer[String]("blue", "darkblue", "dodgerblue", "deepskyblue", "mediumturqoise", 
    "red", "lightcoral", "lightpink", "lightsalmon", 
    "yellow", "khaki", "gold", "goldenrod", 
    "green", "mediumseagreen", "olive",
    "silver", "gray")

  private val calendarFilters = Buffer[Calendar]()

  private val eventCategoryFilters = Buffer[EventCategory]()

  private var _dateCursor = LocalDateTime.now()

  private var currentView: CalendarView = MonthView(Month.getMonth(currentTime))

  private var currentViewEvents: Vector[Event] = fetchEvents()

  def dateCursor = _dateCursor

  def calendars = _calendars

  def eventCategories = _eventCategories

  def availableColors = _availableColors


  //stub
  def startUp() =

    currentTime = LocalDateTime.now()

    this.addCalendar(Calendar("test"))

    val cat = EventCategory("category1", "khaki",Buffer[EventCategory]())

    this.addEvent(Event("event1", calendars(0), LocalDateTime.now(), LocalDateTime.now().plusHours(2), eventCategory = Some(cat)))

    this.addEvent(Event("Event2", calendars(0), LocalDateTime.now().plusWeeks(1), LocalDateTime.now().plusWeeks(1)))

    this.addEvent(Event("event3", calendars(0), LocalDateTime.now(), LocalDateTime.now().plusHours(1)))

    _eventCategories += cat

    currentViewEvents = fetchEvents()


  private def fetchEvents(): Vector[Event] = 

    val retevents = Buffer[Event]()

    for c <- (calendars.diff(calendarFilters))
      e <- c.events if currentView.interval.contains(e) && !eventCategoryFilters.contains(e.eventCategory.getOrElse(None)) 
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

  def modifyEvent(originalEvent: Event, modifiedEvent: Event) =
    deleteEvent(originalEvent)
    addEvent(modifiedEvent)

  def deleteEvent(event: Event) = 
    event.calendar.deleteEvent(event)
    update()

  def findAllEventsByCategory(eventCategory: EventCategory): Vector[Event] = 
    calendars.flatMap(_.events).filter(_.eventCategory.getOrElse(None) == eventCategory).toVector

  def addEventCategory(eventCategory: EventCategory) =
    _eventCategories += eventCategory

  def deleteEventCategory(eventCategory: EventCategory) =
    calendars.flatMap(_.events).filter(_.eventCategory.getOrElse(None) == eventCategory).foreach(_.eventCategory = None)
    _eventCategories -= eventCategory


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

  def findEventCategory(categoryName: String): EventCategory = 
    eventCategories.find(_.name == categoryName).getOrElse(throw CantFindException("can't find category with given name"))

  