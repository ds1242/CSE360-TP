package lessons.evaluationTestbed;

import java.sql.SQLException;

import database.Database;
import lessons.LessonRecord;
import lessons.LessonsEvaluator;
import lessons.LessonsLearnedManager;

/*******
 * <p> Title: LessonsLearnedEvaluationTestbed Class. </p>
 *
 * <p> Description: A standalone, semi-automated test bed for the lessons learned functions of
 * HW2. It performs test cases TC1 - TC14 from the HW2 Task 3 test case document, in the same
 * style as passwordEvaluationTestbedMain.PasswordEvaluationTestingAutomation: each test case
 * displays a header and its input, calls exactly the same evaluator and database methods the
 * JavaFX pages call, and reports whether the result is the one the test case expects.</p>
 *
 * <p> A rejected input only counts as a pass when it is rejected with the expected error
 * message, worded as in the HW2 User Stories, so a test also detects a wrong or missing
 * message.</p>
 *
 * <p> The test cases that need stored lessons (TC1, TC7, TC8, TC9, TC13, and TC14)
 * use a separate H2 database file, {@code ~/FoundationDatabaseTest}, so the test lessons never
 * mix with real users and lessons in {@code ~/FoundationDatabase}. The test bed can therefore be
 * run while the application is also running. Lessons left by an earlier run are removed at the
 * start.</p>
 *
 * <p> The JavaFX widgets themselves (for example, the Yes/No delete confirmation dialog and the
 * placeholder text in the empty table) cannot be shown without starting the GUI. For those test
 * cases the test bed checks the logic the page runs, and the widget is demonstrated in the
 * application itself.</p>
 *
 * <p> Test case summary:</p>
 * <ul>
 * <li>TC1 - TC6: create a lesson (one valid case and five invalid inputs)</li>
 * <li>TC7 - TC10: view lessons (persistence, empty list, separate lists, View with no
 * selection)</li>
 * <li>TC11: Delete with no lesson selected</li>
 * <li>TC12: experience entry with every field blank</li>
 * <li>TC13 - TC14: confirmed and canceled delete</li>
 * </ul>
 *
 * @author Shane McPhillips
 *
 * @version 1.00	2026-10-07 Test cases for HW2
 */
public class LessonsLearnedEvaluationTestbed {

	/** ANSI escape code that turns console text red (used for failures). */
	static String RED = "\u001B[31m";

	/** ANSI escape code that turns console text green (used for passes). */
    static String GREEN = "\u001B[32m";

    /** ANSI escape code that resets console text to the default color. */
    static String RESET = "\u001B[0m";

    /** Green prefix printed in front of the report for a passed test case. */
    static String SUCCESS = GREEN + "***Success***";

    /** Red prefix printed in front of the report for a failed test case. */
    static String FAILURE = RED + "***Failure***";

    /** Counter of the number of passed tests. */
	static int numPassed = 0;

	/** Counter of the number of failed tests. */
	static int numFailed = 0;

	/** JDBC URL of the test bed's own database, kept apart from the application's {@code ~/FoundationDatabase}. */
	static final String TEST_DB_URL = "jdbc:h2:~/FoundationDatabaseTest";

	/** The test database. It is replaced by a new connection in TC7, which simulates an application restart. */
	static Database theDatabase = new Database(TEST_DB_URL);

	/** First test contributor; owns most of the lessons the test cases create. */
	static final String TEST_CONTRIBUTOR_1 = "testContributor1";

	/** Second test contributor; used by TC9 to check that lists are kept separate. */
	static final String TEST_CONTRIBUTOR_2 = "testContributor2";

	/** Third test contributor; never creates a lesson, so TC8 gets an empty list. */
	static final String TEST_CONTRIBUTOR_3 = "testContributor3";

	/** A valid title, used wherever a test case needs only one other field to be wrong. */
	static final String VALID_TITLE = "Commit small changes often";

	/** A valid description, used wherever a test case needs only one other field to be wrong. */
	static final String VALID_DESCRIPTION = "Small commits made it easy to find the change that "
			+ "broke the build.";

	/** A valid category, used wherever a test case needs only one other field to be wrong. */
	static final String VALID_CATEGORY = "STEM";

	/** A valid number of hours (as text), used wherever a test case needs only one other field to be wrong. */
	static final String VALID_EFFORT = "2.5";

	/**
	 * Mainline of the test bed. It displays a report header, connects to the test database and
	 * clears any lessons left by an earlier run, performs the test cases in order, removes the
	 * test lessons again, closes the database, and displays a footer with the number of tests
	 * passed and failed. If the test database cannot be opened, no test cases are run.
	 *
	 * @param args command line arguments (not used)
	 */
	public static void main(String[] args) {
		/************** Test cases semi-automation report header **************/
		System.out.println("______________________________________");
		System.out.println("\nLessons Learned Testing Automation");

		try {
			theDatabase.connectToDatabase();
			removeTestLessons();
		} catch (SQLException e) {
			System.out.println(failure("Unable to connect to the test database " + TEST_DB_URL
					+ ". No test cases were run."));
			e.printStackTrace();
			return;
		}

		System.out.println("/**************************************************************/");
		System.out.println("/************** Start of the create lesson test cases *********/");
		System.out.println("/**************************************************************/");
		// These test cases use evaluateLesson in the LessonsEvaluator class, the same method the
		// Create Lesson page uses when Save is pressed

		// This is a properly written positive test
		// It tests that a valid lesson is saved and appears in the contributor's list
		performCreateLessonTestCase("TC1", VALID_TITLE, VALID_DESCRIPTION, VALID_CATEGORY,
				VALID_EFFORT, true, "");

		// This is a properly written negative test
		// It tests that a blank title is rejected
		performCreateLessonTestCase("TC2", "", VALID_DESCRIPTION, VALID_CATEGORY,
				VALID_EFFORT, false, LessonsEvaluator.TITLE_ERROR);

		// This is a properly written negative test
		// It tests that a title of 4 characters (one below the minimum) is rejected
		performCreateLessonTestCase("TC3", "abcd", VALID_DESCRIPTION, VALID_CATEGORY,
				VALID_EFFORT, false, LessonsEvaluator.TITLE_ERROR);

		// This is a properly written negative test
		// It tests that a title of 101 characters (one above the maximum) is rejected
		performCreateLessonTestCase("TC4", "T".repeat(101), VALID_DESCRIPTION, VALID_CATEGORY,
				VALID_EFFORT, false, LessonsEvaluator.TITLE_ERROR);

		// This is a properly written negative test
		// It tests that a negative number of hours is rejected
		performCreateLessonTestCase("TC5", VALID_TITLE, VALID_DESCRIPTION, VALID_CATEGORY,
				"-3", false, LessonsEvaluator.EFFORT_ERROR);

		// This is a properly written negative test
		// It tests that a non-numeric number of hours is rejected
		performCreateLessonTestCase("TC6", VALID_TITLE, VALID_DESCRIPTION, VALID_CATEGORY,
				"ABC", false, LessonsEvaluator.EFFORT_ERROR);

		System.out.println("/**************************************************************/");
		System.out.println("/************** Start of the view lessons test cases **********/");
		System.out.println("/**************************************************************/");

		// This is a properly written positive test
		// It tests that a saved lesson is still there after the database is closed and reopened
		performPersistenceTestCase("TC7");

		// This is a properly written positive test
		// It tests that a contributor with no lessons gets an empty list
		performEmptyListTestCase("TC8");

		// This is a properly written positive test
		// It tests that contributor 2 sees only contributor 2's lessons
		performSeparateListsTestCase("TC9");

		// This is a properly written negative test
		// It tests that View Lesson with no lesson selected is rejected
		performSelectionTestCase("TC10", "View Lesson pressed with no lesson selected",
				LessonsEvaluator.evaluateViewSelection(null),
				LessonsEvaluator.VIEW_NOT_SELECTED_ERROR);

		System.out.println("/**************************************************************/");
		System.out.println("/************** Start of the delete selection test case *******/");
		System.out.println("/**************************************************************/");

		// This is a properly written negative test
		// It tests that Delete Lesson with no lesson selected is rejected
		performSelectionTestCase("TC11", "Delete Lesson pressed with no lesson selected",
				LessonsEvaluator.evaluateDeleteSelection(null),
				LessonsEvaluator.DELETE_NOT_SELECTED_ERROR);

		System.out.println("/**************************************************************/");
		System.out.println("/************** Start of the experience test cases ************/");
		System.out.println("/**************************************************************/");

		// This is a properly written negative test
		// It tests that an experience entry with every field blank is rejected, with a message
		// for each required field
		performBlankExperienceTestCase("TC12");

		System.out.println("/**************************************************************/");
		System.out.println("/************** Start of the delete lesson test cases *********/");
		System.out.println("/**************************************************************/");

		// This is a properly written positive test
		// It tests that a confirmed delete removes the lesson from the database and the list
		performDeleteTestCase("TC13", true);

		// This is a properly written positive test
		// It tests that a canceled delete leaves the lesson in the database and the list
		performDeleteTestCase("TC14", false);

		/************** End of the test cases **************/
		try {
			removeTestLessons();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		theDatabase.closeConnection();

		/************** Test cases semi-automation report footer **************/
		System.out.println("____________________________________________________________________________");
		System.out.println();
		System.out.println("Number of tests passed: "+ numPassed);
		System.out.println("Number of tests failed: "+ numFailed);
	}


	/*-*******************************************************************************************

	Test case methods

	Each method sets up the input for one kind of test, displays test execution information,
	invokes precisely the same evaluator and database methods that the JavaFX pages use,
	interprets the returned value, and displays the interpreted result.

	 */

	/**
	 * Create a lesson (TC1 - TC6). The input is validated exactly as the Create Lesson page does
	 * when Save is pressed. When it is valid, the lesson is stored for
	 * {@link #TEST_CONTRIBUTOR_1} and the test also checks that it appears in that contributor's
	 * list.
	 *
	 * @param testCase        the test case id shown in the report, such as "TC1"
	 * @param title           the lesson title to validate
	 * @param description     the lesson description to validate
	 * @param category        the lesson category to validate
	 * @param effortText      the number of hours, as the user would type it
	 * @param expectedPass    true if the lesson is expected to be accepted, false if rejected
	 * @param expectedMessage the error message a rejection must contain (ignored when
	 *                        {@code expectedPass} is true)
	 */
	private static void performCreateLessonTestCase(String testCase, String title,
			String description, String category, String effortText, boolean expectedPass,
			String expectedMessage) {
		String input = "title=\"" + title + "\", category=\"" + category + "\", effort=\""
				+ effortText + "\"";
		displayHeader(testCase, input);

		String resultText = LessonsEvaluator.evaluateLesson(title, description, category,
				effortText);
		if (resultText.isEmpty()) {
			try {
				LessonRecord saved = theDatabase.createLesson(title.trim(), description.trim(),
						category, Float.parseFloat(effortText.trim()), TEST_CONTRIBUTOR_1);
				if (!isInList(TEST_CONTRIBUTOR_1, saved.getId()))
					resultText = "The lesson was saved but does not appear in the user's list.";
				else
					System.out.println("Lesson #" + saved.getId() + " appears in "
							+ TEST_CONTRIBUTOR_1 + "'s list.");
			} catch (SQLException e) {
				resultText = "Database error: " + e.getMessage();
			}
		}
		errorMessage(resultText, expectedPass, input, expectedMessage);
	}

	/**
	 * Lesson data persists after a reset (TC7). A lesson is saved, the database connection is
	 * closed as it is when the application quits, a new connection is opened as it is when the
	 * application starts, and the lesson must still be in the contributor's list with the same
	 * title. This method replaces {@link #theDatabase} with the new connection.
	 *
	 * @param testCase the test case id shown in the report
	 */
	private static void performPersistenceTestCase(String testCase) {
		String input = "Save a lesson, close the database, reopen it, and look for the lesson";
		displayHeader(testCase, input);

		String resultText = "";
		try {
			LessonRecord saved = theDatabase.createLesson("Lesson that must persist",
					VALID_DESCRIPTION, VALID_CATEGORY, 1.0f, TEST_CONTRIBUTOR_1);

			theDatabase.closeConnection();						// "Exit the app"
			theDatabase = new Database(TEST_DB_URL);			// "Restart the app"
			theDatabase.connectToDatabase();

			LessonRecord reloaded = theDatabase.getLesson(saved.getId());
			if (reloaded == null || !isInList(TEST_CONTRIBUTOR_1, saved.getId()))
				resultText = "The lesson was not found after the database was reopened.";
			else if (!reloaded.getTitle().equals(saved.getTitle()))
				resultText = "The lesson was found but its data changed.";
			else
				System.out.println("Lesson #" + saved.getId() + " \"" + reloaded.getTitle()
						+ "\" was found after the restart.");
		} catch (SQLException e) {
			resultText = "Database error: " + e.getMessage();
		}
		errorMessage(resultText, true, input, "");
	}

	/**
	 * Empty list (TC8). A contributor who has created no lessons gets an empty list, which is
	 * what makes the My Lessons page show the empty-list message in place of the table rows.
	 * The test uses {@link #TEST_CONTRIBUTOR_3}, who never creates a lesson.
	 *
	 * @param testCase the test case id shown in the report
	 */
	private static void performEmptyListTestCase(String testCase) {
		String input = TEST_CONTRIBUTOR_3 + " has created no lessons and opens My Lessons";
		displayHeader(testCase, input);

		String resultText = "";
		try {
			LessonsLearnedManager lessons = theDatabase.getLessonsByCreator(TEST_CONTRIBUTOR_3);
			if (!lessons.isEmpty())
				resultText = "The list holds " + lessons.size() + " lesson(s); it should be empty.";
			else
				System.out.println("The list is empty, so the page shows: \""
						+ LessonsEvaluator.EMPTY_LIST_MESSAGE + "\"");
		} catch (SQLException e) {
			resultText = "Database error: " + e.getMessage();
		}
		errorMessage(resultText, true, input, "");
	}

	/**
	 * Each contributor sees only their own lessons (TC9). {@link #TEST_CONTRIBUTOR_2} creates a
	 * lesson; their list must hold it and must not hold any lesson created by another
	 * contributor, such as {@link #TEST_CONTRIBUTOR_1}.
	 *
	 * @param testCase the test case id shown in the report
	 */
	private static void performSeparateListsTestCase(String testCase) {
		String input = TEST_CONTRIBUTOR_2 + " creates a lesson and opens My Lessons";
		displayHeader(testCase, input);

		String resultText = "";
		try {
			LessonRecord saved = theDatabase.createLesson("Contributor two's lesson",
					VALID_DESCRIPTION, "Teamwork", 1.5f, TEST_CONTRIBUTOR_2);
			LessonsLearnedManager list = theDatabase.getLessonsByCreator(TEST_CONTRIBUTOR_2);

			if (!isInList(TEST_CONTRIBUTOR_2, saved.getId()))
				resultText = "Contributor 2's own lesson is missing from their list.";
			for (LessonRecord lesson : list.getLessons())
				if (!lesson.getCreatorUsername().equals(TEST_CONTRIBUTOR_2))
					resultText = "Contributor 2's list holds lesson #" + lesson.getId()
							+ " created by " + lesson.getCreatorUsername() + ".";
			if (resultText.isEmpty())
				System.out.println(TEST_CONTRIBUTOR_2 + "'s list holds " + list.size()
						+ " lesson(s), none of them created by " + TEST_CONTRIBUTOR_1 + ".");
		} catch (SQLException e) {
			resultText = "Database error: " + e.getMessage();
		}
		errorMessage(resultText, true, input, "");
	}

	/**
	 * A button that acts on the selected lesson is pressed with nothing selected (TC10, TC11).
	 * The caller passes the result of the selection evaluator the page uses, and the input is
	 * expected to be rejected with {@code expectedMessage}.
	 *
	 * @param testCase        the test case id shown in the report
	 * @param input           a description of the action, shown in the report
	 * @param resultText      the text returned by the selection evaluator (empty means accepted)
	 * @param expectedMessage the error message the evaluator must return
	 */
	private static void performSelectionTestCase(String testCase, String input, String resultText,
			String expectedMessage) {
		displayHeader(testCase, input);
		errorMessage(resultText, false, input, expectedMessage);
	}

	/*
	 * The two test methods below are currently disabled: their test cases (alter a locked field,
	 * and delete a lesson with a locked field) are not run by main. They are kept in a plain
	 * block comment, rather than a Javadoc comment, so the Javadoc tool does not attach them to
	 * the next method.
	 *
	 * Alter a locked field. A lesson is created and its title is locked, as a curator would; then
	 * a save with a changed title is checked exactly as the Lesson Details page checks it.
	 * Returns the locked lesson so the delete test can try to delete it.
	 *
	private static LessonRecord performLockedFieldTestCase(String testCase) {
		String input = "Change the title of a lesson whose title is locked";
		displayHeader(testCase, input);

		String resultText;
		LessonRecord lesson = null;
		try {
			LessonRecord saved = theDatabase.createLesson("Lesson with a locked title",
					VALID_DESCRIPTION, VALID_CATEGORY, 2.0f, TEST_CONTRIBUTOR_1);
			theDatabase.setLessonFieldLock(saved.getId(), LessonRecord.FIELD_TITLE, true);
			lesson = theDatabase.getLesson(saved.getId());

			resultText = LessonsEvaluator.evaluateLockedFields(lesson, "A different title",
					lesson.getDescription(), lesson.getCategory(),
					String.valueOf(lesson.getDurationHours()));
		} catch (SQLException e) {
			resultText = "Database error: " + e.getMessage();
		}
		errorMessage(resultText, false, input, LessonsEvaluator.LOCKED_FIELD_ERROR);
		return lesson;
	}

	 * Delete a lesson with a locked field. The lesson locked above is checked exactly as the
	 * delete buttons check it, using the lesson as it is in the database now.
	 *
	private static void performDeleteLockedTestCase(String testCase, LessonRecord lockedLesson) {
		String input = "Delete a lesson that has a locked field";
		displayHeader(testCase, input);

		String resultText;
		if (lockedLesson == null) {
			resultText = "The locked-field test did not create the locked lesson, so this test could not run.";
			errorMessage(resultText, true, input, "");
			return;
		}
		try {
			resultText = LessonsEvaluator.evaluateCanDelete(
					theDatabase.getLesson(lockedLesson.getId()), TEST_CONTRIBUTOR_1);
		} catch (SQLException e) {
			resultText = "Database error: " + e.getMessage();
		}
		errorMessage(resultText, false, input, LessonsEvaluator.DELETE_BLOCKED_ERROR);
	}
	 */

	/**
	 * Save an experience entry with every field blank (TC12). The entry and the teammate row are
	 * checked exactly as the Lesson Experience page checks them when Save is pressed, and every
	 * required field must be reported: what was done, how it was done, the duration, the
	 * teammate name, and the effort entry.
	 *
	 * @param testCase the test case id shown in the report
	 */
	private static void performBlankExperienceTestCase(String testCase) {
		String input = "what=\"\", how=\"\", duration=\"\", teammate=\"\", effort entry=\"\"";
		displayHeader(testCase, input);

		String resultText = LessonsEvaluator.evaluateExperience("", "", "");
		String teammateErrors = LessonsEvaluator.evaluateTeammateEffort("", "");
		if (!teammateErrors.isEmpty())
			resultText = resultText.isEmpty() ? teammateErrors : resultText + "\n" + teammateErrors;

		String expectedMessage = LessonsEvaluator.WHAT_WAS_DONE_ERROR + "\n"
				+ LessonsEvaluator.HOW_IT_WAS_DONE_ERROR + "\n"
				+ LessonsEvaluator.DURATION_ERROR + "\n"
				+ LessonsEvaluator.TEAMMATE_NAME_ERROR + "\n"
				+ LessonsEvaluator.EFFORT_ENTRY_ERROR;
		errorMessage(resultText, false, input, expectedMessage);
	}

	/**
	 * Delete a lesson (TC13), or cancel the delete (TC14). The lesson must first pass the same
	 * check the delete buttons run. The Yes/No dialog itself is JavaFX, so the contributor's
	 * answer is passed in: on "Yes" the lesson is deleted as the page does, and on "No" the page
	 * returns without deleting. The lesson must then be gone from, or still in, both the
	 * database and the contributor's list.
	 *
	 * @param testCase  the test case id shown in the report
	 * @param answerYes true to simulate answering "Yes" to the confirmation, false for "No"
	 */
	private static void performDeleteTestCase(String testCase, boolean answerYes) {
		String input = "Delete a lesson and answer \"" + (answerYes ? "Yes" : "No")
				+ "\" when asked to confirm";
		displayHeader(testCase, input);

		String resultText = "";
		try {
			LessonRecord saved = theDatabase.createLesson(answerYes ? "Lesson to be deleted"
					: "Lesson that is kept", VALID_DESCRIPTION, VALID_CATEGORY, 1.0f,
					TEST_CONTRIBUTOR_1);
			resultText = LessonsEvaluator.evaluateCanDelete(theDatabase.getLesson(saved.getId()),
					TEST_CONTRIBUTOR_1);

			if (resultText.isEmpty()) {
				if (answerYes) theDatabase.deleteLesson(saved.getId());

				boolean inDatabase = theDatabase.getLesson(saved.getId()) != null;
				boolean inList = isInList(TEST_CONTRIBUTOR_1, saved.getId());
				if (answerYes && (inDatabase || inList))
					resultText = "The lesson is still in the " + (inDatabase ? "database" : "list")
							+ " after the delete was confirmed.";
				else if (!answerYes && (!inDatabase || !inList))
					resultText = "The lesson is missing from the " + (inDatabase ? "list" : "database")
							+ " after the delete was canceled.";
				else
					System.out.println("Lesson #" + saved.getId() + (answerYes
							? " was removed from the database and the list."
							: " is still in the database and the list."));
			}
		} catch (SQLException e) {
			resultText = "Database error: " + e.getMessage();
		}
		errorMessage(resultText, true, input, "");
	}


	/*-*******************************************************************************************

	Helpers

	 */

	/**
	 * Display the header for one test case: its id and a description of its input.
	 *
	 * @param testCase  the test case id, such as "TC1"
	 * @param inputText a description of the input, shown after "Input:"
	 */
	private static void displayHeader(String testCase, String inputText) {
		System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
		System.out.println("Input: " + inputText);
		System.out.println("______________\n");
	}

	/**
	 * Interpret the result of a test case, display the outcome, and update the pass/fail
	 * counters. It takes the result text from the code under test (empty means the input was
	 * accepted), whether the test case expected the input to be accepted, the input, and the
	 * message(s) a rejection must contain, one per line. A rejection with the wrong message is a
	 * failure, because the user would be shown an unhelpful message.
	 *
	 * @param resultText      the text returned by the code under test; empty if the input was
	 *                        accepted, otherwise the error message
	 * @param expectedPass    true if the test case expected the input to be accepted
	 * @param inputText       a description of the input, shown in the report
	 * @param expectedMessage the message(s) a rejection must contain, one per line
	 */
	private static void errorMessage(String resultText, boolean expectedPass, String inputText,
			String expectedMessage) {
		// isEmpty() is used rather than != "" because != compares object references, and an
		// empty String built by a StringBuilder is not the same object as the literal ""
		if (!resultText.isEmpty()) {
			 // If the test case expected the test to pass then this is a failure
			if (expectedPass) {
				System.out.println(failure("The input <" + inputText + "> was rejected." +
						"\nBut it was supposed to be accepted, so this is a failure!\n"));
				System.out.println("Error message: " + resultText);
				numFailed++;
			}
			// If the test case expected the test to fail, it must also show the expected message
			else if (containsEveryLine(resultText, expectedMessage)) {
				System.out.println(success("The input <" + inputText + "> was rejected." +
						"\nIt was supposed to be rejected with this message, so this is a pass!\n"));
				System.out.println("Error message: " + resultText);
				numPassed++;
			}
			else {
				System.out.println(failure("The input <" + inputText + "> was rejected," +
						"\nbut not with the expected message, so this is a failure!\n"));
				System.out.println("Error message:    " + resultText);
				System.out.println("Expected message: " + expectedMessage);
				numFailed++;
			}
		}

		// If the resulting text is empty, the input was accepted
		else {
			// If the test case expected the test to pass then this is a success
			if (expectedPass) {
				System.out.println(success("The input <" + inputText +
						"> was accepted, so this is a pass!"));
				numPassed++;
			}
			// If the test case expected the test to fail then this is a failure
			else {
				System.out.println(failure("The input <" + inputText +
						"> was accepted" +
						"\nBut it was supposed to be rejected, so this is a failure!"));
				numFailed++;
			}
		}
	}

	/**
	 * Check that a result contains every line of the expected message.
	 *
	 * @param resultText      the text returned by the code under test
	 * @param expectedMessage the expected message, one required line per line of text
	 * @return true if every line of {@code expectedMessage} appears somewhere in
	 *         {@code resultText}, false otherwise
	 */
	private static boolean containsEveryLine(String resultText, String expectedMessage) {
		for (String line : expectedMessage.split("\n"))
			if (!resultText.contains(line)) return false;
		return true;
	}

	/**
	 * Check whether a lesson is in a contributor's list, as the My Lessons page loads it.
	 *
	 * @param username the contributor whose list is loaded
	 * @param lessonId the id of the lesson to look for
	 * @return true if the lesson is in the contributor's list, false otherwise
	 * @throws SQLException if the list cannot be read from the database
	 */
	private static boolean isInList(String username, int lessonId) throws SQLException {
		for (LessonRecord lesson : theDatabase.getLessonsByCreator(username).getLessons())
			if (lesson.getId() == lessonId) return true;
		return false;
	}

	/**
	 * Remove every lesson belonging to the test contributors, so each run starts from the same
	 * state (in particular, TC8 needs {@link #TEST_CONTRIBUTOR_3} to have no lessons). Their
	 * experience entries are removed with them by the database's ON DELETE CASCADE rules.
	 *
	 * @throws SQLException if a lesson cannot be read or deleted
	 */
	private static void removeTestLessons() throws SQLException {
		for (String username : new String[] {TEST_CONTRIBUTOR_1, TEST_CONTRIBUTOR_2,
				TEST_CONTRIBUTOR_3})
			for (LessonRecord lesson : theDatabase.getLessonsByCreator(username).getLessons())
				theDatabase.deleteLesson(lesson.getId());
	}

	/**
	 * Wrap a message in the green success prefix and the color reset code.
	 *
	 * @param s the message to display
	 * @return the message with the success prefix and color reset applied
	 */
	private static String success(String s) {
        return SUCCESS + s + RESET;
    }

	/**
	 * Wrap a message in the red failure prefix and the color reset code.
	 *
	 * @param s the message to display
	 * @return the message with the failure prefix and color reset applied
	 */
    private static String failure(String s) {
        return FAILURE + s + RESET;
    }

}