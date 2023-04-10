package calendarApp
package GUI

import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.HBox
import scalafx.scene.paint.Color._
import scalafx.scene.paint._
import scalafx.scene.text.Text
import scalafx.scene.layout.VBox
import scalafx.scene.control.Button
import scalafx.beans.property.ObjectProperty

object CalendarAppGUI extends JFXApp3{

  def start(): Unit = {

    stage = new JFXApp3.PrimaryStage{
      title = "Calendar"
      width = 1000
      height = 600

      val button = new Button {
        text = "Click me"
      }

      var col = ObjectProperty(White)

      scene = new Scene {
        fill <== col
        content = new VBox{
          children = Seq(
            button
          )
        }
      }

      button.onMouseClicked = event => col() = Blue
    }
  }
}
