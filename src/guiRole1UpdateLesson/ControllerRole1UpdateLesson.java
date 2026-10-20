package guiRole1UpdateLesson;

import database.Database;
import entityClasses.LessonsLearned;

/*******
 * <p> Title: ControllerRole1UpdateLesson Class. </p>
 *
 * <p> Description: The controller actions for the Role1 Update Lesson page, performed in response
 * to the user's use of the JavaFX GUI widgets defined by the View class. </p>
 *
 * @author Edis Jakupovic
 */

public class ControllerRole1UpdateLesson {

	/*-*******************************************************************************************

	User Interface Actions for this page

	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and
	the Model is often just a stub, or will be a singleton instantiated object.

	 */

	/**
	 * Default constructor is not used.
	 */
	public ControllerRole1UpdateLesson() {}

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**********
	 * <p> Method: doSelectLesson() </p>
	 *
	 * <p> Description: This method fetches which title was selected in the ComboBox and reads
	 * that lesson from the database into the editing widgets, so what is shown is what is stored
	 * rather than whatever the widgets held from a previous selection. </p>
	 *
	 */
	protected static void doSelectLesson() {
		ViewRole1UpdateLesson.theSelectedLesson =
				ViewRole1UpdateLesson.combobox_SelectLesson.getValue();

		if (ViewRole1UpdateLesson.theSelectedLesson == null ||
				ViewRole1UpdateLesson.theSelectedLesson.compareTo(
						ViewRole1UpdateLesson.SELECT_PROMPT) == 0) {
			ViewRole1UpdateLesson.label_TitleValue.setText("");
			ViewRole1UpdateLesson.text_CoreInformation.clear();
			ViewRole1UpdateLesson.text_Experience.clear();
			return;
		}

		LessonsLearned theLesson = theDatabase.getLesson(
				ViewRole1UpdateLesson.theUser.getUserName(),
				ViewRole1UpdateLesson.theSelectedLesson);

		// A null lesson means the title in the ComboBox no longer names a stored lesson, which
		// happens if it was removed elsewhere.  The page returns to the prompt rather than
		// offering empty fields that would write a lesson that does not exist.
		if (theLesson == null) {
			showFailure("That lesson could no longer be found.  Please choose another.");
			ViewRole1UpdateLesson.populateComboBox();
			ViewRole1UpdateLesson.theSelectedLesson = ViewRole1UpdateLesson.SELECT_PROMPT;
			return;
		}

		ViewRole1UpdateLesson.label_TitleValue.setText(theLesson.getTitle());
		ViewRole1UpdateLesson.text_CoreInformation.setText(theLesson.getCoreInformation());
		ViewRole1UpdateLesson.text_Experience.setText(theLesson.getExperience());
	}

	/**********
	 * <p> Method: repaintTheWindow() </p>
	 *
	 * <p> Description: This method determines the current state of the window and then establishes
	 * the appropriate list of widgets in the Pane to show the proper set of current values. </p>
	 *
	 */
	protected static void repaintTheWindow() {
		ViewRole1UpdateLesson.theRootPane.getChildren().clear();

		if (ViewRole1UpdateLesson.theSelectedLesson == null ||
				ViewRole1UpdateLesson.theSelectedLesson.compareTo(
						ViewRole1UpdateLesson.SELECT_PROMPT) == 0) {
			// Only show the request to select a lesson and the ComboBox, as there is nothing for
			// the editing widgets to show yet
			ViewRole1UpdateLesson.theRootPane.getChildren().addAll(
				ViewRole1UpdateLesson.label_PageTitle, ViewRole1UpdateLesson.label_UserDetails,
				ViewRole1UpdateLesson.line_Separator1,
				ViewRole1UpdateLesson.label_SelectLesson,
				ViewRole1UpdateLesson.combobox_SelectLesson,
				ViewRole1UpdateLesson.line_Separator4, ViewRole1UpdateLesson.button_Return,
				ViewRole1UpdateLesson.button_Logout, ViewRole1UpdateLesson.button_Quit
			);
		}
		else {
			// Show all the fields as there is a selected lesson (as opposed to the prompt)
			ViewRole1UpdateLesson.theRootPane.getChildren().addAll(
				ViewRole1UpdateLesson.label_PageTitle, ViewRole1UpdateLesson.label_UserDetails,
				ViewRole1UpdateLesson.line_Separator1,
				ViewRole1UpdateLesson.label_SelectLesson,
				ViewRole1UpdateLesson.combobox_SelectLesson,
				ViewRole1UpdateLesson.label_Title, ViewRole1UpdateLesson.label_TitleValue,
				ViewRole1UpdateLesson.label_CoreInformation,
				ViewRole1UpdateLesson.text_CoreInformation,
				ViewRole1UpdateLesson.label_Experience,
				ViewRole1UpdateLesson.text_Experience,
				ViewRole1UpdateLesson.button_UpdateLesson,
				ViewRole1UpdateLesson.line_Separator4,
				ViewRole1UpdateLesson.button_Return,
				ViewRole1UpdateLesson.button_Logout,
				ViewRole1UpdateLesson.button_Quit
			);
		}

		ViewRole1UpdateLesson.theStage.setTitle("CSE 360 Foundations: Update a Lesson Learned");
		ViewRole1UpdateLesson.theStage.setScene(ViewRole1UpdateLesson.theUpdateLessonScene);
		ViewRole1UpdateLesson.theStage.show();
	}

	/**********
	 * <p> Method: performUpdate() </p>
	 *
	 * <p> Description: This method validates what the Contributor entered and, when it is
	 * acceptable, replaces the stored lesson.  The two lengths are already enforced as the text
	 * is typed by the listeners the View attached, so what is left to check here is that the core
	 * information was not emptied.  The experience may be empty, as a lesson that has not been
	 * leveraged yet has no time and effort data to report.  The title is not editable on this
	 * page, so the lesson is located under the same title it keeps. </p>
	 *
	 */
	protected static void performUpdate() {
		String theTitle = ViewRole1UpdateLesson.theSelectedLesson;
		String theCoreInformation = ViewRole1UpdateLesson.text_CoreInformation.getText().trim();
		String theExperience = ViewRole1UpdateLesson.text_Experience.getText().trim();

		if (theCoreInformation.length() == 0) {
			showFailure("Please enter the core information for this lesson.  A lesson with a "
					+ "title and nothing else tells a future team nothing.");
			return;
		}

		LessonsLearned theLesson = new LessonsLearned(ViewRole1UpdateLesson.theUser.getUserName(),
				theCoreInformation, theExperience, theTitle);

		if (theDatabase.updateLesson(theTitle, theLesson)) {
			ViewRole1UpdateLesson.alert_UpdateSucceeded.setContentText("\"" + theTitle
					+ "\" has been updated.");
			ViewRole1UpdateLesson.alert_UpdateSucceeded.showAndWait();
		} else {
			showFailure("\"" + theTitle + "\" could not be updated.  It may have been removed "
					+ "since this page was opened.");
		}
	}

	/**********
	 * <p> Method: showFailure(String message) </p>
	 *
	 * <p> Description: This method reports why the lesson was not updated, leaving what was typed
	 * in place so the Contributor can correct it rather than enter it again. </p>
	 *
	 * @param message explains what is wrong and what to do about it
	 *
	 */
	private static void showFailure(String message) {
		ViewRole1UpdateLesson.alert_UpdateFailed.setContentText(message);
		ViewRole1UpdateLesson.alert_UpdateFailed.showAndWait();
	}

	/**********
	 * <p> Method: performReturn() </p>
	 *
	 * <p> Description: This method returns the user to the Role1 home page. </p>
	 *
	 */
	protected static void performReturn() {
		guiRole1.ViewRole1Home.displayRole1Home(ViewRole1UpdateLesson.theStage,
				ViewRole1UpdateLesson.theUser);
	}

	/**********
	 * <p> Method: performLogout() </p>
	 *
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page. </p>
	 *
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewRole1UpdateLesson.theStage);
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
