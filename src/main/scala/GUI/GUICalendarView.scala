package CalendarApp
package GUI

import scalafx.Includes._
import scalafx.scene.layout.VBox
import scala.collection.mutable.Buffer
import scalafx.scene.text.Text
import scalafx.scene.layout.FlowPane
import scalafx.beans.property.ObjectProperty
import scalafx.scene.Node
import scalafx.beans.property.StringProperty
import scalafx.scene.control.Label
import java.time.LocalDate

/** Manages the calendar view part of the GUI
  * 
  *
  * @param runningInstance the currently running instance of CalendarApp connected to the GUI
  */
class GUICalendarView(runningInstance: CalendarApp){

  val calendar = ObjectProperty(new VBox{
      children = new FlowPane {
        children = monthConstructor()
      }
    }
  )

  val caption = new StringProperty(runningInstance.getView._1.interval.monthNameWithYear)

  def nextView() = 
    runningInstance.nextView()
    update()

  def previousView() = 
    runningInstance.previousView()
    update()

  def changeViewType(newViewType: Int) = 

    runningInstance.changeViewType(newViewType)
    update()

  def goto(d: LocalDate) =

    runningInstance.goToDate(d.atStartOfDay())

    update()

  def update() =

    runningInstance.getView._1 match
      case d: DayView =>
        calendar() = new VBox{
          children = new Label{
            text = "day"
          }
        }
      case w: WeekView =>
        calendar() = new VBox{
          children = new Label{
            text = "Week"
          }
        }
      case m: MonthView =>
        calendar() = new VBox{
          children = new FlowPane {
            children = monthConstructor()
          }
        }

    caption() = runningInstance.getView._1.interval.monthNameWithYear

  private def dayConstructor() = ???

  private def monthConstructor(): Seq[VBox] =
    val vbox = Buffer[VBox]()
    val days = runningInstance.getView._1.interval.asInstanceOf[Month].daysInMonth
    for i <- 1 to days do
      val day = new VBox{
        minWidth = 120
        minHeight = 120
        children = {new Text(s"${i}")}
      }
      vbox += day
    vbox.toSeq
  end monthConstructor
}

