package CalendarApp
package CLI


final case class IllegalCommandException(description: String, command: String) extends java.lang.Exception(description)