package calendarapp

import java.time.format.DateTimeFormatter

import java.time.LocalDateTime

class Reminder(dateTime: LocalDateTime):

    override def toString(): String = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)