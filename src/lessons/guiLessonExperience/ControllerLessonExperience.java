package lessons.guiLessonExperience;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.scene.control.ButtonType;
import lessons.LessonExperience;
import lessons.LessonRecord;
import lessons.LessonsEvaluator;
import lessons.TeammateEffort;
import lessons.guiContributorLessons.ViewContributorLessons;

/*******
 * <p> Title: ControllerLessonExperience Class. </p>
 *
 * <p> Description: The Java/FX-based Lesson Experience page.  This class provides the controller
 * actions based on the user's use of the JavaFX GUI widgets defined by the View class.</p>
 *
 * <p> The form on the page is in one of two modes. With no entry selected in the list it adds a
 * new entry; selecting an entry loads it into the form to be edited. "New Entry" returns the
 * form to adding.</p>
 *
 * <p> The class has been written assuming that the View or the Model are the only class methods
 * that can invoke these methods.  This is why each has been declared at "protected".  Do not
 * change any of these methods to public.</p>
 *
 * @author Shane McPhillips
 *
 * @version 1.00	2026-10-07 Initial version for HW2
 */

public class ControllerLessonExperience {

	/*-*******************************************************************************************

	User Interface Actions for this page

	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object).

	 */

	/**
	 * Default constructor is not used.
	 */
	public ControllerLessonExperience() {
	}

	/**********
	 * <p> Method: repaintTheWindow() </p>
	 *
	 * <p> Description: Loads the lesson and its experience entries from the database, resets the
	 * form to add a new entry, and shows the page. If the lesson cannot be used (it is gone, it
	 * belongs to someone else, or the database cannot be read), the form is disabled and the
	 * reason is shown instead.</p>
	 */
	protected static void repaintTheWindow() {
		ViewLessonExperience.label_UserDetails.setText("User: "
				+ ViewLessonExperience.theUser.getUserName());
		ViewLessonExperience.label_Message.setText("");

		String problem;
		try {
			LessonRecord lesson = ModelLessonExperience.loadLesson(ViewLessonExperience.theLessonId);
			problem = LessonsEvaluator.evaluateCanManageExperience(lesson,
					ViewLessonExperience.theUser.getUserName());
			if (problem.isEmpty()) {
				ViewLessonExperience.label_Lesson.setText("Lesson: " + lesson.getTitle());
				ViewLessonExperience.list_Experiences.setItems(FXCollections.observableArrayList(
						ModelLessonExperience.loadExperiences(ViewLessonExperience.theLessonId)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			problem = ModelLessonExperience.DATABASE_ERROR;
		}

		performNewEntry();
		ViewLessonExperience.setFormDisabled(!problem.isEmpty());
		if (!problem.isEmpty()) {
			ViewLessonExperience.label_Lesson.setText("");
			ViewLessonExperience.list_Experiences.setItems(FXCollections.observableArrayList());
			ViewLessonExperience.showProblem(problem);
		}

		// Set the title for the window, display the page, and wait for the user to do something
		ViewLessonExperience.theStage.setTitle("CSE 360 Foundations: Lesson Experience");
		ViewLessonExperience.theStage.setScene(ViewLessonExperience.theLessonExperienceScene);
		ViewLessonExperience.theStage.show();
	}

	/**********
	 * <p> Method: performSelectExperience(LessonExperience) </p>
	 *
	 * <p> Description: Loads the entry selected in the list into the form so it can be edited.
	 * </p>
	 *
	 * @param selected	the entry selected in the list
	 */
	protected static void performSelectExperience(LessonExperience selected) {
		ViewLessonExperience.theSelectedExperience = selected;
		ViewLessonExperience.text_WhatWasDone.setText(selected.getWhatWasDone());
		ViewLessonExperience.text_HowItWasDone.setText(selected.getHowItWasDone());
		ViewLessonExperience.text_Duration.setText(String.valueOf(selected.getDurationHours()));
		ViewLessonExperience.list_Teammates.setItems(
				FXCollections.observableArrayList(selected.getTeammates()));
		ViewLessonExperience.text_TeammateName.clear();
		ViewLessonExperience.text_TeammateEffort.clear();

		ViewLessonExperience.label_Mode.setText("Editing the entry selected on the left");
		ViewLessonExperience.button_Save.setText("Save Changes");
		ViewLessonExperience.label_Message.setText("");
	}

	/**********
	 * <p> Method: performNewEntry() </p>
	 *
	 * <p> Description: Empties the form and sets it to add a new entry. Called when the
	 * contributor presses "New Entry", after a save or delete, and each time the page is shown.
	 * </p>
	 */
	protected static void performNewEntry() {
		ViewLessonExperience.theSelectedExperience = null;
		ViewLessonExperience.list_Experiences.getSelectionModel().clearSelection();
		ViewLessonExperience.text_WhatWasDone.clear();
		ViewLessonExperience.text_HowItWasDone.clear();
		ViewLessonExperience.text_Duration.clear();
		ViewLessonExperience.list_Teammates.setItems(FXCollections.observableArrayList());
		ViewLessonExperience.text_TeammateName.clear();
		ViewLessonExperience.text_TeammateEffort.clear();

		ViewLessonExperience.label_Mode.setText("Adding a new entry");
		ViewLessonExperience.button_Save.setText("Save New Entry");
		ViewLessonExperience.label_Message.setText("");
	}

	/**********
	 * <p> Method: performAddTeammate() </p>
	 *
	 * <p> Description: Adds the teammate in the name and effort fields to the entry on the page.
	 * The row is only saved to the database when the whole entry is saved.</p>
	 */
	protected static void performAddTeammate() {
		String name = ViewLessonExperience.text_TeammateName.getText();
		String effort = ViewLessonExperience.text_TeammateEffort.getText();
		String problem = ModelLessonExperience.checkTeammate(name, effort);
		if (!problem.isEmpty()) {
			ViewLessonExperience.showError(problem);
			return;
		}
		ViewLessonExperience.list_Teammates.getItems().add(
				new TeammateEffort(name.trim(), effort.trim()));
		ViewLessonExperience.text_TeammateName.clear();
		ViewLessonExperience.text_TeammateEffort.clear();
	}

	/**********
	 * <p> Method: performRemoveTeammate() </p>
	 *
	 * <p> Description: Removes the selected teammate from the entry on the page. The change is
	 * only saved to the database when the whole entry is saved.</p>
	 */
	protected static void performRemoveTeammate() {
		TeammateEffort selected = ViewLessonExperience.list_Teammates.getSelectionModel()
				.getSelectedItem();
		if (selected == null) {
			ViewLessonExperience.showError("Please select a teammate to remove.");
			return;
		}
		ViewLessonExperience.list_Teammates.getItems().remove(selected);
	}

	/**********
	 * <p> Method: performSave() </p>
	 *
	 * <p> Description: Saves the entry in the form, as a new entry or in place of the one being
	 * edited (Story 6). On success the list is reloaded from the database and the form is reset;
	 * on failure the form is left as typed and every error is shown.</p>
	 */
	protected static void performSave() {
		LessonExperience editing = ViewLessonExperience.theSelectedExperience;
		String result = ModelLessonExperience.saveExperience(
				ViewLessonExperience.theLessonId,
				editing == null ? -1 : editing.getId(),
				ViewLessonExperience.theUser.getUserName(),
				ViewLessonExperience.text_WhatWasDone.getText(),
				ViewLessonExperience.text_HowItWasDone.getText(),
				ViewLessonExperience.text_Duration.getText(),
				new ArrayList<>(ViewLessonExperience.list_Teammates.getItems()),
				ViewLessonExperience.text_TeammateName.getText(),
				ViewLessonExperience.text_TeammateEffort.getText());

		if (result.isEmpty()) {
			repaintTheWindow();
			ViewLessonExperience.showSuccess(editing == null ? "The experience entry was saved."
					: "Your changes to the experience entry were saved.");
		}
		else {
			ViewLessonExperience.showError(result);
		}
	}

	/**********
	 * <p> Method: performDeleteEntry() </p>
	 *
	 * <p> Description: Deletes the entry selected in the list after the contributor confirms.
	 * Choosing "No", or closing the dialog, cancels the delete.</p>
	 */
	protected static void performDeleteEntry() {
		LessonExperience selected = ViewLessonExperience.list_Experiences.getSelectionModel()
				.getSelectedItem();
		String username = ViewLessonExperience.theUser.getUserName();
		String problem = ModelLessonExperience.checkCanDelete(ViewLessonExperience.theLessonId,
				selected, username);
		if (!problem.isEmpty()) {
			ViewLessonExperience.showError(problem);
			return;
		}

		// NOTE: Optional<ButtonType> is needed to handle cases where dialog is closed abruptly
		Optional<ButtonType> result = ViewLessonExperience.alert_DeleteEntry.showAndWait();
		if (result.isEmpty() || result.get() != ButtonType.YES) return;

		String deleteResult = ModelLessonExperience.deleteExperience(
				ViewLessonExperience.theLessonId, selected, username);
		if (deleteResult.isEmpty()) {
			repaintTheWindow();
			ViewLessonExperience.showSuccess("The experience entry was deleted.");
		}
		else {
			ViewLessonExperience.showError(deleteResult);
		}
	}

	/**********
	 * <p> Method: performUpdate() </p>
	 *
	 * <p> Description: This method directs the user to the User Update Page so the user can change
	 * the user account attributes. </p>
	 */
	protected static void performUpdate () {
		guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewLessonExperience.theStage,
				ViewLessonExperience.theUser);
	}

	/**********
	 * <p> Method: performReturn() </p>
	 *
	 * <p> Description: This method returns the user to the My Lessons page. </p>
	 */
	protected static void performReturn() {
		ViewContributorLessons.displayContributorLessons(ViewLessonExperience.theStage,
				ViewLessonExperience.theUser);
	}

	/**********
	 * <p> Method: performLogout() </p>
	 *
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page where existing users can log in or potential new users with a invitation code can
	 * start the process of setting up an account. </p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewLessonExperience.theStage);
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
