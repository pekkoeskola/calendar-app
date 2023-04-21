package CalendarApp
package GUI

import scalafx.scene.layout.VBox
import scala.collection.mutable.Buffer
import scalafx.scene.text.Text
import scalafx.scene.layout.FlowPane
import scalafx.beans.property.ObjectProperty

class MonthView(runningInstance: CalendarApp) {

  private def monthConstructor(): Seq[VBox] =
    val vbox = Buffer[VBox]()
    val days = runningInstance.currentView.interval.asInstanceOf[Month].daysInMonth
    for i <- 1 to days do
      val day = new VBox{
        minWidth = 100
        minHeight = 100
        children = new Text(s"${i}")
      }
      vbox += day
    vbox.toSeq
  end monthConstructor

  var centerContent = new FlowPane {
      children = monthConstructor()
  }

  def update() = 

    runningInstance.nextView()

    monthConstructor()

    centerContent = new FlowPane {
      children = monthConstructor()
    }
}

