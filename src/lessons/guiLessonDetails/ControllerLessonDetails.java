package lessons.guiLessonDetails;

import java.sql.SQLException;
import java.util.Optional;

import entityClasses.LessonRecord;
import javafx.scene.control.ButtonType;
import lessons.LessonsEvaluator;
import lessons.guiContributorLessons.ViewContributorLessons;
import lessons.guiLessonExperience.ViewLessonExperience;

/*******
 * <p> Title: ControllerLessonDetails Class. </p>
 *
 * <p> Description: The Java/FX-based Lesson Details page.  This class provides the controller
 * actions based on the user's use of the JavaFX GUI widgets defined by the View class, and it
 * fills the View's form with the lesson the Model loads from the database.</p>
 *
 * <p> The class has been written assuming that the View or the Model are the only class methods
 * that can invoke these methods.  This is why each has been declared at "protected".  Do not
 * change any of these methods to public.</p>
 *
 * @author Shane McPhillips
 *
 * @version 1.00	2026-10-07 Initial version for HW2
 */

public class ControllerLessonDetails {

	/*-*******************************************************************************************

	User Interface Actions for this page

	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object).

	 */

	/**
	 * Default constructor is not used.
	 */
	public ControllerLessonDetails() {
	}

	/**********
	 * <p> Method: repaintTheWindow() </p>
	 *
	 * <p> Description: Loads the lesson from the database and fills the form with it (Story 3).
	 * Each locked field is disabled and marked "(Locked)", and Delete is disabled when the lesson
	 * cannot be deleted, so the contributor can see what may be managed before trying.</p>
	 *
	 * <p> If the lesson cannot be shown (it is gone, it belongs to someone else, or the database
	 * cannot be read), the form is emptied and disabled and the reason is shown instead.</p>
	 */
	protected static void repaintTheWindow() {
		ViewLessonDetails.label_UserDetails.setText("User: "
				+ ViewLessonDetails.theUser.getUserName());

		LessonRecord lesson = null;
		String problem;
		try {
			lesson = ModelLessonDetails.loadLesson(ViewLessonDetails.theLessonId);
			problem = LessonsEvaluator.evaluateCanView(lesson,
					ViewLessonDetails.theUser.getUserName());
		} catch (SQLException e) {
			e.printStackTrace();
			problem = ModelLessonDetails.DATABASE_ERROR;
		}

		if (problem.isEmpty()) {
			fillForm(lesson);
			ViewLessonDetails.label_Message.setText("");
		}
		else {
			clearForm();
			ViewLessonDetails.showError(problem);
		}

		// Set the title for the window, display the page, and wait for the user to do something
		ViewLessonDetails.theStage.setTitle("CSE 360 Foundations: Lesson Details");
		ViewLessonDetails.theStage.setScene(ViewLessonDetails.theLessonDetailsScene);
		ViewLessonDetails.theStage.show();
	}

	/**********
	 * <p> Method: performSave() </p>
	 *
	 * <p> Description: Called when the contributor presses "Save Changes" (Story 4). On success
	 * the form is reloaded from the database, so it shows exactly what was stored. On failure
	 * the form is left as typed and every error is shown.</p>
	 */
	protected static void performSave() {
		String result = ModelLessonDetails.updateLesson(
				ViewLessonDetails.theLessonId,
				ViewLessonDetails.theUser.getUserName(),
				ViewLessonDetails.text_Title.getText(),
				ViewLessonDetails.text_Description.getText(),
				ViewLessonDetails.combobox_Category.getValue(),
				ViewLessonDetails.text_Effort.getText());

		if (result.isEmpty()) {
			repaintTheWindow();
			ViewLessonDetails.showSuccess("Your changes were saved.");
		}
		else {
			ViewLessonDetails.showError(result);
		}
	}

	/**********
	 * <p> Method: performDelete() </p>
	 *
	 * <p> Description: Deletes this lesson (Story 5). The checks run first, so the contributor
	 * is only asked to confirm a delete that is allowed. Choosing "Yes" deletes the lesson and
	 * returns to the list; choosing "No", or closing the dialog, cancels the delete.</p>
	 */
	protected static void performDelete() {
		String username = ViewLessonDetails.theUser.getUserName();
		String problem = ModelLessonDetails.checkCanDelete(ViewLessonDetails.theLessonId, username);
		if (!problem.isEmpty()) {
			ViewLessonDetails.showError(problem);
			return;
		}

		// NOTE: Optional<ButtonType> is needed to handle cases where dialog is closed abruptly
		Optional<ButtonType> result = ViewLessonDetails.alert_DeleteLesson.showAndWait();
		if (result.isEmpty() || result.get() != ButtonType.YES) return;

		String deleteResult = ModelLessonDetails.deleteLesson(ViewLessonDetails.theLessonId,
				username);
		if (deleteResult.isEmpty())
			ViewContributorLessons.displayContributorLessons(ViewLessonDetails.theStage,
					ViewLessonDetails.theUser);
		else
			ViewLessonDetails.showError(deleteResult);
	}

	/**********
	 * <p> Method: performAddExperience() </p>
	 *
	 * <p> Description: Takes the contributor to the Experience page for this lesson (Story 6).
	 * </p>
	 */
	protected static void performAddExperience() {
		String problem = ModelLessonDetails.checkCanAddExperience(ViewLessonDetails.theLessonId,
				ViewLessonDetails.theUser.getUserName());
		if (!problem.isEmpty()) {
			ViewLessonDetails.showError(problem);
			return;
		}
		ViewLessonExperience.displayLessonExperience(ViewLessonDetails.theStage,
				ViewLessonDetails.theUser, ViewLessonDetails.theLessonId);
	}

	/**********
	 * <p> Method: performUpdate() </p>
	 *
	 * <p> Description: This method directs the user to the User Update Page so the user can change
	 * the user account attributes. </p>
	 */
	protected static void performUpdate () {
		guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewLessonDetails.theStage,
				ViewLessonDetails.theUser);
	}

	/**********
	 * <p> Method: performReturn() </p>
	 *
	 * <p> Description: This method returns the user to the My Lessons page, which reloads the
	 * list so any change made here is shown. </p>
	 */
	protected static void performReturn() {
		ViewContributorLessons.displayContributorLessons(ViewLessonDetails.theStage,
				ViewLessonDetails.theUser);
	}

	/**********
	 * <p> Method: performLogout() </p>
	 *
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page where existing users can log in or potential new users with a invitation code can
	 * start the process of setting up an account. </p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewLessonDetails.theStage);
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

	// Private helper: show a lesson in the form, disabling each locked field and the buttons
	// that may not be used
	private static void fillForm(LessonRecord lesson) {
		ViewLessonDetails.text_Title.setText(lesson.getTitle());
		ViewLessonDetails.text_Description.setText(lesson.getDescription());
		ViewLessonDetails.combobox_Category.setValue(lesson.getCategory());
		ViewLessonDetails.text_Effort.setText(String.valueOf(lesson.getDurationHours()));

		ViewLessonDetails.label_Created.setText("Created: " + (lesson.getCreatedAt() == null ? ""
				: lesson.getCreatedAt().format(ViewLessonDetails.DATE_FORMAT)));

		ViewLessonDetails.showLock(ViewLessonDetails.label_Title, ViewLessonDetails.text_Title,
				ViewLessonDetails.TITLE_LABEL, lesson.isTitleLocked());
		ViewLessonDetails.showLock(ViewLessonDetails.label_Description,
				ViewLessonDetails.text_Description, ViewLessonDetails.DESCRIPTION_LABEL,
				lesson.isDescriptionLocked());
		ViewLessonDetails.showLock(ViewLessonDetails.label_Category,
				ViewLessonDetails.combobox_Category, ViewLessonDetails.CATEGORY_LABEL,
				lesson.isCategoryLocked());
		ViewLessonDetails.showLock(ViewLessonDetails.label_Effort, ViewLessonDetails.text_Effort,
				ViewLessonDetails.EFFORT_LABEL, lesson.isEffortLocked());

		// Story 3: a lesson with a locked field shows Delete as a grayed-out button. Story 5
		// also blocks deleting a lesson someone else has changed, so that disables it too.
		boolean canDelete = !lesson.isLocked() && !lesson.isAlteredByAnother();
		ViewLessonDetails.button_Save.setDisable(false);
		ViewLessonDetails.button_Delete.setDisable(!canDelete);
		ViewLessonDetails.button_AddExperience.setDisable(false);

		if (!canDelete)
			ViewLessonDetails.label_LockNote.setText("Fields marked (Locked) cannot be edited, and "
					+ "this lesson cannot be deleted.");
		else
			ViewLessonDetails.label_LockNote.setText("");
	}

	// Private helper: empty and disable the form when there is no lesson that may be shown
	private static void clearForm() {
		ViewLessonDetails.text_Title.clear();
		ViewLessonDetails.text_Description.clear();
		ViewLessonDetails.combobox_Category.setValue(null);
		ViewLessonDetails.text_Effort.clear();
		ViewLessonDetails.label_Created.setText("");
		ViewLessonDetails.label_LockNote.setText("");

		// Disabled but not marked "(Locked)": there is no lesson, so nothing is locked
		ViewLessonDetails.showLock(ViewLessonDetails.label_Title, ViewLessonDetails.text_Title,
				ViewLessonDetails.TITLE_LABEL, false);
		ViewLessonDetails.showLock(ViewLessonDetails.label_Description,
				ViewLessonDetails.text_Description, ViewLessonDetails.DESCRIPTION_LABEL, false);
		ViewLessonDetails.showLock(ViewLessonDetails.label_Category,
				ViewLessonDetails.combobox_Category, ViewLessonDetails.CATEGORY_LABEL, false);
		ViewLessonDetails.showLock(ViewLessonDetails.label_Effort, ViewLessonDetails.text_Effort,
				ViewLessonDetails.EFFORT_LABEL, false);
		ViewLessonDetails.text_Title.setDisable(true);
		ViewLessonDetails.text_Description.setDisable(true);
		ViewLessonDetails.combobox_Category.setDisable(true);
		ViewLessonDetails.text_Effort.setDisable(true);
		ViewLessonDetails.button_Save.setDisable(true);
		ViewLessonDetails.button_Delete.setDisable(true);
		ViewLessonDetails.button_AddExperience.setDisable(true);
	}
}
