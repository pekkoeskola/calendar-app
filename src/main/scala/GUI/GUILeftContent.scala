package calendarapp
package GUI

import scalafx.Includes._
import scalafx.beans.property.ObjectProperty
import scalafx.scene.layout.VBox
import scalafx.scene.layout.HBox
import scalafx.scene.control.CheckBox
import scalafx.scene.control.Label
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.DatePicker
import scalafx.scene.control.Button
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonType

class GUILeftContent(runningInstance: CalendarApp, calendarView: GUICalendarView, dialogs: GUIDialogs):

  val content = ObjectProperty(new VBox)

  private var datePickerandCalendars = new VBox

  def initialise() =

    val dp = new DatePicker
    dp.value.onChange {
      (_,_,n) => {
        calendarView.goto(n)
      }
    }

    datePickerandCalendars = new VBox{
      children = Seq(new HBox{children = dp}, new Label("Calendars:"))
    }

    val calendars = ObservableBuffer[Calendar]()
    runningInstance.calendars.foreach(x => calendars.add(x))
    val calendarToggles = calendars.map(c => new HBox{children = new CheckBox(c.name){
        selected = true
        onAction = handle{
          runningInstance.toggleCalendarFilter(c)
          calendarView.update()}
      }})

    calendarToggles.foreach(ct => datePickerandCalendars.getChildren().add(ct))

    datePickerandCalendars.getChildren().add(new Label("Event Categories:"))

    update()

  def update():Unit =

    val list = new VBox

    runningInstance.eventCategories.map(eC => list.getChildren().add(
      new HBox{
        children = Seq(
          new CheckBox(eC.name){
            selected = true
            onAction = handle{
              runningInstance.toggleEventCategoryFilter(eC)
              calendarView.update()
            }
          },
          new Button("x"){
            onAction = handle{
              val alert = dialogs.deleteEventCategoryDialog(eC)
              val result = alert.showAndWait()

              result match
                case Some(ButtonType.OK) => 
                  runningInstance.deleteEventCategory(eC)
                  calendarView.update()
                  update()
                case _ =>
            }
          }
        )
      }
    ))

    content() = new VBox{
      children = Seq(datePickerandCalendars, list, new Button("New Event Category"){
        onAction = handle{
          var failed = true
          while failed do
            val dialog = dialogs.newEventCategoryDialog
            val result = dialog.showAndWait()

            result match
              case Some(res: NewEventCategoryDialogResult) => 
                res match
                  case NewEventCategoryDialogResult(true, Some(eCat)) =>
                    runningInstance.addEventCategory(eCat)
                    failed = false
                  case NewEventCategoryDialogResult(false, None) =>
                    failed = true
                    new Alert(AlertType.Error) {
                      title = "Error Dialog"
                      headerText = "Error during event category creation"
                      contentText = "name already in use"
                    }.showAndWait()
                  case _ =>
                end match

              case _ =>
                failed = false
            end match
          
          end while
          update()     
        }
      })
    }
  
  end update

      