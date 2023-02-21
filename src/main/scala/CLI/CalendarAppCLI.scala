package CalendarApp
package CLI

import scala.io.StdIn.readLine

class CalendarAppCLI(val calendarApp: CalendarApp):

  val validCommands = Set("exit")

  def run() = 

    println("Welcome to CalendarApp")
    println()

    weeklyview()

    var exit = false

    while !exit do

      if requestCommand() == "exit" then exit = true else exit = false

  def weeklyview(): Unit =

    println("this week's events:\n")

    for e <- calendarApp.fetchEvents do println(e)

  def requestCommand(): String =

    var valid = false

    var input = ""

    while !valid do
      input = readLine("Enter command: ")

      if validCommands.contains(input) then valid = true else println("\ninvalid command please try again\n")

    input