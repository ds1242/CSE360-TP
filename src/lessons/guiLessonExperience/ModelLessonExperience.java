package lessons.guiLessonExperience;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.Database;
import entityClasses.LessonExperience;
import entityClasses.LessonRecord;
import entityClasses.TeammateEffort;
import lessons.LessonsEvaluator;

/*******
 * <p> Title: ModelLessonExperience Class. </p>
 *
 * <p> Description: The Lesson Experience Page Model.  This class holds the rules for adding,
 * editing, and deleting the experience entries of a lesson (Story 6): every value is validated
 * with lessons.LessonsEvaluator, and only the owner of the lesson may change its experience.</p>
 *
 * <p> Ownership is checked against the lesson as it is in the database at the moment of each
 * change, not as it was when the page was opened.</p>
 *
 * @author Shane McPhillips
 *
 * @version 1.00	2026-10-07 Initial version for HW2
 */

public class ModelLessonExperience {

	/** Shown when the database cannot be read or written (worded as in Story 2) */
	protected static final String DATABASE_ERROR = "Unable to connect to database at this time...";

	/** Shown when Delete Entry is pressed with no entry selected. Story 6 does not give the
	 * wording, so it follows the pattern of the Story 5 message. */
	protected static final String ENTRY_NOT_SELECTED_ERROR =
			"Please select an experience entry to delete.";

	/** Shown when the entry being edited or deleted is no longer in the database. Story 6 does
	 * not give the wording, so it follows the pattern of the Story 3 message. */
	protected static final String ENTRY_NOT_FOUND_ERROR =
			"This experience entry could not be found. Is it in the database?";

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**
	 * Default constructor is not used.
	 */
	public ModelLessonExperience() {
	}

	/**********
	 * <p> Method: loadLesson(int) </p>
	 *
	 * @param lessonId	the database id of the lesson
	 * @return the lesson as it is in the database now, or null if it is not there
	 * @throws SQLException if the database cannot be read
	 */
	protected static LessonRecord loadLesson(int lessonId) throws SQLException {
		return theDatabase.getLesson(lessonId);
	}

	/**********
	 * <p> Method: loadExperiences(int) </p>
	 *
	 * @param lessonId	the database id of the lesson
	 * @return the lesson's experience entries, oldest first
	 * @throws SQLException if the database cannot be read
	 */
	protected static List<LessonExperience> loadExperiences(int lessonId) throws SQLException {
		return theDatabase.getExperiences(lessonId);
	}

	/**********
	 * <p> Method: checkTeammate(String, String) </p>
	 *
	 * <p> Description: Checks one teammate row before it is added to the entry on the page.</p>
	 *
	 * @param teammateName	the name of the teammate
	 * @param effortEntry	the description of the teammate's effort
	 * @return "" if the row is valid, otherwise the message(s) to show
	 */
	protected static String checkTeammate(String teammateName, String effortEntry) {
		return LessonsEvaluator.evaluateTeammateEffort(teammateName, effortEntry);
	}

	/**********
	 * <p> Method: saveExperience(...) </p>
	 *
	 * <p> Description: Validates an experience entry and stores it, either as a new entry
	 * (experienceId &lt; 0) or in place of an existing one. Nothing is saved unless every check
	 * passes, and every problem is reported at once.</p>
	 *
	 * <p> A teammate typed into the name and effort fields but not yet added with "Add Teammate"
	 * is included, so it is not silently lost. An entry must have at least one teammate row
	 * (the effort "for them or a team member" in Story 6); if there is none, the empty fields are
	 * checked, which shows the teammate error messages.</p>
	 *
	 * @param lessonId		the database id of the lesson
	 * @param experienceId	the database id of the entry being edited, or -1 for a new entry
	 * @param username		the logged-in contributor
	 * @param whatWasDone	the description of what was done
	 * @param howItWasDone	the description of how it was done
	 * @param durationText	the duration text
	 * @param teammates		the teammate rows already added on the page
	 * @param pendingName	the text in the teammate name field
	 * @param pendingEffort	the text in the teammate effort field
	 * @return "" on success, otherwise the message(s) to show the contributor
	 */
	protected static String saveExperience(int lessonId, int experienceId, String username,
			String whatWasDone, String howItWasDone, String durationText,
			List<TeammateEffort> teammates, String pendingName, String pendingEffort) {

		// Step 1: the lesson must still exist and belong to this contributor
		String problem = checkCanManage(lessonId, username);
		if (!problem.isEmpty()) return problem;

		// Step 2: validate the entry and any teammate still in the input fields
		String errors = LessonsEvaluator.evaluateExperience(whatWasDone, howItWasDone,
				durationText);
		List<TeammateEffort> allTeammates = new ArrayList<>(teammates);
		if (!isBlank(pendingName) || !isBlank(pendingEffort) || allTeammates.isEmpty()) {
			String teammateErrors = checkTeammate(pendingName, pendingEffort);
			if (teammateErrors.isEmpty())
				allTeammates.add(new TeammateEffort(pendingName.trim(), pendingEffort.trim()));
			else
				errors = errors.isEmpty() ? teammateErrors : errors + "\n" + teammateErrors;
		}
		if (!errors.isEmpty()) return errors;

		// Step 3: store it, trimming stray spaces as Create Lesson does
		float duration = Float.parseFloat(durationText.trim());
		try {
			if (experienceId < 0)
				theDatabase.createExperience(lessonId, whatWasDone.trim(), howItWasDone.trim(),
						duration, allTeammates);
			else if (!theDatabase.updateExperience(lessonId, experienceId, whatWasDone.trim(),
					howItWasDone.trim(), duration, allTeammates))
				return ENTRY_NOT_FOUND_ERROR;
		} catch (SQLException e) {
			e.printStackTrace();
			return DATABASE_ERROR;
		}
		return "";
	}

	/**********
	 * <p> Method: checkCanDelete(int, LessonExperience, String) </p>
	 *
	 * <p> Description: Called before the Yes/No confirmation is shown.</p>
	 *
	 * @param lessonId		the database id of the lesson
	 * @param selected		the entry selected in the list, or null if none is selected
	 * @param username		the logged-in contributor
	 * @return "" if the entry may be deleted, otherwise the message to show
	 */
	protected static String checkCanDelete(int lessonId, LessonExperience selected,
			String username) {
		if (selected == null) return ENTRY_NOT_SELECTED_ERROR;
		return checkCanManage(lessonId, username);
	}

	/**********
	 * <p> Method: deleteExperience(int, LessonExperience, String) </p>
	 *
	 * @param lessonId		the database id of the lesson
	 * @param selected		the entry to delete
	 * @param username		the logged-in contributor
	 * @return "" if the entry was deleted, otherwise the message to show
	 */
	protected static String deleteExperience(int lessonId, LessonExperience selected,
			String username) {
		String problem = checkCanDelete(lessonId, selected, username);
		if (!problem.isEmpty()) return problem;
		try {
			if (!theDatabase.deleteExperience(lessonId, selected.getId()))
				return ENTRY_NOT_FOUND_ERROR;
		} catch (SQLException e) {
			e.printStackTrace();
			return DATABASE_ERROR;
		}
		return "";
	}

	// Private helper: "" if the user may change this lesson's experience, otherwise the message
	private static String checkCanManage(int lessonId, String username) {
		try {
			return LessonsEvaluator.evaluateCanManageExperience(theDatabase.getLesson(lessonId),
					username);
		} catch (SQLException e) {
			e.printStackTrace();
			return DATABASE_ERROR;
		}
	}

	// Private helper: true if the text is null or only spaces
	private static boolean isBlank(String text) {
		return text == null || text.trim().isEmpty();
	}
}
