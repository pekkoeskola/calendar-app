package CalendarApp

import java.time.{LocalDateTime, DayOfWeek}
import java.time.temporal.{TemporalAdjusters, ChronoUnit}

abstract class Interval(val start: LocalDateTime, val end: LocalDateTime):

  def next: Interval

  def previous: Interval

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

  def next = Day(start.plusWeeks(1), end.plusWeeks(1))

  def previous = Day(start.minusWeeks(1), end.minusWeeks(1))

  override def toString(): String = s"Week from $start to $end"

object Week:

  def getWeek(t :LocalDateTime): Week =  

    val s = Day.beginningOfDay(t.`with`(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)))

    val e = s.plusDays(7)

    Week(s,e)

class Month(start: LocalDateTime, end : LocalDateTime) extends Interval(start, end): 

  def next = Day(start.plusMonths(1), end.plusMonths(1))

  def previous = Day(start.minusMonths(1), end.minusMonths(1))

  override def toString(): String = s"Month from $start to $end"  