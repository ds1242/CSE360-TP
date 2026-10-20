package lessonsLearnedTestbedMain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import cse360.GRP.ADES.evaluator.textLengthEvaluation.TextLengthEvaluator;
import database.Database;
import entityClasses.LessonsLearned;

/*******
 * <p> Title: LessonsLearnedTestingAutomation Class. </p>
 *
 * <p> Description: A Java demonstration for semi-automated tests of the lessons learned
 * subsystem.  The first group of test cases exercises the input validation that limits how much
 * text a Contributor may enter.  The second group exercises the Create, Read, Update, and Delete
 * operations against a database.
 *
 * The database test cases run against an in-memory H2 database created here and discarded when
 * the program ends.  They never touch the FoundationDatabase the application keeps in the user's
 * home directory, so running the tests cannot disturb real lessons. </p>
 *
 * @author Edis Jakupovic
 *
 */
public class LessonsLearnedTestingAutomation {

    static String RED = "\u001B[31m";
    static String GREEN = "\u001B[32m";
    static String RESET = "\u001B[0m";

    static String SUCCESS = GREEN + "***Success***";
    static String FAILURE = RED + "***Failure***";

	static int numPassed = 0;	// Counter of the number of passed tests
	static int numFailed = 0;	// Counter of the number of failed tests
	static int count = 0;

	// The lengths the GUI enforces as the Contributor types.  A title may not be left empty, so
	// its minimum is one character.  The body has no minimum of its own, because an empty body
	// is refused by the Controller with a message of its own.
	static final int MIN_TITLE_LENGTH = 1;
	static final int MAX_TITLE_LENGTH = TextLengthEvaluator.MAX_TEXT_LENGTH;
	static final int MIN_BODY_LENGTH = 0;
	static final int MAX_BODY_LENGTH = 1000;

	// The database under test and the connection it was built on
	static Database theDatabase;
	static Connection theConnection;

	/*
	 * This mainline displays a header to the console, performs a sequence of
	 * test cases, and then displays a footer with a summary of the results
	 */
	public static void main(String[] args) {
		/************** Test cases semi-automation report header **************/
		System.out.println("______________________________________");
		System.out.println("\nTesting Automation");
		System.out.println("/**************************************************************/");
		System.out.println("/************** Start of the title length test cases **********/");
		System.out.println("/**************************************************************/");
		// The performTextLengthTestCase methods use the evaluateText method in the
		// TextLengthEvaluator class to evaluate the test cases

		// This is a properly written positive test
		// It tests that an ordinary title is accepted
		performTextLengthTestCase(count++, "ordinary title", MIN_TITLE_LENGTH, MAX_TITLE_LENGTH, true);

		// This is a properly written positive test
		// It tests the boundary, a title of exactly the maximum length
		performTextLengthTestCase(count++, repeat("x", MAX_TITLE_LENGTH), MIN_TITLE_LENGTH,
				MAX_TITLE_LENGTH, true);

		// This is a properly written positive test
		// It tests the other boundary, a title of exactly the minimum length
		performTextLengthTestCase(count++, "x", MIN_TITLE_LENGTH, MAX_TITLE_LENGTH, true);

		// This is a properly written negative test
		// It tests the boundary, one character beyond the maximum length
		performTextLengthTestCase(count++, repeat("x", MAX_TITLE_LENGTH + 1), MIN_TITLE_LENGTH,
				MAX_TITLE_LENGTH, false);

		// This is a properly written negative test
		// It tests that an empty title is refused
		performTextLengthTestCase(count++, "", MIN_TITLE_LENGTH, MAX_TITLE_LENGTH, false);

		// This is a properly written negative test
		// It tests that a title of nothing but spaces is refused, because the text is trimmed
		// before it is measured
		performTextLengthTestCase(count++, "     ", MIN_TITLE_LENGTH, MAX_TITLE_LENGTH, false);

		System.out.println("/**************************************************************/");
		System.out.println("/************** Start of the body length test cases ***********/");
		System.out.println("/**************************************************************/");
		// The core information and the experience are both limited to 1000 characters, which is a
		// different maximum from the one every single line field in this application uses

		// This is a properly written positive test
		// It tests that a paragraph of core information is accepted
		performTextLengthTestCase(count++, "This is core information", MIN_BODY_LENGTH,
				MAX_BODY_LENGTH, true);

		// This is a properly written positive test
		// It tests the boundary, a body of exactly the maximum length
		performTextLengthTestCase(count++, repeat("x", MAX_BODY_LENGTH), MIN_BODY_LENGTH,
				MAX_BODY_LENGTH, true);

		// This is a properly written negative test
		// It tests the boundary, one character beyond the maximum length
		performTextLengthTestCase(count++, repeat("x", MAX_BODY_LENGTH + 1), MIN_BODY_LENGTH,
				MAX_BODY_LENGTH, false);

		System.out.println("/**************************************************************/");
		System.out.println("/************** Start of the database test cases **************/");
		System.out.println("/**************************************************************/");
		// The database test cases run against an in-memory database created here

		if (!setUpDatabase()) {
			System.out.println(failure("The in-memory database could not be created, so the "
					+ "database test cases were not run.\n"));
			numFailed++;
		} else {
			performDatabaseTestCases();
		}

		/************** End of the test cases **************/

		/************** Test cases semi-automation report footer **************/
		System.out.println("____________________________________________________________________________");
		System.out.println();
		System.out.println("Number of tests passed: " + numPassed);
		System.out.println("Number of tests failed: " + numFailed);
	}

	/*
	 * This method establishes an in-memory H2 database and hands its connection to the Database
	 * class, so the tests exercise the very same methods the application uses.  DB_CLOSE_DELAY=-1
	 * keeps the database alive for the whole run rather than discarding it as soon as the first
	 * statement finishes.
	 */
	private static boolean setUpDatabase() {
		String jdbcUrl = "jdbc:h2:mem:lessonsLearnedTestDB;DB_CLOSE_DELAY=-1";
		try {
			theConnection = DriverManager.getConnection(jdbcUrl, "sa", "");
			theDatabase = new Database(theConnection);
			return true;
		} catch (SQLException e) {
			System.out.println("Unable to create the in-memory database: " + e.getMessage());
			return false;
		}
	}

	/*
	 * This method performs the Create, Read, Update, and Delete test cases in the order a
	 * Contributor would perform them, because each operation needs the lessons the previous ones
	 * stored.
	 */
	private static void performDatabaseTestCases() {
		String me = "user1";

		// Create: a valid lesson is stored
		performDatabaseTestCase(count++, "Add a new lesson",
				theDatabase.addLesson(new LessonsLearned(me,
						"This is core information", "",
						"lessonTitle1")), true);

		// Create: two more lessons, so there is a list to examine later
		performDatabaseTestCase(count++, "Add a second lesson",
				theDatabase.addLesson(new LessonsLearned(me,
						"This is core information", "This is some experience text",
						"lessonTitle2")), true);
		performDatabaseTestCase(count++, "Add a third lesson",
				theDatabase.addLesson(new LessonsLearned(me,
						"This is core information", "",
						"lessonTitle3")), true);

		// Create: the same contributor cannot reuse a title
		performDatabaseTestCase(count++, "Add a duplicate title for the same contributor",
				theDatabase.addLesson(new LessonsLearned(me, "Different body.", "",
						"lessonTitle1")), false);

		// Create: a lesson with no title is refused, because a contributor and a title together
		// identify a lesson, and one with no title could never be found again
		performDatabaseTestCase(count++, "Add a lesson with an empty title",
				theDatabase.addLesson(new LessonsLearned(me, "A body with no title.", "", "")),
				false);

		// Create: a title of nothing but spaces is refused for the same reason
		performDatabaseTestCase(count++, "Add a lesson whose title is only spaces",
				theDatabase.addLesson(new LessonsLearned(me, "A body with no title.", "", "   ")),
				false);

		// Read: a stored lesson reads back exactly as it was entered
		LessonsLearned stored = theDatabase.getLesson(me, "lessonTitle2");
		performDatabaseTestCase(count++, "Read a lesson back field for field",
				stored != null
						&& stored.getTitle().equals("lessonTitle2")
						&& stored.getContributorUserName().equals(me)
						&& stored.getCoreInformation().equals("This is core information")
						&& stored.getExperience().equals("This is some experience text"), true);

		// Read: a title no lesson uses
		performDatabaseTestCase(count++, "Read a lesson that does not exist",
				theDatabase.getLesson(me, "lessonTitle4") != null, false);

		// Read: one contributor cannot reach another contributor's lesson
		performDatabaseTestCase(count++, "Read another contributor's lesson",
				theDatabase.getLesson("user2", "lessonTitle2") != null, false);

		// Read: the list holds every lesson this contributor added and none of anyone else's
		List<LessonsLearned> myLessons = theDatabase.getAllLessons(me);
		System.out.println("\nThe lessons stored for " + me + ":");
		for (LessonsLearned lesson : myLessons)
			System.out.printf("    Contributor: %s  Title: %s\n",
					lesson.getContributorUserName(), lesson.getTitle());
		performDatabaseTestCase(count++, "List the lessons of one contributor",
				myLessons.size() == 3, true);

		// Read: a contributor with no lessons receives an empty list rather than null
		List<LessonsLearned> noLessons = theDatabase.getAllLessons("nobody");
		performDatabaseTestCase(count++, "List the lessons of a contributor who has none",
				noLessons != null && noLessons.isEmpty(), true);

		// Update: the core information and the experience of a stored lesson are replaced
		performDatabaseTestCase(count++, "Update a lesson",
				theDatabase.updateLesson("lessonTitle2",
						new LessonsLearned(me, "This is core information", "This is some experience text",
								"lessonTitle2")), true);

		LessonsLearned updated = theDatabase.getLesson(me, "lessonTitle2");
		performDatabaseTestCase(count++, "Read back the updated lesson",
				updated != null
						&& updated.getExperience().equals("This is some experience text"), true);

		// Update: an update may not empty the title
		performDatabaseTestCase(count++, "Update a lesson to have an empty title",
				theDatabase.updateLesson("lessonTitle2",
						new LessonsLearned(me, "Body.", "", "")), false);

		// Update: a title that is not stored
		performDatabaseTestCase(count++, "Update a lesson that does not exist",
				theDatabase.updateLesson("lessonTitle4",
						new LessonsLearned(me, "Body.", "", "lessonTitle4")), false);

		// Update: a lesson belonging to someone else
		performDatabaseTestCase(count++, "Update another contributor's lesson",
				theDatabase.updateLesson("lessonTitle2",
						new LessonsLearned("user2", "Body.", "", "lessonTitle2")),
				false);

		// Delete: a lesson this contributor owns
		performDatabaseTestCase(count++, "Delete a lesson",
				theDatabase.deleteLesson(me, "lessonTitle3"), true);

		// Delete: the same lesson a second time
		performDatabaseTestCase(count++, "Delete the same lesson again",
				theDatabase.deleteLesson(me, "lessonTitle3"), false);

		// Delete: a lesson belonging to someone else
		performDatabaseTestCase(count++, "Delete another contributor's lesson",
				theDatabase.deleteLesson("user2", "lessonTitle1"), false);

		// Read: the list is one shorter than it was before the delete
		performDatabaseTestCase(count++, "List the lessons after the delete",
				theDatabase.getAllLessons(me).size() == 2, true);

		theDatabase.closeConnection();
	}

	/*
	 * This method sets up the input value for the test from the input parameters,
	 * displays test execution information, invokes precisely the same evaluator
	 * that the interactive JavaFX mainline uses, interprets the returned value,
	 * and displays the interpreted result.
	 */
	private static void performTextLengthTestCase(int testCase, String inputText, int minLength,
			int maxLength, boolean expectedPass) {

		/************** Display an individual test case header **************/
		System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
		System.out.println("Input: \"" + abbreviate(inputText) + "\"");
		System.out.println("Input length: " + inputText.length() + "   Allowed: " + minLength
				+ " to " + maxLength);
		System.out.println("______________");

		/************** Call the evaluator to process the input **************/
		String resultText = TextLengthEvaluator.evaluateText(inputText, minLength, maxLength);

		/************** Interpret the result and display that interpreted information **************/
		System.out.println();
		errorMessage(resultText, expectedPass, abbreviate(inputText));
	}

	/*
	 * This method reports the outcome of one database operation.  A database operation answers
	 * with a value rather than with an error message, so the value it returned is compared with
	 * the value the test case expects instead of being passed through the error message method.
	 */
	private static void performDatabaseTestCase(int testCase, String description, boolean actual, boolean expected) {

		/************** Display an individual test case header **************/
		System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
		System.out.println("Operation: " + description);
		System.out.println("Expected result: " + expected + "   Actual result: " + actual);
		System.out.println("______________");
		System.out.println();

		if (actual == expected) {
			System.out.println(success("The operation <" + description + "> answered " + actual
					+ ", which is what was expected, so this is a pass!\n"));
			numPassed++;
		} else {
			System.out.println(failure("The operation <" + description + "> answered " + actual
					+ ".\nBut it was supposed to answer " + expected
					+ ", so this is a failure!\n"));
			numFailed++;
		}
	}

	/*
	 * This method sets up the error message for the test cases, and
	 * displays test execution information, the method will take in
	 * the result text from the validator methods, the expected pass
	 * from the test case, and the test case string.  It then proceeds
	 * to match the three arguments to make output the correct message.
	 */
	private static void errorMessage(String resultText, boolean expectedPass, String inputText) {
		// If the resulting text is not empty, the evaluator rejected the input
		if (resultText != "") {
			// If the test case expected the test to pass then this is a failure
			if (expectedPass) {
				System.out.println(failure("The input <" + inputText + "> is invalid." +
						"\nBut it was supposed to be valid, so this is a failure!\n"));
				System.out.println("Error message: " + resultText);
				numFailed++;
			}
			// If the test case expected the test to fail then this is a success
			else {
				System.out.println(success("The input <" + inputText + "> is invalid." +
						"\nBut it was supposed to be invalid, so this is a pass!\n"));
				System.out.println("Error message: " + resultText);
				numPassed++;
			}
		}

		// If the resulting text is empty, the evaluator accepted the input
		else {
			// If the test case expected the test to pass then this is a success
			if (expectedPass) {
				System.out.println(success("The input <" + inputText +
						"> is valid, so this is a pass!"));
				numPassed++;
			}
			// If the test case expected the test to fail then this is a failure
			else {
				System.out.println(failure("The input <" + inputText +
						"> was judged as valid" +
						"\nBut it was supposed to be invalid, so this is a failure!"));
				numFailed++;
			}
		}
	}

	/*
	 * shortens the very long inputs
     * (so a thousand character input does not fill the console)
	 */
	private static String abbreviate(String s) {
		if (s.length() <= 40) return s;
		return s.substring(0, 37) + "...";
	}

	private static String repeat(String s, int times) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < times; i++) sb.append(s);
		return sb.toString();
	}

    private static String success(String s) {
        return SUCCESS + s + RESET;
    }

    private static String failure(String s) {
        return FAILURE + s + RESET;
    }

}
