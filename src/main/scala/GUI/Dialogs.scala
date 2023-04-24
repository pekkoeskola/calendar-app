package CalendarApp
package GUI

import scalafx.Includes._
import scalafx.stage
import scalafx.scene.control.Dialog
import scalafx.scene.control.TextField
import scalafx.scene.control.ButtonType
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.VBox


case class NewEventDialogResult(Name: String)

class Dialogs:

  def createNewEventDialog: Dialog[NewEventDialogResult] = 

    val dialog = new Dialog[NewEventDialogResult](){
      //initOwner(stage)
      title = "Create Event"
      headerText = "Create Event Here"
    }

    val createEventButtonType = new ButtonType("Create Event", ButtonData.OKDone)

    dialog.dialogPane().buttonTypes = Seq(createEventButtonType,ButtonType.Cancel)

    val createEventButton = dialog.dialogPane().lookupButton(createEventButtonType)

    createEventButton.disable = true

    val nameField = new TextField

    val grid = new VBox{
      children = nameField
    }

    dialog.dialogPane().content = grid


    dialog
