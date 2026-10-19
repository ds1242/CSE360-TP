package lessons.guiLessonDetails;

import java.sql.SQLException;

import database.Database;
import lessons.LessonRecord;
import lessons.LessonsEvaluator;

/*******
 * <p> Title: ModelLessonDetails Class. </p>
 *
 * <p> Description: The Lesson Details Page Model.  This class holds the rules for viewing
 * (Story 3), updating (Story 4), and deleting (Story 5) one lesson.</p>
 *
 * <p> Every method reloads the lesson from the database before acting on it. The page may have
 * been open for some time, and in the meantime a curator may have locked a field or the lesson
 * may have been deleted; acting on the copy shown on the page would miss that.</p>
 *
 * <p> The Model knows nothing about JavaFX. It takes plain values and returns "" on success or
 * the message to show, the same convention as lessons.LessonsEvaluator.</p>
 *
 * @author Shane McPhillips
 *
 * @version 1.00	2026-10-07 Initial version for HW2
 */

public class ModelLessonDetails {

	/** Shown when the database cannot be read or written (worded as in Story 2) */
	protected static final String DATABASE_ERROR = "Unable to connect to database at this time...";

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**
	 * Default constructor is not used.
	 */
	public ModelLessonDetails() {
	}

	/**********
	 * <p> Method: loadLesson(int) </p>
	 *
	 * @param lessonId	the database id of the lesson
	 * @return the lesson as it is in the database now, or null if it is not there
	 * @throws SQLException if the database cannot be read; the Controller turns this into
	 * 			the DATABASE_ERROR message
	 */
	protected static LessonRecord loadLesson(int lessonId) throws SQLException {
		return theDatabase.getLesson(lessonId);
	}

	/**********
	 * <p> Method: updateLesson(int, String, String, String, String, String) </p>
	 *
	 * <p> Description: Saves the contributor's changes (Story 4). Every field is validated
	 * exactly as when a lesson is created ("uniform validation"). Nothing is saved unless every
	 * check passes, so an invalid field can never reach the database, and the contributor is
	 * shown every problem at once.</p>
	 *
	 * <p> Locked fields are shown disabled on the page, so normally their values cannot change.
	 * They are still checked here, because a field may have been locked after the page was
	 * opened and edited.</p>
	 *
	 * @param lessonId		the database id of the lesson
	 * @param username		the logged-in contributor
	 * @param title			the title text
	 * @param description	the description text
	 * @param category		the selected category
	 * @param effortText	the effort text
	 * @return "" on success, otherwise the message(s) to show the contributor
	 */
	protected static String updateLesson(int lessonId, String username, String title,
			String description, String category, String effortText) {

		// Step 1: the lesson must still exist and belong to this contributor
		LessonRecord lesson;
		try {
			lesson = theDatabase.getLesson(lessonId);
		} catch (SQLException e) {
			e.printStackTrace();
			return DATABASE_ERROR;
		}
		String problem = LessonsEvaluator.evaluateCanEdit(lesson, username);
		if (!problem.isEmpty()) return problem;

		// Step 2: no locked field may be changed, and every field must pass the Story 1 rules
		String errors = LessonsEvaluator.evaluateLesson(title, description, category, effortText);
		String lockError = LessonsEvaluator.evaluateLockedFields(lesson, title, description,
				category, effortText);
		if (!lockError.isEmpty())
			errors = errors.isEmpty() ? lockError : lockError + "\n" + errors;
		if (!errors.isEmpty()) return errors;

		// Step 3: save, trimming stray spaces as Create does
		try {
			if (!theDatabase.updateLesson(lessonId, title.trim(), description.trim(), category,
					Float.parseFloat(effortText.trim()), username))
				return LessonsEvaluator.LESSON_NOT_FOUND_ERROR;
		} catch (SQLException e) {
			e.printStackTrace();
			return DATABASE_ERROR;
		}
		return "";
	}

	/**********
	 * <p> Method: checkCanDelete(int, String) </p>
	 *
	 * <p> Description: Called before the Yes/No confirmation is shown, so the contributor is
	 * never asked to confirm a delete that cannot happen.</p>
	 *
	 * @param lessonId	the database id of the lesson
	 * @param username	the logged-in contributor
	 * @return "" if the lesson may be deleted, otherwise the Story 5 message to show
	 */
	protected static String checkCanDelete(int lessonId, String username) {
		try {
			return LessonsEvaluator.evaluateCanDelete(theDatabase.getLesson(lessonId), username);
		} catch (SQLException e) {
			e.printStackTrace();
			return DATABASE_ERROR;
		}
	}

	/**********
	 * <p> Method: deleteLesson(int, String) </p>
	 *
	 * <p> Description: Deletes the lesson after the contributor has confirmed. The checks are
	 * run again because the lesson could have been locked while the confirmation was open.</p>
	 *
	 * @param lessonId	the database id of the lesson
	 * @param username	the logged-in contributor
	 * @return "" if the lesson was deleted, otherwise the message to show
	 */
	protected static String deleteLesson(int lessonId, String username) {
		String problem = checkCanDelete(lessonId, username);
		if (!problem.isEmpty()) return problem;
		try {
			if (!theDatabase.deleteLesson(lessonId)) return LessonsEvaluator.LESSON_NOT_FOUND_ERROR;
		} catch (SQLException e) {
			e.printStackTrace();
			return DATABASE_ERROR;
		}
		return "";
	}

	/**********
	 * <p> Method: checkCanAddExperience(int, String) </p>
	 *
	 * @param lessonId	the database id of the lesson
	 * @param username	the logged-in contributor
	 * @return "" if experience may be added to the lesson, otherwise the message to show
	 */
	protected static String checkCanAddExperience(int lessonId, String username) {
		try {
			return LessonsEvaluator.evaluateCanManageExperience(theDatabase.getLesson(lessonId),
					username);
		} catch (SQLException e) {
			e.printStackTrace();
			return DATABASE_ERROR;
		}
	}
}
