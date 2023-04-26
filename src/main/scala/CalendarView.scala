package calendarapp

import java.time.LocalDateTime

sealed abstract class CalendarView(val interval: Interval):

  def next: CalendarView

  def previous: CalendarView

object CalendarView:

  val DAYVIEW = 1

  val WEEKVIEW = 2

  val MONTHVIEW = 3

  def changeViewType(d: LocalDateTime, i: Int): CalendarView = 

    i match
      case 1 => DayView(Day.getDay(d))
      case 2 => WeekView(Week.getWeek(d))
      case 3 => MonthView(Month.getMonth(d))

class DayView(val day: Day) extends CalendarView(day):

  def next = DayView(day.next)

  def previous = DayView(day.previous)

class WeekView(val week: Week) extends CalendarView(week):

  def next = WeekView(week.next)

  def previous = WeekView(week.previous)

  override def toString(): String = week.toString()

class MonthView(val month: Month) extends CalendarView(month):

  def next = MonthView(month.next)

  def previous = MonthView(month.previous)