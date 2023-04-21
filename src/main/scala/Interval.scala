package CalendarApp

import java.time.{LocalDate, LocalDateTime, DayOfWeek, Year}
import java.time.temporal.{TemporalAdjusters, ChronoUnit}
import java.time.format.DateTimeFormatter

sealed abstract class Interval(val start: LocalDateTime, val end: LocalDateTime): 

  def contains(e: Event): Boolean = 
    
    (e.startTime.isAfter(start) && e.startTime.isBefore(end)) || (e.endTime.isAfter(start) && e.endTime.isBefore(end))

class Day(start: LocalDateTime, end : LocalDateTime) extends Interval(start, end):

  def next = Day(start.plusDays(1), end.plusDays(1))

  def previous = Day(start.minusDays(1), end.minusDays(1))

  override def toString(): String = s"Day from $start to $end"

object Day:

  def getDay(t :LocalDateTime): Day = 

    val s = beginningOfDay(t)

    val e = s.plusDays(1)

    Day(s,e)

  def beginningOfDay(t :LocalDateTime) =

    t.truncatedTo(ChronoUnit.DAYS)

class Week(start: LocalDateTime, end : LocalDateTime) extends Interval(start, end):

  def next = Week(start.plusWeeks(1), end.plusWeeks(1))

  def previous = Week(start.minusWeeks(1), end.minusWeeks(1))

  override def toString(): String = s"Week from $start to $end"

object Week:

  def getWeek(t :LocalDateTime): Week =  

    val s = Day.beginningOfDay(t.`with`(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)))

    val e = s.plusDays(7)

    Week(s,e)

class Month(start: LocalDateTime, end : LocalDateTime) extends Interval(start, end): 

  private val _daysInMonth = start.getMonth().length(Year.of(start.getYear()).isLeap())

  private val _monthNameWithYear: String = start.format(DateTimeFormatter.ofPattern("MMMM YYYY"))

  def next: Month = Month(start.plusMonths(1), end.plusMonths(1))

  def previous: Month = Month(start.minusMonths(1), end.minusMonths(1))

  def daysInMonth: Int = _daysInMonth

  def monthNameWithYear: String = _monthNameWithYear

  override def toString(): String = s"Month from $start to $end"  

object Month:

  def getMonth(t: LocalDateTime): Month =

    val s = Day.beginningOfDay(t.`with`(TemporalAdjusters.firstDayOfMonth()))

    val e = Day.beginningOfDay(t.`with`(TemporalAdjusters.lastDayOfMonth())).plusDays(1)

    Month(s,e)

