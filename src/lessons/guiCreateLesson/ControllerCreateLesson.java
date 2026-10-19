package lessons.guiCreateLesson;

import lessons.guiContributorLessons.ViewContributorLessons;

/*******
 * <p> Title: ControllerCreateLesson Class. </p>
 *
 * <p> Description: The Java/FX-based Create Lesson page.  This class provides the controller
 * actions based on the user's use of the JavaFX GUI widgets defined by the View class.</p>
 *
 * <p> The Controller is the only class that talks to both the View and the Model: it reads the
 * values from the View's widgets, hands them to the Model, and shows the Model's answer in the
 * View. The View never calls the Model and the Model never touches a widget, so each of the
 * three classes has exactly one job.</p>
 *
 * <p> The class has been written assuming that the View or the Model are the only class methods
 * that can invoke these methods.  This is why each has been declared at "protected".  Do not
 * change any of these methods to public.</p>
 *
 * @author Shane McPhillips
 *
 * @version 1.00	2026-10-07 Initial version for HW2
 */

public class ControllerCreateLesson {

	/*-*******************************************************************************************

	User Interface Actions for this page

	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object).

	 */

	/**
	 * Default constructor is not used.
	 */
	public ControllerCreateLesson() {
	}

	/**********
	 * <p> Method: performSave() </p>
	 *
	 * <p> Description: Called when the contributor presses "Save". The Model validates the input
	 * and, only if every field is valid, stores the lesson in the database (Story 1).</p>
	 *
	 * <p> On success the form is cleared and a confirmation is shown, so the contributor can add
	 * another lesson straight away. On failure the form is left exactly as typed and every error
	 * is shown, so the contributor only needs to fix what is wrong.</p>
	 */
	protected static void performSave() {
		String result = ModelCreateLesson.createLesson(
				ViewCreateLesson.text_Title.getText(),
				ViewCreateLesson.text_Description.getText(),
				ViewCreateLesson.combobox_Category.getValue(),
				ViewCreateLesson.text_Effort.getText(),
				ViewCreateLesson.theUser.getUserName());

		if (result.isEmpty()) {
			String savedTitle = ViewCreateLesson.text_Title.getText().trim();
			clearForm();
			ViewCreateLesson.showSuccess("Lesson \"" + savedTitle + "\" was saved.");
		}
		else {
			ViewCreateLesson.showError(result);
		}
	}

	/**********
	 * <p> Method: performClear() </p>
	 *
	 * <p> Description: Called when the contributor presses "Clear", and each time the page is
	 * displayed. Empties every field and removes any message.</p>
	 */
	protected static void performClear() {
		clearForm();
		ViewCreateLesson.label_Message.setText("");
	}

	/**********
	 * <p> Method: performViewLessons() </p>
	 *
	 * <p> Description: Takes the contributor to the list of their lessons (Story 2), so a lesson
	 * that was just saved can be seen in the list right away.</p>
	 */
	protected static void performViewLessons() {
		ViewContributorLessons.displayContributorLessons(
				ViewCreateLesson.theStage, ViewCreateLesson.theUser);
	}

	/**********
	 * <p> Method: performUpdate() </p>
	 *
	 * <p> Description: This method directs the user to the User Update Page so the user can change
	 * the user account attributes. </p>
	 */
	protected static void performUpdate () {
		guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewCreateLesson.theStage,
				ViewCreateLesson.theUser);
	}

	/**********
	 * <p> Method: performReturn() </p>
	 *
	 * <p> Description: This method returns the user to the Contributor Home page. </p>
	 */
	protected static void performReturn() {
		guiContributor.ViewContributorHome.displayContributorHome(ViewCreateLesson.theStage,
				ViewCreateLesson.theUser);
	}

	/**********
	 * <p> Method: performLogout() </p>
	 *
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page where existing users can log in or potential new users with a invitation code can
	 * start the process of setting up an account. </p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewCreateLesson.theStage);
	}

	/**********
	 * <p> Method: performQuit() </p>
	 *
	 * <p> Description: This method terminates the execution of the program.  It leaves the
	 * database in a state where the normal login page will be displayed when the application is
	 * restarted.</p>
	 */
	protected static void performQuit() {
		System.exit(0);
	}

	// Private helper: empty every input field. Shared by Save (after success) and Clear.
	private static void clearForm() {
		ViewCreateLesson.text_Title.clear();
		ViewCreateLesson.text_Description.clear();
		ViewCreateLesson.combobox_Category.getSelectionModel().clearSelection();
		ViewCreateLesson.combobox_Category.setValue(null);
		ViewCreateLesson.text_Effort.clear();
	}
}
