package calendarapp
package CLI

import scala.io.StdIn.readLine

class CalendarAppCLI(val calendarApp: CalendarApp):

  val validCommands = Set("exit", "next", "previous")

  def run() = 

    println("Welcome to CalendarApp")
    println()

    printView()

    var exit = false

    while !exit do

      println()

      requestCommand() match
        case "next" =>
          calendarApp.nextView()
          printView()
        case "previous" =>
          calendarApp.previousView()
          printView()
        case "exit" => exit = true
        case _ => println("oops")

  def printView() =

    println()

    val q = calendarApp.getView

    println(q(0))
    for i <- q(1) do
      println(i)


  def requestCommand(): String =

    var valid = false

    var input = ""

    while !valid do
      input = readLine("Enter command: ")

      if validCommands.contains(input) then valid = true else println("\ninvalid command please try again\n")

    input