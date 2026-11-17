package lessons.guiCreateLesson;

import java.sql.SQLException;

import database.Database;
import lessons.LessonsEvaluator;

/*******
 * <p> Title: ModelCreateLesson Class. </p>
 *
 * <p> Description: The Create Lesson Page Model.  This class holds the logic for creating a
 * lesson: validate the input with LessonsEvaluator, and only if it is all valid, store it
 * through the application's Database.</p>
 *
 * <p> The Model knows nothing about JavaFX. It takes plain Strings and returns a plain String,
 * which keeps the logic testable without a GUI and means the page could be redesigned without
 * touching any of the rules here.</p>
 *
 * @author Shane McPhillips
 *
 * @version 1.00	2026-10-07 Initial version for HW2
 */

public class ModelCreateLesson {

	/** Shown when the lesson cannot be written to the database (worded as in Story 2) */
	protected static final String DATABASE_ERROR = "Unable to connect to database at this time...";

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**
	 * Default constructor is not used.
	 */
	public ModelCreateLesson() {
	}

	/**********
	 * <p> Method: createLesson(String, String, String, String, String) </p>
	 *
	 * <p> Description: Validates the contributor's input and stores the new lesson. The database
	 * is only called once every field has passed, which is what guarantees an invalid lesson can
	 * never be persisted.</p>
	 *
	 * @param title				the title text
	 * @param description		the description text
	 * @param category			the selected category (null if none was selected)
	 * @param effortText		the effort text
	 * @param creatorUsername	the logged-in contributor, who becomes the lesson's owner
	 * @return "" on success, otherwise the message(s) to show the contributor
	 */
	protected static String createLesson(String title, String description, String category,
			String effortText, String creatorUsername) {

		// Step 1: validate every field, and stop if anything is wrong
		String errors = LessonsEvaluator.evaluateLesson(title, description, category, effortText);
		if (!errors.isEmpty()) return errors;

		// Step 2: store the lesson. The values are trimmed so stray spaces the contributor typed
		// are not saved. A database failure is reported instead of crashing the application.
		try {
			theDatabase.createLesson(
					title.trim(),
					description.trim(),
					category,
					Float.parseFloat(effortText.trim()),
					creatorUsername);
		} catch (SQLException e) {
			e.printStackTrace();
			return DATABASE_ERROR;
		}
		return "";
	}
}
