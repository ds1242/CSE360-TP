package lessons.guiContributorLessons;

import java.sql.SQLException;
import java.util.Optional;

import entityClasses.LessonsLearnedList;
import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import lessons.LessonRecord;
import lessons.guiCreateLesson.ViewCreateLesson;
import lessons.guiLessonDetails.ViewLessonDetails;
import lessons.guiLessonExperience.ViewLessonExperience;


/*******
 * <p> Title: ControllerContributorLessons Class. </p>
 *
 * <p> Description: The Java/FX-based My Lessons page.  This class provides the controller
 * actions based on the user's use of the JavaFX GUI widgets defined by the View class, and it
 * fills the View's table with the lessons the Model loads from the database.</p>
 *
 * <p> The class has been written assuming that the View or the Model are the only class methods
 * that can invoke these methods.  This is why each has been declared at "protected".  Do not
 * change any of these methods to public.</p>
 *
 * @author Shane McPhillips
 *
 * @version 1.00	2026-10-07 Initial version for HW2
 * @version 1.01	2026-10-07 View Lesson, Add Experience, and Delete Lesson actions
 */

public class ControllerContributorLessons {

	/*-*******************************************************************************************

	User Interface Actions for this page

	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object).

	 */

	/**
	 * Default constructor is not used.
	 */
	public ControllerContributorLessons() {
	}

	/**********
	 * <p> Method: repaintTheWindow() </p>
	 *
	 * <p> Description: Loads this contributor's lessons from the database, fills the table,
	 * and shows the page. It is called every time the page is displayed, so the table always
	 * reflects the current contents of the database (Story 2).</p>
	 *
	 * <p> If the database cannot be read, the table is emptied and the Story 2 error message is
	 * shown instead, so the contributor is never shown an out-of-date list as if it were current.
	 * </p>
	 */
	protected static void repaintTheWindow() {
		ViewContributorLessons.label_UserDetails.setText("User: "
				+ ViewContributorLessons.theUser.getUserName());

		try {
			LessonsLearnedList myLessons = ModelContributorLessons.loadMyLessons(
					ViewContributorLessons.theUser.getUserName());

			ViewContributorLessons.table_Lessons.setItems(
					FXCollections.observableArrayList(myLessons.getLessons()));
			ViewContributorLessons.label_LessonCount.setText(
					"You have created " + myLessons.size()
					+ (myLessons.size() == 1 ? " lesson." : " lessons."));
			ViewContributorLessons.label_Message.setText("");
		}
		catch (SQLException e) {
			e.printStackTrace();
			ViewContributorLessons.table_Lessons.setItems(FXCollections.observableArrayList());
			ViewContributorLessons.label_LessonCount.setText("");
			ViewContributorLessons.showError(ModelContributorLessons.DATABASE_ERROR);
		}

		// Set the title for the window, display the page, and wait for the user to do something
		ViewContributorLessons.theStage.setTitle("CSE 360 Foundations: My Lessons Learned");
		ViewContributorLessons.theStage.setScene(ViewContributorLessons.theContributorLessonsScene);
		ViewContributorLessons.theStage.show();
	}

	/**********
	 * <p> Method: performCreateLesson() </p>
	 *
	 * <p> Description: Takes the contributor to the Create Lesson page (Story 1).</p>
	 */
	protected static void performCreateLesson() {
		ViewCreateLesson.displayCreateLesson(ViewContributorLessons.theStage,
				ViewContributorLessons.theUser);
	}

	/**********
	 * <p> Method: performViewLesson() </p>
	 *
	 * <p> Description: Opens the lesson selected in the table on the Lesson Details page
	 * (Story 3). If nothing is selected, the lesson is gone, or it belongs to someone else, the
	 * contributor stays on this page and the reason is shown.</p>
	 */
	protected static void performViewLesson() {
		LessonRecord selected = ViewContributorLessons.table_Lessons.getSelectionModel()
				.getSelectedItem();
		String problem = ModelContributorLessons.checkCanView(selected,
				ViewContributorLessons.theUser.getUserName());
		if (!problem.isEmpty()) {
			ViewContributorLessons.showError(problem);
			return;
		}
		ViewLessonDetails.displayLessonDetails(ViewContributorLessons.theStage,
				ViewContributorLessons.theUser, selected.getId());
	}

	/**********
	 * <p> Method: performAddExperience() </p>
	 *
	 * <p> Description: Opens the Experience page for the lesson selected in the table
	 * (Story 6).</p>
	 */
	protected static void performAddExperience() {
		LessonRecord selected = ViewContributorLessons.table_Lessons.getSelectionModel()
				.getSelectedItem();
		String problem = ModelContributorLessons.checkCanAddExperience(selected,
				ViewContributorLessons.theUser.getUserName());
		if (!problem.isEmpty()) {
			ViewContributorLessons.showError(problem);
			return;
		}
		ViewLessonExperience.displayLessonExperience(ViewContributorLessons.theStage,
				ViewContributorLessons.theUser, selected.getId());
	}

	/**********
	 * <p> Method: performDeleteLesson() </p>
	 *
	 * <p> Description: Deletes the lesson selected in the table (Story 5). The checks run first,
	 * so the contributor is only asked to confirm a delete that is allowed. Choosing "No", or
	 * closing the dialog, cancels the delete.</p>
	 */
	protected static void performDeleteLesson() {
		LessonRecord selected = ViewContributorLessons.table_Lessons.getSelectionModel()
				.getSelectedItem();
		String username = ViewContributorLessons.theUser.getUserName();
		String problem = ModelContributorLessons.checkCanDelete(selected, username);
		if (!problem.isEmpty()) {
			ViewContributorLessons.showError(problem);
			return;
		}

		// NOTE: Optional<ButtonType> is needed to handle cases where dialog is closed abruptly
		ViewContributorLessons.alert_DeleteLesson.setContentText(
				"Delete the lesson \"" + selected.getTitle() + "\"?");
		Optional<ButtonType> result = ViewContributorLessons.alert_DeleteLesson.showAndWait();
		if (result.isEmpty() || result.get() != ButtonType.YES) return;

		String deleteResult = ModelContributorLessons.deleteLesson(selected, username);
		repaintTheWindow();
		if (deleteResult.isEmpty())
			ViewContributorLessons.showSuccess("Lesson \"" + selected.getTitle()
					+ "\" was deleted.");
		else
			ViewContributorLessons.showError(deleteResult);
	}

	/**********
	 * <p> Method: performUpdate() </p>
	 *
	 * <p> Description: This method directs the user to the User Update Page so the user can change
	 * the user account attributes. </p>
	 */
	protected static void performUpdate () {
		guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewContributorLessons.theStage,
				ViewContributorLessons.theUser);
	}

	/**********
	 * <p> Method: performReturn() </p>
	 *
	 * <p> Description: This method returns the user to the Contributor Home page. </p>
	 */
	protected static void performReturn() {
		guiContributor.ViewContributorHome.displayContributorHome(ViewContributorLessons.theStage,
				ViewContributorLessons.theUser);
	}

	/**********
	 * <p> Method: performLogout() </p>
	 *
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page where existing users can log in or potential new users with a invitation code can
	 * start the process of setting up an account. </p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewContributorLessons.theStage);
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
}
