package guiRole1AddLesson;

import database.Database;
import entityClasses.LessonsLearned;

/*******
 * <p> Title: ControllerRole1AddLesson Class. </p>
 *
 * <p> Description: The controller actions for the Role1 Add Lesson page, performed in response to
 * the user's use of the JavaFX GUI widgets defined by the View class. </p>
 *
 * @author Edis Jakupovic
 */

public class ControllerRole1AddLesson {

	/*-*******************************************************************************************

	User Interface Actions for this page

	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and
	the Model is often just a stub, or will be a singleton instantiated object.

	 */

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**
	 * Default constructor is not used.
	 */
	public ControllerRole1AddLesson() {
	}

	/**********
	 * <p> Method: performSaveLesson() </p>
	 *
	 * <p> Description: This method validates what the Contributor entered and, when it is
	 * acceptable, adds the lesson to the database.  The two lengths are already enforced as the
	 * text is typed by the listeners the View attached, so what is left to check here is that
	 * neither field was left empty.  The contributor is taken from the logged-in user rather than
	 * from a field, so a lesson cannot be attributed to anyone else, and the experience is empty
	 * because it is gathered only after an attempt to leverage the lesson has occurred. </p>
	 *
	 */
	protected static void performSaveLesson() {
		String theTitle = ViewRole1AddLesson.text_Title.getText().trim();
		String theCoreInformation = ViewRole1AddLesson.text_CoreInformation.getText().trim();

		// The text is trimmed before it is measured so a field holding nothing but spaces is
		// treated as the empty field the Contributor actually left behind.
		if (theTitle.length() == 0) {
			showFailure("Please enter a title for this lesson.  The title is how the lesson is "
					+ "found in a list, so it cannot be left empty.");
			return;
		}

		if (theCoreInformation.length() == 0) {
			showFailure("Please enter the core information for this lesson.  A lesson with a "
					+ "title and nothing else tells a future team nothing.");
			return;
		}

		LessonsLearned theLesson = new LessonsLearned(ViewRole1AddLesson.theUser.getUserName(),
				theCoreInformation, "", theTitle);

		// A false return means the database refused the row.  The reason that refusal is expected
		// is the pair of a contributor and a title being unique, so that is what the message
		// explains rather than reporting a database failure the Contributor can do nothing about.
		if (theDatabase.addLesson(theLesson)) {
			ViewRole1AddLesson.alert_SaveSucceeded.setContentText("\"" + theTitle
					+ "\" has been added to your lessons learned.");
			ViewRole1AddLesson.alert_SaveSucceeded.showAndWait();
			ViewRole1AddLesson.text_Title.clear();
			ViewRole1AddLesson.text_CoreInformation.clear();
		} else {
			showFailure("You already have a lesson titled \"" + theTitle + "\".  Please give this "
					+ "lesson a different title.");
		}
	}

	/**********
	 * <p> Method: showFailure(String message) </p>
	 *
	 * <p> Description: This method reports why the lesson was not added, leaving what was typed
	 * in place so the Contributor can correct it rather than enter it again. </p>
	 *
	 * @param message explains what is wrong and what to do about it
	 *
	 */
	private static void showFailure(String message) {
		ViewRole1AddLesson.alert_SaveFailed.setContentText(message);
		ViewRole1AddLesson.alert_SaveFailed.showAndWait();
	}

	/**********
	 * <p> Method: performReturn() </p>
	 *
	 * <p> Description: This method returns the user to the Role1 home page without saving what
	 * was entered. </p>
	 *
	 */
	protected static void performReturn() {
		guiRole1.ViewRole1Home.displayRole1Home(ViewRole1AddLesson.theStage,
				ViewRole1AddLesson.theUser);
	}

	/**********
	 * <p> Method: performLogout() </p>
	 *
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page. </p>
	 *
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewRole1AddLesson.theStage);
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
