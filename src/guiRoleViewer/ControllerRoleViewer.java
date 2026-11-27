package guiRoleViewer;

import java.sql.SQLException;
import java.util.Optional;

import javafx.scene.control.ButtonType;
import entityClasses.LessonRecord;
import lessons.LessonsEvaluator;
import lessons.guiContributorLessons.ViewContributorLessons;
import lessons.guiLessonExperience.ViewLessonExperience;

/*******
 * <p> Title: ControllerRoleViewer Class. </p>
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

public class ControllerRoleViewer {

	/*-*******************************************************************************************

	User Interface Actions for this page

	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object).

	 */

	/**
	 * Default constructor is not used.
	 */
	public ControllerRoleViewer() {
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
		ViewRoleViewer.label_UserDetails.setText("User: "
				+ ViewRoleViewer.theUser.getUserName());

		LessonRecord lesson = null;
		String problem;
		try {
			lesson = ModelRoleViewer.loadLesson(ViewRoleViewer.theLessonId);
			problem = LessonsEvaluator.evaluateCanView(lesson,
					ViewRoleViewer.theUser.getUserName());
		} catch (SQLException e) {
			e.printStackTrace();
			problem = ModelRoleViewer.DATABASE_ERROR;
		}

		if (problem.isEmpty()) {
			fillForm(lesson);
			ViewRoleViewer.label_Message.setText("");
		}
		else {
			clearForm();
			ViewRoleViewer.showError(problem);
		}

		// Set the title for the window, display the page, and wait for the user to do something
		ViewRoleViewer.theStage.setTitle("CSE 360 Foundations: Lesson Details");
		ViewRoleViewer.theStage.setScene(ViewRoleViewer.theLessonDetailsScene);
		ViewRoleViewer.theStage.show();
	}

	/**********
	 * <p> Method: performSave() </p>
	 *
	 * <p> Description: Called when the contributor presses "Save Changes" (Story 4). On success
	 * the form is reloaded from the database, so it shows exactly what was stored. On failure
	 * the form is left as typed and every error is shown.</p>
	 */
	protected static void performSave() {
		String result = ModelRoleViewer.updateLesson(
				ViewRoleViewer.theLessonId,
				ViewRoleViewer.theUser.getUserName(),
				ViewRoleViewer.text_Title.getText(),
				ViewRoleViewer.text_Description.getText(),
				ViewRoleViewer.combobox_Category.getValue(),
				ViewRoleViewer.text_Effort.getText());

		if (result.isEmpty()) {
			repaintTheWindow();
			ViewRoleViewer.showSuccess("Your changes were saved.");
		}
		else {
			ViewRoleViewer.showError(result);
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
		String username = ViewRoleViewer.theUser.getUserName();
		String problem = ModelRoleViewer.checkCanDelete(ViewRoleViewer.theLessonId, username);
		if (!problem.isEmpty()) {
			ViewRoleViewer.showError(problem);
			return;
		}

		// NOTE: Optional<ButtonType> is needed to handle cases where dialog is closed abruptly
		Optional<ButtonType> result = ViewRoleViewer.alert_DeleteLesson.showAndWait();
		if (result.isEmpty() || result.get() != ButtonType.YES) return;

		String deleteResult = ModelRoleViewer.deleteLesson(ViewRoleViewer.theLessonId,
				username);
		if (deleteResult.isEmpty())
			ViewContributorLessons.displayContributorLessons(ViewRoleViewer.theStage,
					ViewRoleViewer.theUser);
		else
			ViewRoleViewer.showError(deleteResult);
	}

	/**********
	 * <p> Method: performAddExperience() </p>
	 *
	 * <p> Description: Takes the contributor to the Experience page for this lesson (Story 6).
	 * </p>
	 */
	protected static void performAddExperience() {
		String problem = ModelRoleViewer.checkCanAddExperience(ViewRoleViewer.theLessonId,
				ViewRoleViewer.theUser.getUserName());
		if (!problem.isEmpty()) {
			ViewRoleViewer.showError(problem);
			return;
		}
		ViewLessonExperience.displayLessonExperience(ViewRoleViewer.theStage,
				ViewRoleViewer.theUser, ViewRoleViewer.theLessonId);
	}

	/**********
	 * <p> Method: performUpdate() </p>
	 *
	 * <p> Description: This method directs the user to the User Update Page so the user can change
	 * the user account attributes. </p>
	 */
	protected static void performUpdate () {
		guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewRoleViewer.theStage,
				ViewRoleViewer.theUser);
	}

	/**********
	 * <p> Method: performReturn() </p>
	 *
	 * <p> Description: This method returns the user to the My Lessons page, which reloads the
	 * list so any change made here is shown. </p>
	 */
	protected static void performReturn() {
		ViewContributorLessons.displayContributorLessons(ViewRoleViewer.theStage,
				ViewRoleViewer.theUser);
	}

	/**********
	 * <p> Method: performLogout() </p>
	 *
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page where existing users can log in or potential new users with a invitation code can
	 * start the process of setting up an account. </p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewRoleViewer.theStage);
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
		ViewRoleViewer.text_Title.setText(lesson.getTitle());
		ViewRoleViewer.text_Description.setText(lesson.getDescription());
		ViewRoleViewer.combobox_Category.setValue(lesson.getCategory());
		ViewRoleViewer.text_Effort.setText(String.valueOf(lesson.getDurationHours()));

		ViewRoleViewer.label_Created.setText("Created: " + (lesson.getCreatedAt() == null ? ""
				: lesson.getCreatedAt().format(ViewRoleViewer.DATE_FORMAT)));

		ViewRoleViewer.showLock(ViewRoleViewer.label_Title, ViewRoleViewer.text_Title,
				ViewRoleViewer.TITLE_LABEL, lesson.isTitleLocked());
		ViewRoleViewer.showLock(ViewRoleViewer.label_Description,
				ViewRoleViewer.text_Description, ViewRoleViewer.DESCRIPTION_LABEL,
				lesson.isDescriptionLocked());
		ViewRoleViewer.showLock(ViewRoleViewer.label_Category,
				ViewRoleViewer.combobox_Category, ViewRoleViewer.CATEGORY_LABEL,
				lesson.isCategoryLocked());
		ViewRoleViewer.showLock(ViewRoleViewer.label_Effort, ViewRoleViewer.text_Effort,
				ViewRoleViewer.EFFORT_LABEL, lesson.isEffortLocked());

		// Story 3: a lesson with a locked field shows Delete as a grayed-out button. Story 5
		// also blocks deleting a lesson someone else has changed, so that disables it too.
		boolean canDelete = !lesson.isLocked() && !lesson.isAlteredByAnother();
		ViewRoleViewer.button_Save.setDisable(false);
		ViewRoleViewer.button_Delete.setDisable(!canDelete);
		ViewRoleViewer.button_AddExperience.setDisable(false);

		if (!canDelete)
			ViewRoleViewer.label_LockNote.setText("Fields marked (Locked) cannot be edited, and "
					+ "this lesson cannot be deleted.");
		else
			ViewRoleViewer.label_LockNote.setText("");
	}

	// Private helper: empty and disable the form when there is no lesson that may be shown
	private static void clearForm() {
		ViewRoleViewer.text_Title.clear();
		ViewRoleViewer.text_Description.clear();
		ViewRoleViewer.combobox_Category.setValue(null);
		ViewRoleViewer.text_Effort.clear();
		ViewRoleViewer.label_Created.setText("");
		ViewRoleViewer.label_LockNote.setText("");

		// Disabled but not marked "(Locked)": there is no lesson, so nothing is locked
		ViewRoleViewer.showLock(ViewRoleViewer.label_Title, ViewRoleViewer.text_Title,
				ViewRoleViewer.TITLE_LABEL, false);
		ViewRoleViewer.showLock(ViewRoleViewer.label_Description,
				ViewRoleViewer.text_Description, ViewRoleViewer.DESCRIPTION_LABEL, false);
		ViewRoleViewer.showLock(ViewRoleViewer.label_Category,
				ViewRoleViewer.combobox_Category, ViewRoleViewer.CATEGORY_LABEL, false);
		ViewRoleViewer.showLock(ViewRoleViewer.label_Effort, ViewRoleViewer.text_Effort,
				ViewRoleViewer.EFFORT_LABEL, false);
		ViewRoleViewer.text_Title.setDisable(true);
		ViewRoleViewer.text_Description.setDisable(true);
		ViewRoleViewer.combobox_Category.setDisable(true);
		ViewRoleViewer.text_Effort.setDisable(true);
		ViewRoleViewer.button_Save.setDisable(true);
		ViewRoleViewer.button_Delete.setDisable(true);
		ViewRoleViewer.button_AddExperience.setDisable(true);
	}
}
