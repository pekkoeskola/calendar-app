package CalendarApp
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

  def update() =

    val list = new VBox

    runningInstance.eventCategories.map(eC => list.getChildren().add(
      new HBox{
        children = Seq(new CheckBox{
          selected = true
          onAction = handle{
            runningInstance.toggleEventCategoryFilter(eC)
            calendarView.update()
          }
        },
        new Label(eC.name){

        }
        )
      }
    ))

    content().getChildren().add(datePickerandCalendars)

    content().getChildren().add(list)

    content().getChildren().add(new Button("New Event Category")) 
  
  end update

      