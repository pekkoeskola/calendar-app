package calendarapp
package IO

import calendarapp.Calendar

import java.io._
import java.nio.file.Paths
import scala.collection.mutable.Buffer

object IOtest extends App:

  val r = new ReadCalendar

  //r.readCalendar("calendars/test.txt")

  //System.out.println(Paths.get("").toAbsolutePath());

  //println(r.getCalendars())

  val cat = EventCategory("category1", "khaki",Buffer[EventCategory]())

  val readcats = r.readEventCategories("test.txt").get

  readcats.foreach(x => println(x.cantOverlapWith))

  val res = r.readCalendar("test.txt",  Vector(cat))

  println("calendar: " + res.get.name)
  println(res.get.events)
  println(res.get.events(0).eventCategory.get.name)
