package calendarapp
package IO

import java.io._


import java.time.format.DateTimeFormatter


class WriteCalendar:

  def writeCalendar(calendar: Calendar, eventCategories: Option[Vector[EventCategory]]) = 
    try
      val fileOut= FileWriter("./calendars/" + calendar.name + ".txt")
      val printer = PrintWriter(fileOut, true)
      try 

        eventCategories match
          case Some(cats: Vector[EventCategory]) =>
            for c <- cats do
              printer.println("EventCategoryStart")

              printer.println(c.name)
              printer.println(c.color)
              for cOW <- c.cantOverlapWith do
                printer.print(cOW.name)
                printer.print(",")
              printer.println()
              
              printer.println("EventCategoryEnd")
          case None =>

        for e <- calendar.events do
          printer.println("EventStart")

          printer.println(e.name)
          printer.println(e.startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
          printer.println(e.endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
          printer.println(e.location.getOrElse(""))
          printer.println(e.participants.getOrElse(""))
          printer.println(e.webLink.getOrElse(""))
          printer.println(e.description.getOrElse(""))
          e.eventCategory match
            case Some(e) =>
              printer.println(e.name)
            case None =>
              printer.println("")
          printer.println(e.reminder.getOrElse(""))

          printer.println("EventEnd")

      finally
        fileOut.close()
        printer.close()
    catch
      case e1: FileNotFoundException =>
        println("file not found")
      case e2: IOException =>
        println("oops")