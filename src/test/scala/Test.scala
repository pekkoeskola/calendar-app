package CalendarApp

import java.time.LocalDateTime

object Test extends App:

  var t = LocalDateTime.now()

  var begt = Day.beginningOfDay(t)

  var dayt = Day.getDay(t)

  var weekt = Week.getWeek(t)

  println(t)

  println(begt)

  println(dayt)

  println(weekt)

