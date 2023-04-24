package CalendarApp
package GUI

import java.time.LocalDateTime
import java.time.LocalDate

//scalafx imports
import scalafx.Includes._
import scalafx.stage
import scalafx.scene.control.Dialog
import scalafx.scene.control.TextField
import scalafx.scene.control.ButtonType
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.VBox
import scalafx.scene.control.Label
import scalafx.geometry.Insets
import scalafx.scene.control.DatePicker
import scalafx.scene.layout.HBox
import scalafx.scene.control.ChoiceBox
import scalafx.collections.ObservableBuffer
import scalafx.beans.property.BooleanProperty


case class NewEventDialogResult(success: Boolean, event: Option[Event], errorMsg: Option[String])

class Dialogs:

  def createNewEventDialog: Dialog[NewEventDialogResult] = 

    val dialog = new Dialog[NewEventDialogResult](){
      //initOwner(stage)
      title = "Create Event"
      headerText = "Create Event Here"
    }

    val createEventButtonType = new ButtonType("Create Event", ButtonData.OKDone)
    dialog.dialogPane().buttonTypes = Seq(createEventButtonType, ButtonType.Cancel)
    val createEventButton = dialog.dialogPane().lookupButton(createEventButtonType)

    createEventButton.disable = true

    val nameField = new TextField
    nameField.text.onChange { (_, _, newValue) =>
      createEventButton.disable = newValue.trim().isEmpty
    }

    val dp1 = new DatePicker(LocalDate.now())

    val dp2 = new DatePicker(LocalDate.now())

    val hchoices = ObservableBuffer("00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","22","23","24")

    val m1choices = ObservableBuffer("0","1","2","3","4","5")
    val m2choices = ObservableBuffer("0","1","2","3","4","5","6","7","8","9")

    val shours = new ChoiceBox(hchoices){
      value = "00"
    }
    val smins1 = new ChoiceBox(m1choices){
      value = "0"
    }
    val smins2 = new ChoiceBox(m2choices){
      value = "0"
    }

    val startTime = new HBox{
      children = Seq(
        shours,
        new Label(" : "),
        smins1,
        smins2
      )
    }

    val ehours = new ChoiceBox(hchoices){
      value = "00"
    }
    val emins1 = new ChoiceBox(m1choices){
      value = "0"
    }
    val emins2 = new ChoiceBox(m2choices){
      value = "0"
    }

    val endTime = new HBox{
      children = Seq(
        ehours,
        new Label(" : "),
        emins1,
        emins2
      )
    }

    val startEndCheck = new BooleanProperty

    val grid = new GridPane() {
      hgap = 10
      vgap = 10
      padding = Insets(20, 100, 10, 10)

      add(new Label("Name:"), 0, 0)
      add(nameField, 1, 0)
      add(new Label("Start:"), 0, 1)
      add(dp1, 1, 1)
      add(startTime, 3, 1)
      add(new Label("End:"), 0, 2)
      add(dp2, 1, 2)
      add(endTime, 3, 2)
    }

    dialog.resultConverter = dialogButton =>
      if (dialogButton == createEventButtonType)

        val s = dp1.value().atStartOfDay().plusHours(shours.value().toInt).plusMinutes(smins1.value().toInt*10).plusMinutes(smins2.value().toInt)

        val e = dp2.value().atStartOfDay().plusHours(ehours.value().toInt).plusMinutes(emins1.value().toInt*10).plusMinutes(emins2.value().toInt)

        if s.isBefore(e) then
          NewEventDialogResult(true, Some(Event(nameField.text(), s, e, None, None)), None) 
        else
          NewEventDialogResult(false, None, Some("Event end was before start"))
      else
        null

    dialog.dialogPane().content = grid

    dialog
