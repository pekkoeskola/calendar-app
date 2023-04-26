package calendarapp
package IO

import java.io._

import calendarapp.Calendar
import scala.collection.mutable.Buffer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/** Class that contains methods for reading calendar data from different calendar files in ./calendars/. It assumes there are only .txt files
  * containing calendar data in the directory, that are formatted correctly. Has some but not full protection against all error cases.
  */

class ReadCalendar:

  def getCalendarFileNames(): Vector[String]=

    val calendarNames = scala.collection.mutable.Buffer[String]()

    val calendarDirectory = File("./calendars")

    val listing = calendarDirectory.listFiles()

    for f <- listing do
      if f.isFile() then
        calendarNames += f.getName()

    calendarNames.toVector
    
    //calendarNames.toVector.map(_.split("\\.")(0))



  def readEventCategories(fileName: String): Option[Vector[EventCategory]] =

    try
      val fileIn = FileReader("./calendars/" + fileName)
      val linesIn = BufferedReader(fileIn)

      try
        case class pseudoEventCategory(name: String, color: String, cantOverlapWith: Buffer[String])

        val pseudoEventCategories = Buffer[pseudoEventCategory]()

        var n: String = ""
        var c: String = ""
        val cOW = Buffer[String]()

        def constructpseudoEventCategory = 
          pseudoEventCategory(n,c, cOW)

        var oneLine = linesIn.readLine()
        var inEventCategory = false
        var linec = 0
        while oneLine != null && oneLine != "EventStart" do
          if inEventCategory && oneLine != "EventCategoryEnd" then
            if linec == 0 then
              n = oneLine
            else if linec == 1 then
              c = oneLine
            else
              if oneLine != "" then oneLine.split(",").dropRight(1).foreach(x => cOW += x)
            linec += 1
          else
            if oneLine == "EventCategoryEnd" then
              inEventCategory = false
              pseudoEventCategories += constructpseudoEventCategory
              linec = 0
            else if oneLine == "EventCategoryStart" then
              inEventCategory = true
          oneLine = linesIn.readLine()


        val eventCategories = Buffer[EventCategory]()

        //contains a bug so that cantOverLapWith data is lost for all read eventCategories, doesn't seem to have other side effects.
        def constructEventCategories() = 

          for c <- pseudoEventCategories do

            if c.cantOverlapWith.isEmpty then
              eventCategories += EventCategory(c.name, c.color, Buffer[EventCategory]())
            else
              val newc = EventCategory(c.name, c.color, Buffer[EventCategory]())
              for cInner <- c.cantOverlapWith do
                if eventCategories.forall(x => x.name != cInner) then
                  val newcat = pseudoEventCategories.find(_.name == cInner).get
                  val newcatreal = EventCategory(newcat.name, newcat.color, Buffer[EventCategory]())
                  eventCategories += newcatreal
                  newc.cantOverlapWith += newcatreal
                else
                  newc.cantOverlapWith += eventCategories.find(_.name == cInner).get
              eventCategories += newc

        constructEventCategories()

        //return this unless error
        if eventCategories.isEmpty then None else Some(eventCategories.toVector)

      finally
        fileIn.close()
        linesIn.close()
    catch
      case e1: FileNotFoundException =>
        println("file not found")
        None
      case e2: IOException =>
        println("oops")
        None

  def readCalendar(fileName: String, eventCategories: Vector[EventCategory]): Option[Calendar] =
    
    try

      val cal = Calendar(fileName.split("\\.")(0))

      val fileIn = FileReader("./calendars/" + fileName)
      val linesIn = BufferedReader(fileIn)
      try 
        var oneLine = linesIn.readLine()
        while oneLine != null && oneLine != "EventStart" do
          oneLine = linesIn.readLine()
        
        var inEvent = false
        var c = 0
        var n = ""
        var st = LocalDateTime.now()
        var et = LocalDateTime.now()
        var l = ""
        var p = ""
        var w = ""
        var d = ""
        var eC = ""
        var r = ""

        def myToOption(s: String): Option[String] = if s == "" then None else Some(s)

        def findEventCategory(categoryString: String) = if categoryString == "" then None else Some(eventCategories.find(_.name == categoryString)).get

        def constructEvent = 

          Event(n, cal, st, et, myToOption(l), myToOption(p), myToOption(w), myToOption(d), findEventCategory(eC) ,None)


        while oneLine != null do
          if inEvent && oneLine != "EventEnd" then
            if c == 0 then
              n = oneLine
            else if c == 1 then
              st = LocalDateTime.parse(oneLine, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            else if c == 2 then
              et = LocalDateTime.parse(oneLine, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            else if c == 3 then
              l = oneLine
            else if c == 4 then
              p = oneLine
            else if c == 5 then
              w = oneLine
            else if c == 6 then
              d = oneLine
            else if c == 7 then
              eC = oneLine
            c += 1
          else
            if oneLine == "EventStart" then
              inEvent = true
            else if oneLine == "EventEnd" then
              cal.addEvent(constructEvent)
              inEvent = false
              c = 0
          oneLine = linesIn.readLine()

        Some(cal)

      finally
        fileIn.close()
        linesIn.close()
    catch
      case e1: FileNotFoundException =>
        println("file not found")
        None
      case e2: IOException =>
        println("oops")
        None