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
import scalafx.scene.AccessibleRole.TableView
import scalafx.scene.control.DatePicker
import scalafx.geometry.Pos
import scalafx.geometry.Insets

import java.time.LocalDate
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.Region
import scalafx.scene.layout.Priority
import scalafx.scene.layout.BorderPane
import scalafx.scene.layout.TilePane
import scalafx.scene.layout.ColumnConstraints
import scalafx.scene.control.ToolBar
import scalafx.scene.layout.Border
import scalafx.scene.layout.FlowPane
import scalafx.scene.layout.Background
import scalafx.scene.layout.BackgroundFill
import scalafx.scene.layout.CornerRadii

object CalendarAppGUI extends JFXApp3{

  def start(): Unit = {

    stage = new JFXApp3.PrimaryStage{
      title = "Calendar"
      width = 1200
      height = 600

      var col = ObjectProperty(White)

      scene = new Scene {

        val dp = new DatePicker(LocalDate.now())

        //button1.onMouseClicked = event => println(5+5)

        val leftButtons = new HBox{
          children = Seq(
            new Button{
              text = "New Event"
            },
            new Text{
              text = "Month here"
            }
          )
        }

        val rightButtons = new HBox{
          children = Seq(
            new Button{
              text = "change view"
            },
            new Button{
              text = "<"
            },
            new Button{
              text = ">"
            },                    
            new Button{
              text = "search"
            }
          )
        }

        val leftcol = new ColumnConstraints
        leftcol.hgrow = Priority.Always
        val rightcol = new ColumnConstraints
        rightcol.hgrow = Priority.Never

        val ins = Insets(4)
        val ins0 = Insets(0)

        val topFill = BackgroundFill(Color.Gray, CornerRadii.Empty,ins0)
        val topBG = Background(Array(topFill))

        root = new BorderPane{
          top = new GridPane{ //top toolbar
            background = topBG
            add(leftButtons, 0, 0)
            add(rightButtons, 1, 0)
            columnConstraints = Seq(leftcol, rightcol)
            padding = ins
          }
          center = new FlowPane {
            children = (1 to 5).toSeq.map(_ => scalafx.scene.shape.Rectangle(50, 50, Color.DarkGray))
          }
          left = new VBox {
            children = (dp)
          }
        }
      }
    }
  }
}
