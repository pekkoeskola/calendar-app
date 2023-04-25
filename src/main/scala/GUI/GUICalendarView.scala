package CalendarApp
package GUI

import java.time.LocalDate
import java.time.LocalDateTime

//scalafx imports
import scalafx.Includes._
import scalafx.scene.layout.VBox
import scala.collection.mutable.Buffer
import scalafx.scene.text.Text
import scalafx.scene.layout.FlowPane
import scalafx.beans.property.ObjectProperty
import scalafx.scene.Node
import scalafx.beans.property.StringProperty
import scalafx.scene.control.Label
import scalafx.scene.layout.HBox
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.ScrollPane
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.Priority
import scalafx.scene.layout.ColumnConstraints
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType


/** Manages the calendar view part of the GUI
  * 
  *
  * @param runningInstance the currently running instance of CalendarApp connected to the GUI
  */
class GUICalendarView(runningInstance: CalendarApp, dialogs: GUIDialogs){

  val calendar = ObjectProperty(new VBox)

  val caption = new StringProperty(runningInstance.getView._1.interval.monthNameWithYear)

  def initialise() =
    update()

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

    val viewAndEvents = runningInstance.getView

    viewAndEvents._1 match
      case d: DayView =>
        calendar() = new VBox{
          hgrow = Priority.ALWAYS
          children = new ScrollPane{
            prefViewportHeight = 1500
            fitToWidth = true
            vbarPolicy = ScrollPane.ScrollBarPolicy.ALWAYS
            content = dayConstructor(d, viewAndEvents._2)
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
            children = monthConstructor(m, viewAndEvents._2)
          }
        }

    caption() = runningInstance.getView._1.interval.monthNameWithYear

  private def dayConstructor(view: DayView, events: Vector[Event]): GridPane = 

    case class row(e: Vector[Event], i: AnyInterval)

    val rows = Buffer[row]()
    val rowsToRender = Buffer[HBox]()
  
    for h <- 0 to 23 do
      val i1 = AnyInterval(view.day.start.plusHours(h), view.day.start.plusHours(h).plusMinutes(30)) 
      val i2 = AnyInterval(view.day.start.plusHours(h).plusMinutes(30), view.day.start.plusHours(h+1))
      rows += row(events.filter(i1.contains(_)), i1)
      rows += row(events.filter(i2.contains(_)), i2)

    val leftcol = new ColumnConstraints
    leftcol.hgrow = Priority.Never
    val rightcol = new ColumnConstraints
    rightcol.hgrow = Priority.Always

    val g = new GridPane{
      hgap = 10
      style = "-fx-border-color: black"
    }
    g.getColumnConstraints().add(leftcol)
    g.getColumnConstraints().add(rightcol)

    var isHour = true
    var rowCounter = 0

    for r <- rows do

      var cellStyle = ""

      if isHour then
        g.add(new Label(r.i.start.getHour.toString), 0, rowCounter)
        isHour = false
        cellStyle = "-fx-border-style: solid none none none; -fx-border-color: black"
      else
        g.add(new Label("  "), 0, rowCounter)
        isHour = true
        cellStyle = ""

      if !r.e.isEmpty then

        val rowEvents = new HBox{
          hgrow = Priority.Always
          maxWidth = Double.MaxValue
        }

        r.e.foreach(x => rowEvents.getChildren().add(new Label(x.name){
          style = cellStyle + "; -fx-text-fill: black; -fx-background-color: red"
          maxWidth = Double.MaxValue
          hgrow = Priority.Always

          onMouseClicked = handle{
            var failed = true
            while failed do
              val dialog = dialogs.modifyorDeleteEventDialog(x)

              val result = dialog.showAndWait()

              result match
                case Some(res: ModifyorDeleteEventDialogResult) =>
                  res match
                    case ModifyorDeleteEventDialogResult(true, false, Some(e), Some(ogEvent), None) =>
                      runningInstance.addEvent(e)
                      update()
                      failed = false
                    case ModifyorDeleteEventDialogResult(false, false, None, None,Some(d)) =>
                      failed = true
                      new Alert(AlertType.Error) {
                        title = "Error Dialog"
                        headerText = "Error during event creation"
                        contentText = d
                      }.showAndWait()
                    case ModifyorDeleteEventDialogResult(true, true, None, Some(ogEvent), None) => 
                      runningInstance.deleteEvent(ogEvent)
                      println("deleted")
                      failed = false
                    case _ =>
                  end match

                case _ => 
                  failed = false
              end match
              
            end while
            update()
            println("updated")
          }
        }))
        g.add(rowEvents, 1, rowCounter)
      else
        val empty = new Label("empty"){
          style = cellStyle
          maxWidth = Double.MaxValue
          hgrow = Priority.ALWAYS
        }

        g.add(empty, 1, rowCounter)

      rowCounter += 1

    g

  private def monthConstructor(view: MonthView, events: Vector[Event]): Seq[VBox] =
    val vbox = Buffer[VBox]()
    val days = view.interval.asInstanceOf[Month].daysInMonth
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

