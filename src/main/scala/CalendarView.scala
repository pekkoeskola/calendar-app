package CalendarApp

import java.time.LocalDateTime

sealed abstract class CalendarView(val start: LocalDateTime,val end: LocalDateTime)

class DayView(val day: Day) extends CalendarView(day.start, day.end)

class WeekView(val week: Week) extends CalendarView(week.start, week.end)

class MonthView(val month: Month) extends CalendarView(month.start, month.end)