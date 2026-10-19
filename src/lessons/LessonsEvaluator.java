package lessons;

import java.util.List;

/*******
 * <p> Title: LessonsEvaluator Class </p>
 *
 * <p> Description: Validates the core information of a lesson learned, using the rules and the
 * exact error messages from User Stories 1 and 4, and checks experience information (Story 6)
 * and whether a user may view, edit, or delete a lesson (Stories 3, 4, and 5). Each method
 * returns an empty string when the input is valid, or the error message to show the user when
 * it is not.</p>
 *
 * <p> Why return "" or a message? This is the same convention the evaluators in
 * cse360.GRP.ADES.evaluator use, so the GUI can treat every evaluator the same way: an empty
 * result means "go ahead", anything else is shown to the user. Keeping the rules here, not in
 * the GUI, means Create (Story 1) and Update (Story 4) share one copy of the rules ("uniform
 * validation"), and the automated tests can call these methods without starting JavaFX.</p>
 *
 * <p> Note: the en dash in some messages is written as the escape for U+2013 so the source
 * compiles the same way whatever text encoding Eclipse is set to use.</p>
 *
 * @author Shane McPhillips
 *
 * @version 1.00	2026-10-07 Initial version for HW2
 * @version 1.01	2026-10-07 Experience, permission, and lock rules for Stories 3-6
 */
public class LessonsEvaluator {

	/*-*******************************************************************************************

	Limits and allowed values (from User Story 1)

	 */

	/** Minimum number of characters in a title */
	public static final int TITLE_MIN = 5;
	/** Maximum number of characters in a title (also the database column size) */
	public static final int TITLE_MAX = 100;
	/** Minimum number of characters in a description */
	public static final int DESCRIPTION_MIN = 20;
	/** Maximum number of characters in a description (also the database column size) */
	public static final int DESCRIPTION_MAX = 2000;

	/** The listed categories a lesson may use. A fixed list keeps lessons groupable and
	 * searchable instead of every contributor inventing their own spelling. */
	public static final List<String> CATEGORIES = List.of(
			"STEM", "Finance", "Business", "Government", "Infastructure", "Other");


	/*-*******************************************************************************************

	Error messages (worded exactly as in User Story 1)

	 */

	/** Shown when the title is too short, too long, or missing */
	public static final String TITLE_ERROR =
			"Title must be a minimum of 5 characters and a maximum of 100 characters long.";
	/** Shown when the description is too short, too long, or missing */
	public static final String DESCRIPTION_ERROR =
			"The description must be between 20 – 2,000 characters in length to provide "
			+ "ample knowledge of the lesson.";
	/** Shown when no listed category is selected */
	public static final String CATEGORY_ERROR = "Please select a valid category";
	/** Shown when the effort is not a number greater than zero */
	public static final String EFFORT_ERROR = "Time of effort must be greater than 0 hours.";


	/*-*******************************************************************************************

	Limits and error messages for experience information (worded exactly as in User Story 6)

	 */

	/** Minimum number of characters in "what was done", "how it was done", and an effort entry */
	public static final int EXPERIENCE_TEXT_MIN = 20;
	/** Maximum number of characters in "what was done", "how it was done", and an effort entry */
	public static final int EXPERIENCE_TEXT_MAX = 2000;
	/** Minimum number of characters in a teammate's name */
	public static final int TEAMMATE_NAME_MIN = 2;
	/** Maximum number of characters in a teammate's name (also the database column size) */
	public static final int TEAMMATE_NAME_MAX = 50;

	/** Shown when "what was done" is too short, too long, or missing */
	public static final String WHAT_WAS_DONE_ERROR =
			"Must provide a description of what was done during the lesson between "
			+ "20 – 2000 characters long.";
	/** Shown when "how it was done" is too short, too long, or missing */
	public static final String HOW_IT_WAS_DONE_ERROR =
			"Must provide a description of how it was done during the lesson between "
			+ "20 – 2000 characters long.";
	/** Shown when the duration is not a number greater than zero */
	public static final String DURATION_ERROR = "Duration must be a value greater than 0.";
	/** Shown when a teammate's name is too short, too long, or missing */
	public static final String TEAMMATE_NAME_ERROR =
			"Teammate name must be at least 2 characters long and no longer than 50 characters.";
	/** Shown when a teammate's effort entry is too short, too long, or missing */
	public static final String EFFORT_ENTRY_ERROR =
			"Experience effort entry must be at least 20 characters long and no longer than "
			+ "2,000 characters.";


	/*-*******************************************************************************************

	Selection, permission, and lock messages (worded exactly as in User Stories 2, 3, 4, and 5)

	 */

	/** Shown in place of the lesson list when the contributor has no lessons (Story 2) */
	public static final String EMPTY_LIST_MESSAGE =
			"Please create a new lesson to populate the lesson list.";
	/** Shown when View is pressed with no lesson selected (Story 3) */
	public static final String VIEW_NOT_SELECTED_ERROR =
			"A lesson must be selected in order to view.";
	/** Shown when the selected lesson is no longer in the database (Story 3) */
	public static final String LESSON_NOT_FOUND_ERROR =
			"This lesson could not be found. Is it in the database?";
	/** Shown when a user tries to view a lesson someone else owns (Story 3) */
	public static final String VIEW_PERMISSION_ERROR =
			"You do not have permission to view this lesson.";
	/** Shown when a save would change a locked field (Story 4) */
	public static final String LOCKED_FIELD_ERROR = "Cannot edit a locked field.";
	/** Shown when a user tries to edit a lesson someone else owns (Story 4) */
	public static final String EDIT_PERMISSION_ERROR =
			"You do not have the permission to edit this lesson.";
	/** Shown when Delete is pressed with no lesson selected (Story 5) */
	public static final String DELETE_NOT_SELECTED_ERROR = "Please select a lesson to delete.";
	/** Shown when a lesson cannot be deleted because it is locked or was changed by another user
	 * (Story 5) */
	public static final String DELETE_BLOCKED_ERROR =
			"Cannot perform delete. A field has been altered by another user / locked.";
	/** Shown when a user tries to delete a lesson someone else owns (Story 5) */
	public static final String DELETE_PERMISSION_ERROR =
			"You do not have the permission to perform this action.";

	/** Shown when Add Experience is pressed with no lesson selected. Story 6 does not give the
	 * wording, so it follows the pattern of the Story 5 message. */
	public static final String EXPERIENCE_NOT_SELECTED_ERROR =
			"Please select a lesson to add experience to.";
	/** Shown when a user tries to add experience to a lesson someone else owns. Story 6 does not
	 * give the wording, so it follows the pattern of the Story 4 message. */
	public static final String EXPERIENCE_PERMISSION_ERROR =
			"You do not have the permission to add experience to this lesson.";


	/**
	 * Default constructor is not used; every method is static.
	 */
	public LessonsEvaluator() {
	}


	/*-*******************************************************************************************

	Field evaluators

	 */

	/**********
	 * <p> Method: evaluateTitle(String) </p>
	 *
	 * <p> Description: The title must be 5-100 characters. The text is trimmed first so a title
	 * made only of spaces is not accepted.</p>
	 *
	 * @param title	the title entered by the contributor
	 * @return "" if valid, otherwise TITLE_ERROR
	 */
	public static String evaluateTitle(String title) {
		if (title == null) return TITLE_ERROR;
		int length = title.trim().length();
		if (length < TITLE_MIN || length > TITLE_MAX) return TITLE_ERROR;
		return "";
	}


	/**********
	 * <p> Method: evaluateDescription(String) </p>
	 *
	 * <p> Description: The description must be 20-2,000 characters after trimming, so it holds
	 * enough to be useful and still fits the database column.</p>
	 *
	 * @param description	the description entered by the contributor
	 * @return "" if valid, otherwise DESCRIPTION_ERROR
	 */
	public static String evaluateDescription(String description) {
		if (description == null) return DESCRIPTION_ERROR;
		int length = description.trim().length();
		if (length < DESCRIPTION_MIN || length > DESCRIPTION_MAX) return DESCRIPTION_ERROR;
		return "";
	}


	/**********
	 * <p> Method: evaluateCategory(String) </p>
	 *
	 * <p> Description: The category must be one of CATEGORIES. The GUI uses a ComboBox, so an
	 * unlisted value usually means nothing was selected (null). The check stays here so the rule
	 * holds even when a lesson is created without the GUI, such as from a test.</p>
	 *
	 * @param category	the category selected by the contributor
	 * @return "" if valid, otherwise CATEGORY_ERROR
	 */
	public static String evaluateCategory(String category) {
		if (category == null || !CATEGORIES.contains(category)) return CATEGORY_ERROR;
		return "";
	}


	/**********
	 * <p> Method: evaluateEffort(String) </p>
	 *
	 * <p> Description: Effort arrives from a text field, so it is a String. It must parse as a
	 * number (decimals are fine) and be greater than 0. NaN and Infinity are rejected on purpose,
	 * because Float.parseFloat accepts both as valid input.</p>
	 *
	 * @param effortText	the effort entered by the contributor
	 * @return "" if valid, otherwise EFFORT_ERROR
	 */
	public static String evaluateEffort(String effortText) {
		if (!isPositiveNumber(effortText)) return EFFORT_ERROR;
		return "";
	}


	/**********
	 * <p> Method: evaluateLesson(String, String, String, String) </p>
	 *
	 * <p> Description: Runs every field evaluator and returns all of the problems at once, one
	 * per line. Reporting every error together means the contributor can fix the whole form in
	 * one pass instead of finding problems one save at a time.</p>
	 *
	 * @param title			the title text
	 * @param description	the description text
	 * @param category		the selected category
	 * @param effortText	the effort text
	 * @return "" if every field is valid, otherwise every error message, separated by newlines
	 */
	public static String evaluateLesson(String title, String description, String category,
			String effortText) {
		StringBuilder errors = new StringBuilder();
		addError(errors, evaluateTitle(title));
		addError(errors, evaluateDescription(description));
		addError(errors, evaluateCategory(category));
		addError(errors, evaluateEffort(effortText));
		return errors.toString();
	}



	/*-*******************************************************************************************

	Experience evaluators (Story 6)

	 */

	/**********
	 * <p> Method: evaluateWhatWasDone(String) </p>
	 *
	 * @param whatWasDone	the description of what was done
	 * @return "" if it is 20-2,000 characters after trimming, otherwise WHAT_WAS_DONE_ERROR
	 */
	public static String evaluateWhatWasDone(String whatWasDone) {
		if (!isLengthBetween(whatWasDone, EXPERIENCE_TEXT_MIN, EXPERIENCE_TEXT_MAX))
			return WHAT_WAS_DONE_ERROR;
		return "";
	}

	/**********
	 * <p> Method: evaluateHowItWasDone(String) </p>
	 *
	 * @param howItWasDone	the description of how it was done
	 * @return "" if it is 20-2,000 characters after trimming, otherwise HOW_IT_WAS_DONE_ERROR
	 */
	public static String evaluateHowItWasDone(String howItWasDone) {
		if (!isLengthBetween(howItWasDone, EXPERIENCE_TEXT_MIN, EXPERIENCE_TEXT_MAX))
			return HOW_IT_WAS_DONE_ERROR;
		return "";
	}

	/**********
	 * <p> Method: evaluateDuration(String) </p>
	 *
	 * <p> Description: Uses the same number rule as evaluateEffort, with the Story 6 message.</p>
	 *
	 * @param durationText	the duration entered by the contributor, in hours
	 * @return "" if valid, otherwise DURATION_ERROR
	 */
	public static String evaluateDuration(String durationText) {
		if (!isPositiveNumber(durationText)) return DURATION_ERROR;
		return "";
	}

	/**********
	 * <p> Method: evaluateTeammateName(String) </p>
	 *
	 * @param teammateName	the name of a teammate
	 * @return "" if it is 2-50 characters after trimming, otherwise TEAMMATE_NAME_ERROR
	 */
	public static String evaluateTeammateName(String teammateName) {
		if (!isLengthBetween(teammateName, TEAMMATE_NAME_MIN, TEAMMATE_NAME_MAX))
			return TEAMMATE_NAME_ERROR;
		return "";
	}

	/**********
	 * <p> Method: evaluateEffortEntry(String) </p>
	 *
	 * @param effortEntry	the description of a teammate's effort
	 * @return "" if it is 20-2,000 characters after trimming, otherwise EFFORT_ENTRY_ERROR
	 */
	public static String evaluateEffortEntry(String effortEntry) {
		if (!isLengthBetween(effortEntry, EXPERIENCE_TEXT_MIN, EXPERIENCE_TEXT_MAX))
			return EFFORT_ENTRY_ERROR;
		return "";
	}

	/**********
	 * <p> Method: evaluateTeammateEffort(String, String) </p>
	 *
	 * <p> Description: Checks one teammate row (name and effort entry) and returns both problems
	 * at once.</p>
	 *
	 * @param teammateName	the name of the teammate
	 * @param effortEntry	the description of the teammate's effort
	 * @return "" if both are valid, otherwise every error message, separated by newlines
	 */
	public static String evaluateTeammateEffort(String teammateName, String effortEntry) {
		StringBuilder errors = new StringBuilder();
		addError(errors, evaluateTeammateName(teammateName));
		addError(errors, evaluateEffortEntry(effortEntry));
		return errors.toString();
	}

	/**********
	 * <p> Method: evaluateExperience(String, String, String) </p>
	 *
	 * <p> Description: Checks the three experience fields together, so every problem is reported
	 * in one pass. Teammate rows are checked separately, one at a time, by
	 * evaluateTeammateEffort.</p>
	 *
	 * @param whatWasDone	the description of what was done
	 * @param howItWasDone	the description of how it was done
	 * @param durationText	the duration text
	 * @return "" if every field is valid, otherwise every error message, separated by newlines
	 */
	public static String evaluateExperience(String whatWasDone, String howItWasDone,
			String durationText) {
		StringBuilder errors = new StringBuilder();
		addError(errors, evaluateWhatWasDone(whatWasDone));
		addError(errors, evaluateHowItWasDone(howItWasDone));
		addError(errors, evaluateDuration(durationText));
		return errors.toString();
	}


	/*-*******************************************************************************************

	Selection evaluators (Stories 3, 5, and 6)

	These check that a lesson was selected in the My Lessons table before a button that acts on
	one was pressed. They are here, not in the page's Model, so the test bed can run exactly the
	same check the GUI does.

	 */

	/**********
	 * <p> Method: evaluateViewSelection(LessonRecord) </p>
	 *
	 * @param selected	the lesson selected in the table, or null if none is selected
	 * @return "" if a lesson is selected, otherwise VIEW_NOT_SELECTED_ERROR
	 */
	public static String evaluateViewSelection(LessonRecord selected) {
		if (selected == null) return VIEW_NOT_SELECTED_ERROR;
		return "";
	}

	/**********
	 * <p> Method: evaluateDeleteSelection(LessonRecord) </p>
	 *
	 * @param selected	the lesson selected in the table, or null if none is selected
	 * @return "" if a lesson is selected, otherwise DELETE_NOT_SELECTED_ERROR
	 */
	public static String evaluateDeleteSelection(LessonRecord selected) {
		if (selected == null) return DELETE_NOT_SELECTED_ERROR;
		return "";
	}

	/**********
	 * <p> Method: evaluateExperienceSelection(LessonRecord) </p>
	 *
	 * @param selected	the lesson selected in the table, or null if none is selected
	 * @return "" if a lesson is selected, otherwise EXPERIENCE_NOT_SELECTED_ERROR
	 */
	public static String evaluateExperienceSelection(LessonRecord selected) {
		if (selected == null) return EXPERIENCE_NOT_SELECTED_ERROR;
		return "";
	}


	/*-*******************************************************************************************

	Lock evaluator (Story 4)

	 */

	/**********
	 * <p> Method: evaluateLockedFields(LessonRecord, String, String, String, String) </p>
	 *
	 * <p> Description: A locked field may not be changed. Text is compared after trimming, as it
	 * would be saved, and effort is compared as a number so that "3.5" and "3.50" count as the
	 * same value. Locked fields are disabled on the Lesson Details page, so normally this only
	 * fails when a field is locked while the page is open.</p>
	 *
	 * @param lesson		the lesson as it is in the database now
	 * @param title			the title text being saved
	 * @param description	the description text being saved
	 * @param category		the category being saved
	 * @param effortText	the effort text being saved
	 * @return "" if no locked field would change, otherwise LOCKED_FIELD_ERROR
	 */
	public static String evaluateLockedFields(LessonRecord lesson, String title,
			String description, String category, String effortText) {
		if (lesson.isTitleLocked() && !lesson.getTitle().equals(trimmed(title)))
			return LOCKED_FIELD_ERROR;
		if (lesson.isDescriptionLocked() && !lesson.getDescription().equals(trimmed(description)))
			return LOCKED_FIELD_ERROR;
		if (lesson.isCategoryLocked() && !lesson.getCategory().equals(category))
			return LOCKED_FIELD_ERROR;
		if (lesson.isEffortLocked()) {
			try {
				if (Float.parseFloat(trimmed(effortText)) != lesson.getDurationHours())
					return LOCKED_FIELD_ERROR;
			} catch (NumberFormatException | NullPointerException e) {
				return LOCKED_FIELD_ERROR;
			}
		}
		return "";
	}


	/*-*******************************************************************************************

	Permission evaluators (Stories 3, 4, 5, and 6)

	These take the lesson as it is in the database right now (null if it is no longer there) and
	the logged-in user. The pages always reload the lesson just before calling one of these, so a
	lesson that was deleted or locked while the page was open is caught here.

	 */

	/**********
	 * <p> Method: evaluateCanView(LessonRecord, String) </p>
	 *
	 * @param lesson	the lesson from the database, or null if it was not found
	 * @param username	the logged-in user
	 * @return "" if the user may view the lesson, otherwise the Story 3 message
	 */
	public static String evaluateCanView(LessonRecord lesson, String username) {
		if (lesson == null) return LESSON_NOT_FOUND_ERROR;
		if (!lesson.getCreatorUsername().equals(username)) return VIEW_PERMISSION_ERROR;
		return "";
	}

	/**********
	 * <p> Method: evaluateCanEdit(LessonRecord, String) </p>
	 *
	 * @param lesson	the lesson from the database, or null if it was not found
	 * @param username	the logged-in user
	 * @return "" if the user may edit the lesson, otherwise the Story 4 message
	 */
	public static String evaluateCanEdit(LessonRecord lesson, String username) {
		if (lesson == null) return LESSON_NOT_FOUND_ERROR;
		if (!lesson.getCreatorUsername().equals(username)) return EDIT_PERMISSION_ERROR;
		return "";
	}

	/**********
	 * <p> Method: evaluateCanDelete(LessonRecord, String) </p>
	 *
	 * <p> Description: Ownership is checked before locking, so a user who does not own the lesson
	 * is not told anything about its state.</p>
	 *
	 * @param lesson	the lesson from the database, or null if it was not found
	 * @param username	the logged-in user
	 * @return "" if the user may delete the lesson, otherwise the Story 5 message
	 */
	public static String evaluateCanDelete(LessonRecord lesson, String username) {
		if (lesson == null) return LESSON_NOT_FOUND_ERROR;
		if (!lesson.getCreatorUsername().equals(username)) return DELETE_PERMISSION_ERROR;
		if (lesson.isLocked() || lesson.isAlteredByAnother()) return DELETE_BLOCKED_ERROR;
		return "";
	}

	/**********
	 * <p> Method: evaluateCanManageExperience(LessonRecord, String) </p>
	 *
	 * @param lesson	the lesson from the database, or null if it was not found
	 * @param username	the logged-in user
	 * @return "" if the user may add, edit, or delete experience on the lesson, otherwise the
	 * 			message to show
	 */
	public static String evaluateCanManageExperience(LessonRecord lesson, String username) {
		if (lesson == null) return LESSON_NOT_FOUND_ERROR;
		if (!lesson.getCreatorUsername().equals(username)) return EXPERIENCE_PERMISSION_ERROR;
		return "";
	}


	/*-*******************************************************************************************

	Private helpers

	 */

	// Append a non-empty message on its own line
	private static void addError(StringBuilder errors, String message) {
		if (message.isEmpty()) return;
		if (errors.length() > 0) errors.append("\n");
		errors.append(message);
	}

	// Trim text that may be null
	private static String trimmed(String text) {
		return text == null ? null : text.trim();
	}

	// True if the trimmed text is between min and max characters long (inclusive)
	private static boolean isLengthBetween(String text, int min, int max) {
		if (text == null) return false;
		int length = text.trim().length();
		return length >= min && length <= max;
	}

	// True if the text is a number greater than 0. NaN and Infinity are rejected on purpose,
	// because Float.parseFloat accepts both as valid input.
	private static boolean isPositiveNumber(String text) {
		if (text == null || text.trim().isEmpty()) return false;
		try {
			float value = Float.parseFloat(text.trim());
			return !Float.isNaN(value) && !Float.isInfinite(value) && value > 0;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
