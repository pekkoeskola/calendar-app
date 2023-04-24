package CalendarApp
package GUI

import scala.collection.mutable.Buffer

//scalafx imports
import scalafx.Includes._
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
import scalafx.beans.property.BooleanProperty
import scalafx.scene.layout.Pane
import scalafx.scene.Node
import scalafx.scene.control.ChoiceBox
import scalafx.collections.ObservableBuffer
import scalafx.beans.property.StringProperty

object CalendarAppGUI extends JFXApp3{
  def start(): Unit = {

    //initialisation
    
    val AppInstance = new CalendarApp

    val dialogs = new Dialogs

    AppInstance.startUp()

    val viewBuilder = GUICalendarView(AppInstance)

    //build ui

    val leftButtons = new HBox{
      children = Seq(
        new Button{
          text = "New Event"

          onAction = e => {
            val dialog = dialogs.createNewEventDialog

            val result = dialog.showAndWait()
          }
        },
        new Text{
          text <== viewBuilder.caption
        }
      )
    }

    val viewChoices = ObservableBuffer("Day", "Week", "Month")
    val viewChoiceBox = new ChoiceBox(viewChoices)

    viewChoiceBox.onAction = e => {

      val chosen = viewChoiceBox.value()

      chosen match
        case "Day" => viewBuilder.changeViewType(CalendarView.DAYVIEW)
        case "Week" => viewBuilder.changeViewType(CalendarView.WEEKVIEW)
        case "Month" => viewBuilder.changeViewType(CalendarView.MONTHVIEW)
    }

    val rightButtons = new HBox{
      children = Seq(
        viewChoiceBox,
        new Button{
          text = "<"
          onAction = {e =>
            viewBuilder.previousView()}
        },
        new Button{
          text = ">"
          onAction = {e =>
            viewBuilder.nextView()}
        },                   
        new Button{
          text = "search"
        }
      )
    }

    val topToolBar = new GridPane{
      val leftcol = new ColumnConstraints
      leftcol.hgrow = Priority.Always
      val rightcol = new ColumnConstraints
      rightcol.hgrow = Priority.Never

      val ins = Insets(4)
      val ins0 = Insets(0)

      val topFill = BackgroundFill(Color.Gray, CornerRadii.Empty,ins0)
      val topBG = Background(Array(topFill))

      background = topBG
      add(leftButtons, 0, 0)
      add(rightButtons, 1, 0)
      columnConstraints = Seq(leftcol, rightcol)
      padding = ins
    }

    val bp = new BorderPane{
      val topButtons = topToolBar
      val dp = new DatePicker

      top = topButtons

      left = dp

      center <== viewBuilder.calendar
    }

    val monthView = new Scene {
      root = bp
    }

    stage = new JFXApp3.PrimaryStage{
      title = "Calendar"
      width = 1200
      height = 800

      scene = monthView
    } 
  }
}

