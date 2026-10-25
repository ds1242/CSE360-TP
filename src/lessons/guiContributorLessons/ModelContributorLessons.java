package lessons.guiContributorLessons;

import java.sql.SQLException;

import database.Database;
import entityClasses.LessonsLearnedList;
import lessons.LessonRecord;
import lessons.LessonsEvaluator;

/*******
 * <p> Title: ModelContributorLessons Class. </p>
 *
 * <p> Description: The My Lessons Page Model.  This class gets the logged-in contributor's
 * lessons from the application's Database, and decides whether the lesson selected in the table
 * may be viewed (Story 3), given experience (Story 6), or deleted (Story 5).</p>
 *
 * <p> Only the contributor's own lessons are requested. The filtering is done by the database
 * query rather than by hiding rows in the GUI, so another contributor's lessons are never loaded
 * into this page at all (Story 2).</p>
 *
 * <p> Every check reloads the selected lesson from the database first. The table may have been
 * filled some time ago, and the lesson may since have been deleted or locked; checking the row
 * in the table would miss that.</p>
 *
 * @author Shane McPhillips
 *
 * @version 1.00	2026-10-07 Initial version for HW2
 * @version 1.01	2026-10-07 View, Add Experience, and Delete checks for Stories 3, 5, and 6
 */

public class ModelContributorLessons {

	/** Shown when the lessons cannot be read from the database (worded as in Story 2) */
	protected static final String DATABASE_ERROR = "Unable to connect to database at this time...";

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**
	 * Default constructor is not used.
	 */
	public ModelContributorLessons() {
	}

	/**********
	 * <p> Method: loadMyLessons(String) </p>
	 *
	 * <p> Description: Loads every lesson created by the given contributor, newest first.</p>
	 *
	 * @param username	the logged-in contributor
	 * @return a LessonsLearnedList holding that contributor's lessons (possibly empty)
	 * @throws SQLException if the database cannot be read; the Controller turns this into
	 * 			the DATABASE_ERROR message
	 */
	protected static LessonsLearnedList loadMyLessons(String username) throws SQLException {
		return theDatabase.getLessonsByCreator(username);
	}

	/**********
	 * <p> Method: checkCanView(LessonRecord, String) </p>
	 *
	 * @param selected	the lesson selected in the table, or null if none is selected
	 * @param username	the logged-in contributor
	 * @return "" if the lesson may be viewed, otherwise the Story 3 message to show
	 */
	protected static String checkCanView(LessonRecord selected, String username) {
		String problem = LessonsEvaluator.evaluateViewSelection(selected);
		if (!problem.isEmpty()) return problem;
		try {
			return LessonsEvaluator.evaluateCanView(theDatabase.getLesson(selected.getId()),
					username);
		} catch (SQLException e) {
			e.printStackTrace();
			return DATABASE_ERROR;
		}
	}

	/**********
	 * <p> Method: checkCanAddExperience(LessonRecord, String) </p>
	 *
	 * @param selected	the lesson selected in the table, or null if none is selected
	 * @param username	the logged-in contributor
	 * @return "" if experience may be added to the lesson, otherwise the message to show
	 */
	protected static String checkCanAddExperience(LessonRecord selected, String username) {
		String problem = LessonsEvaluator.evaluateExperienceSelection(selected);
		if (!problem.isEmpty()) return problem;
		try {
			return LessonsEvaluator.evaluateCanManageExperience(
					theDatabase.getLesson(selected.getId()), username);
		} catch (SQLException e) {
			e.printStackTrace();
			return DATABASE_ERROR;
		}
	}

	/**********
	 * <p> Method: checkCanDelete(LessonRecord, String) </p>
	 *
	 * <p> Description: Called before the Yes/No confirmation is shown, so the contributor is
	 * never asked to confirm a delete that cannot happen.</p>
	 *
	 * @param selected	the lesson selected in the table, or null if none is selected
	 * @param username	the logged-in contributor
	 * @return "" if the lesson may be deleted, otherwise the Story 5 message to show
	 */
	protected static String checkCanDelete(LessonRecord selected, String username) {
		String problem = LessonsEvaluator.evaluateDeleteSelection(selected);
		if (!problem.isEmpty()) return problem;
		try {
			return LessonsEvaluator.evaluateCanDelete(theDatabase.getLesson(selected.getId()),
					username);
		} catch (SQLException e) {
			e.printStackTrace();
			return DATABASE_ERROR;
		}
	}

	/**********
	 * <p> Method: deleteLesson(LessonRecord, String) </p>
	 *
	 * <p> Description: Deletes the lesson after the contributor has confirmed. The checks are
	 * run again because the lesson could have been locked while the confirmation was open.</p>
	 *
	 * @param selected	the lesson selected in the table
	 * @param username	the logged-in contributor
	 * @return "" if the lesson was deleted, otherwise the message to show
	 */
	protected static String deleteLesson(LessonRecord selected, String username) {
		String problem = checkCanDelete(selected, username);
		if (!problem.isEmpty()) return problem;
		try {
			if (!theDatabase.deleteLesson(selected.getId()))
				return LessonsEvaluator.LESSON_NOT_FOUND_ERROR;
		} catch (SQLException e) {
			e.printStackTrace();
			return DATABASE_ERROR;
		}
		return "";
	}
}
