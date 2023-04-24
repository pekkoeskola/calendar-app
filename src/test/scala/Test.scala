package CalendarApp

import java.time.LocalDateTime

object Test extends App:

  var t = LocalDateTime.now()

  var begt = Day.beginningOfDay(t)

  var dayt = Day.getDay(t)

  val dayt2 = dayt.next

  var weekt = Week.getWeek(t)
  val week2 = weekt.next

  var montht = Month.getMonth(t)

  val e = Event("testevent", LocalDateTime.now(), dayt.next.start, None, None)

  println(t)

  println(begt)

  println(dayt)

  println(weekt)
  println(week2)

  println(montht)

  println(dayt2.contains(e))

