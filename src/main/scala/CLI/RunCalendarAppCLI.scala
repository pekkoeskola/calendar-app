package CalendarApp
package CLI

object RunCalendarAppCLI extends App:

  val calendarApp = new CalendarApp

  calendarApp.startUp()

  val instance = new CalendarAppCLI(calendarApp)

  instance.run()

