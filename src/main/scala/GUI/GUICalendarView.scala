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

class GUICalendarView(runningInstance: CalendarApp){

  val calendar = ObjectProperty(new VBox{
      children = new FlowPane {
        children = monthConstructor()
      }
    }
  )

  val caption = new StringProperty(runningInstance.getView()._1.asInstanceOf[MonthView].month.monthNameWithYear)

  def nextView() = 
    runningInstance.nextView()

    calendar() = new VBox{
      children = new FlowPane {
        children = monthConstructor()
      }
    }
    //TODO change this
    caption() = runningInstance.getView()._1.asInstanceOf[MonthView].month.monthNameWithYear

  def previousView() = 
    runningInstance.previousView()

    calendar() = new VBox{
      children = new FlowPane {
        children = monthConstructor()
      }
    }
    //TODO change this
    caption() = runningInstance.getView()._1.asInstanceOf[MonthView].month.monthNameWithYear

  def changeViewType = ???

  private def monthConstructor(): Seq[VBox] =
    val vbox = Buffer[VBox]()
    val days = runningInstance.getView()._1.interval.asInstanceOf[Month].daysInMonth
    for i <- 1 to days do
      val day = new VBox{
        minWidth = 100
        minHeight = 100
        children = {new Text(s"${i}")}
      }
      vbox += day
    vbox.toSeq
  end monthConstructor
}

