package guiRole1DeleteLesson;

import java.util.Optional;

import database.Database;
import javafx.scene.control.ButtonType;

/*******
 * <p> Title: ControllerRole1DeleteLesson Class. </p>
 *
 * <p> Description: The controller actions for the Role1 Delete Lesson page, performed in response
 * to the user's use of the JavaFX GUI widgets defined by the View class. </p>
 *
 * @author Edis Jakupovic
 */

public class ControllerRole1DeleteLesson {

	/**
	 * Default constructor is not used.
	 */
	public ControllerRole1DeleteLesson() {}

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**********
	 * <p> Method: doSelectLesson() </p>
	 *
	 * <p> Description: This method fetches which title was selected in the ComboBox and records
	 * it, so the rest of the page does not have to ask the widget again. </p>
	 *
	 */
	protected static void doSelectLesson() {
		ViewRole1DeleteLesson.theSelectedLesson =
				ViewRole1DeleteLesson.combobox_SelectLesson.getValue();
	}

	/**********
	 * <p> Method: repaintTheWindow() </p>
	 *
	 * <p> Description: This method determines the current state of the window and then establishes
	 * the appropriate list of widgets in the Pane to show the proper set of current values. </p>
	 *
	 */
	protected static void repaintTheWindow() {
		ViewRole1DeleteLesson.theRootPane.getChildren().clear();

		if (ViewRole1DeleteLesson.theSelectedLesson == null ||
				ViewRole1DeleteLesson.theSelectedLesson.compareTo(
						ViewRole1DeleteLesson.SELECT_PROMPT) == 0) {
			// Only show the request to select a lesson and the ComboBox, as there is nothing a
			// Delete button could act on yet
			ViewRole1DeleteLesson.theRootPane.getChildren().addAll(
				ViewRole1DeleteLesson.label_PageTitle, ViewRole1DeleteLesson.label_UserDetails,
				ViewRole1DeleteLesson.line_Separator1,
				ViewRole1DeleteLesson.label_SelectLesson,
				ViewRole1DeleteLesson.combobox_SelectLesson,
				ViewRole1DeleteLesson.line_Separator4, ViewRole1DeleteLesson.button_Return,
				ViewRole1DeleteLesson.button_Logout, ViewRole1DeleteLesson.button_Quit
			);
		}
		else {
			// Show all the fields as there is a selected lesson (as opposed to the prompt)
			ViewRole1DeleteLesson.theRootPane.getChildren().addAll(
				ViewRole1DeleteLesson.label_PageTitle, ViewRole1DeleteLesson.label_UserDetails,
				ViewRole1DeleteLesson.line_Separator1,
				ViewRole1DeleteLesson.label_SelectLesson,
				ViewRole1DeleteLesson.combobox_SelectLesson,
				ViewRole1DeleteLesson.line_Separator4,
				ViewRole1DeleteLesson.button_DeleteLesson,
				ViewRole1DeleteLesson.button_Return,
				ViewRole1DeleteLesson.button_Logout,
				ViewRole1DeleteLesson.button_Quit
			);
		}

		ViewRole1DeleteLesson.theStage.setTitle("CSE 360 Foundations: Delete a Lesson Learned");
		ViewRole1DeleteLesson.theStage.setScene(ViewRole1DeleteLesson.theDeleteLessonScene);
		ViewRole1DeleteLesson.theStage.show();
	}

	/**********
	 * <p> Method: performChange() </p>
	 *
	 * <p> Description: This method opens a confirmation dialog box.  If yes is selected, the
	 * selected lesson is removed from the database.  The contributor is passed along with the
	 * title so the delete cannot reach a lesson belonging to anyone else. </p>
	 *
	 */
	protected static void performChange() {
		// NOTE: Optional<ButtonType> is needed to handle cases where dialog is closed abruptly
		Optional<ButtonType> result = ViewRole1DeleteLesson.alert_DeleteLesson.showAndWait();

		String stdoutMessage = result.isPresent() ? result.get().getText() : "No result present";
		System.out.println("*** ControllerRole1DeleteLesson.performChange(): " + stdoutMessage);

		if (result.isPresent() && result.get() == ButtonType.YES) {
			String selectedLesson = ViewRole1DeleteLesson.theSelectedLesson;
			boolean removed = theDatabase.deleteLesson(
					ViewRole1DeleteLesson.theUser.getUserName(), selectedLesson);
			System.out.println("*** ControllerRole1DeleteLesson.performChange(): deleted lesson: "
					+ selectedLesson + " (" + removed + ")");
			// NOTE: need to refresh combobox entries after deleting a lesson
			ViewRole1DeleteLesson.populateComboBox();
			doSelectLesson();
			repaintTheWindow();
		}
	}

	/**********
	 * <p> Method: performReturn() </p>
	 *
	 * <p> Description: This method returns the user to the Role1 home page. </p>
	 *
	 */
	protected static void performReturn() {
		guiRole1.ViewRole1Home.displayRole1Home(ViewRole1DeleteLesson.theStage,
				ViewRole1DeleteLesson.theUser);
	}

	/**********
	 * <p> Method: performLogout() </p>
	 *
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page. </p>
	 *
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewRole1DeleteLesson.theStage);
	}

	/**********
	 * <p> Method: performQuit() </p>
	 *
	 * <p> Description: This method terminates the execution of the program. </p>
	 *
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}
