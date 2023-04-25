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

case class ModifyorDeleteEventDialogResult(success: Boolean, isDelete: Boolean, event: Option[Event], originalEvent: Option[Event], errorMsg: Option[String])

class GUIDialogs(runningInstance: CalendarApp):

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
    val locationField = new TextField
    val participantsField = new TextField

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

    val calendarNames = runningInstance.calendars.map(_.name).toVector
    val calendarChoices = ObservableBuffer[String]()
    calendarChoices.addAll(calendarNames)
    val calendarChoiceBox = new ChoiceBox(calendarChoices)
    calendarChoiceBox.value() = calendarNames(0)

    val grid = new GridPane() {
      hgap = 10
      vgap = 10
      padding = Insets(20, 100, 10, 10)

      add(new Label("Calendar:"), 0, 0)
      add(calendarChoiceBox, 1, 0)

      add(new Label("Name:"), 0, 1)
      add(nameField, 1, 1)

      add(new Label("Start:"), 0, 2)
      add(dp1, 1, 2)
      add(startTime, 2, 2)

      add(new Label("End:"), 0, 3)
      add(dp2, 1, 3)
      add(endTime, 2, 3)

      add(new Label("Location:"), 0, 4)
      add(locationField, 1, 4)
    }

    dialog.resultConverter = dialogButton =>
      if (dialogButton == createEventButtonType)

        val s = dp1.value().atStartOfDay().plusHours(shours.value().toInt).plusMinutes(smins1.value().toInt*10).plusMinutes(smins2.value().toInt)

        val e = dp2.value().atStartOfDay().plusHours(ehours.value().toInt).plusMinutes(emins1.value().toInt*10).plusMinutes(emins2.value().toInt)

        //gathers all the info to craft a new event object
        def buildEvent: Event = 

          val name = nameField.text()
          val cal: Calendar = runningInstance.findCalendar(calendarChoiceBox.value())
          val start = s
          val end = e
          val location = Option(locationField.text()).filter(_.trim.nonEmpty)

          Event(name, cal, start, end, location, None, None, None)
        
        end buildEvent

        if s.isBefore(e) then
          //finish this!!
          NewEventDialogResult(true, Some(buildEvent), None) 
        else
          NewEventDialogResult(false, None, Some("Event end was before start"))
      else
        null

    dialog.dialogPane().content = grid

    dialog

  end createNewEventDialog

  def modifyorDeleteEventDialog(eventToModify: Event): Dialog[ModifyorDeleteEventDialogResult] = 

    val dialog = new Dialog[ModifyorDeleteEventDialogResult](){
      //initOwner(stage)
      title = "Modify Event"
      headerText = "Modifying event"
    }

    val saveEventButtonType = new ButtonType("Save", ButtonData.OKDone)
    val deleteEventButtonType = new ButtonType("Delete Event", ButtonData.Other)

    dialog.dialogPane().buttonTypes = Seq(saveEventButtonType, deleteEventButtonType, ButtonType.Cancel)
    val createEventButton = dialog.dialogPane().lookupButton(saveEventButtonType)

    createEventButton.disable = false
    val nameField = new TextField{
      text = eventToModify.name
    }
    nameField.text.onChange { (_, _, newValue) =>
      createEventButton.disable = newValue.trim().isEmpty
    }
    val locationField = new TextField{
      text = eventToModify.location.getOrElse("")
    }
    val participantsField = new TextField{
      text = eventToModify.location.getOrElse("")
    }

    val dp1 = new DatePicker(eventToModify.startTime.toLocalDate())

    val dp2 = new DatePicker(eventToModify.endTime.toLocalDate())

    val hchoices = ObservableBuffer("00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","22","23","24")

    val m1choices = ObservableBuffer("0","1","2","3","4","5")
    val m2choices = ObservableBuffer("0","1","2","3","4","5","6","7","8","9")

    val smins = eventToModify.startTime.getMinute()
    val emins = eventToModify.endTime.getMinute()

    val shours = new ChoiceBox(hchoices){
      value = if eventToModify.startTime.getHour() < 10 then "0" + eventToModify.startTime.getHour().toString() else eventToModify.startTime.getHour().toString()
    }
    val smins1 = new ChoiceBox(m1choices){
      value = if smins < 10 then 0.toString() else smins.toString()(0).toString()
    }
    val smins2 = new ChoiceBox(m2choices){
      value = if smins < 10 then smins.toString()(0).toString() else smins.toString()(1).toString()
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
      value = eventToModify.endTime.getHour().toString()
    }
    val emins1 = new ChoiceBox(m1choices){
      value = if emins < 10 then 0.toString() else smins.toString()(0).toString()
    }
    val emins2 = new ChoiceBox(m2choices){
      value = if emins < 10 then smins.toString()(0).toString() else smins.toString()(1).toString()
    }

    val endTime = new HBox{
      children = Seq(
        ehours,
        new Label(" : "),
        emins1,
        emins2
      )
    }

    val calendarNames = runningInstance.calendars.map(_.name).toVector
    val calendarChoices = ObservableBuffer[String]()
    calendarChoices.addAll(calendarNames)
    val calendarChoiceBox = new ChoiceBox(calendarChoices)
    calendarChoiceBox.value() = calendarNames(0)

    val grid = new GridPane() {
      hgap = 10
      vgap = 10
      padding = Insets(20, 100, 10, 10)

      add(new Label("Calendar:"), 0, 0)
      add(calendarChoiceBox, 1, 0)

      add(new Label("Name:"), 0, 1)
      add(nameField, 1, 1)

      add(new Label("Start:"), 0, 2)
      add(dp1, 1, 2)
      add(startTime, 2, 2)

      add(new Label("End:"), 0, 3)
      add(dp2, 1, 3)
      add(endTime, 2, 3)

      add(new Label("Location:"), 0, 4)
      add(locationField, 1, 4)
    }

    dialog.resultConverter = dialogButton =>
      if (dialogButton == saveEventButtonType) then

        val s = dp1.value().atStartOfDay().plusHours(shours.value().toInt).plusMinutes(smins1.value().toInt*10).plusMinutes(smins2.value().toInt)

        val e = dp2.value().atStartOfDay().plusHours(ehours.value().toInt).plusMinutes(emins1.value().toInt*10).plusMinutes(emins2.value().toInt)

        //gathers all the info to craft a new event object
        def buildEvent: Event = 

          val name = nameField.text()
          val cal: Calendar = runningInstance.findCalendar(calendarChoiceBox.value())
          val start = s
          val end = e
          val location = Option(locationField.text()).filter(_.trim.nonEmpty)

          Event(name, cal, start, end, location, None, None, None)
        
        end buildEvent

        if s.isBefore(e) then
          //finish this!!
          ModifyorDeleteEventDialogResult(true, false, Some(buildEvent), Some(eventToModify),None) 
        else
          ModifyorDeleteEventDialogResult(false, false, None, None, Some("Event end was before start"))
      else if (dialogButton == deleteEventButtonType) then
        ModifyorDeleteEventDialogResult(true, true, None, Some(eventToModify), None)
      else
        null

    dialog.dialogPane().content = grid

    dialog

  end modifyorDeleteEventDialog

