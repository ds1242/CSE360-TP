package lessons.guiContributorLessons;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import applicationMain.FoundationsMain;
import database.Database;
import entityClasses.LessonRecord;
import entityClasses.LessonsLearnedList;
import entityClasses.User;

/**********
 * <p> Class: ModelContributorLessonsTest </p>
 *
 * <p> Description: JUnit 5 tests for ModelContributorLessons. Each test runs against a
 * fresh in-memory H2 database seeded with three users (Bob, Alice, Jerry) and five lessons
 * (three created by Alice, two by Bob, none by Jerry).</p>
 *
 */
class ModelContributorLessonsTest {

	/** ANSI escape code that switches console text to green. */
	static String GREEN = "\u001B[32m";

	/** ANSI escape code that resets console text to its default color. */
	static String RESET = "\u001B[0m";

	/** Green "PASSED" label printed at the end of each successful test. */
	static String PASSED = GREEN + "PASSED" + RESET;

	/** JDBC connection to the in-memory H2 test database; closed in {@link #tearDown()}. */
	private Connection connection;

	/** The Database instance backed by {@link #connection} and used by every test. */
	private Database db;

	/** The application's real Database reference, saved so it can be restored after each test. */
	private Database originalDatabase;

	/** Lesson created by Alice (seed data). */
	private LessonRecord aliceLesson1;

	/** Second lesson created by Alice (seed data). */
	private LessonRecord aliceLesson2;

	/** Third lesson created by Alice (seed data). */
	private LessonRecord aliceLesson3;

	/** First lesson created by Bob (seed data). */
	private LessonRecord bobLesson1;

	/** Second lesson created by Bob (seed data). */
	private LessonRecord bobLesson2;

	/**********
	 * <p> Method: injectDb(Class, Database) </p>
	 *
	 * <p> Description: Uses reflection to replace the private static {@code theDatabase} field of
	 * the given class. This is needed because the class under test reads
	 * {@code FoundationsMain.database} once, when the class is first loaded, and would otherwise
	 * keep pointing at a database from an earlier test (whose connection has been closed).</p>
	 *
	 * @param cls	the class whose static {@code theDatabase} field is to be replaced
	 * @param value	the Database instance to store in that field
	 * @throws Exception if the class has no {@code theDatabase} field or it cannot be accessed
	 */
	private static void injectDb(Class<?> cls, Database value) throws Exception {
		var f = cls.getDeclaredField("theDatabase");
		f.setAccessible(true);
		f.set(null, value);
	}

	/**********
	 * <p> Method: setUp() </p>
	 *
	 * <p> Description: Runs before each test. Opens a fresh in-memory H2 database, points the
	 * application and ModelContributorLessons at it, registers the test users, and creates the
	 * test lessons.</p>
	 *
	 * @throws Exception if the database connection, injection, or seeding fails
	 */
	@BeforeEach
	void setUp() throws Exception {
		String jdbcUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL";
		connection = DriverManager.getConnection(jdbcUrl, "sa", "");
		db = new Database(connection);

		originalDatabase = FoundationsMain.database;
		FoundationsMain.database = db;
		injectDb(ModelContributorLessons.class, db);

		// Test users
		db.register(new User("Bob", "Password1!", "Bob", "", "Last", "Bobby",
				"bob@test.com", true, false, false, false));
		db.register(new User("Alice", "Password1!", "Alice", "", "Last", "Alice",
				"alice@test.com", false, true, false, false));
		db.register(new User("Jerry", "Password1!", "Jerry", "", "Last", "Jerry",
				"jerry@test.com", false, true, false, false));

		// Test lessons: Alice has 3, Bob has 2, Jerry has none
		aliceLesson1 = db.createLesson("alice's lesson",  "description of lesson",   "category", 1, "Alice");
		aliceLesson2 = db.createLesson("alice's lesson2", "description of lesson3",  "cat 2",    1, "Alice");
		aliceLesson3 = db.createLesson("alice's lesson3", "description of lesson23", "cat 2",    3, "Alice");
		bobLesson1   = db.createLesson("Bob's lesson1",   "description of lesson",   "cat 2",    1, "Bob");
		bobLesson2   = db.createLesson("Bob's lesson2",   "description of lesson2",  "cat 2",    1, "Bob");
	}

	/**********
	 * <p> Method: tearDown() </p>
	 *
	 * <p> Description: Runs after each test. Restores the application's original Database
	 * reference, drops every object in the in-memory database, and closes the connection so
	 * tests do not affect one another.</p>
	 *
	 * @throws Exception if restoring the reference or closing the database fails
	 */
	@AfterEach
	void tearDown() throws Exception {
		FoundationsMain.database = originalDatabase;
		injectDb(ModelContributorLessons.class, originalDatabase);

		if (connection != null && !connection.isClosed()) {
			try (Statement stmt = connection.createStatement()) {
				stmt.execute("DROP ALL OBJECTS");
			}
			connection.close();
		}
	}

	/**********
	 * <p> Test: testLoadMyLessonsExist() </p>
	 *
	 * <p> Type: Coverage </p>
	 *
	 * <p> Description: loadMyLessons("Alice") should return a non-null list holding exactly the
	 * three lessons Alice created, each with Alice recorded as the creator.</p>
	 */
	@Test
	void testLoadMyLessonsExist() {
		LessonsLearnedList lessonList = new LessonsLearnedList();
		try {
			lessonList = ModelContributorLessons.loadMyLessons("Alice");
		} catch (Exception e) {
			System.out.println("Error: Unable to load lesson list" + e);
		}
		assertNotNull(lessonList, "loadMyLessons should never return null");
		assertEquals(3, lessonList.size(), "Alice should have exactly 3 lessons");
		for (int i = 0; i < lessonList.size(); i++) {
			assertEquals("Alice", lessonList.getLesson(i).getCreatorUsername());
		}
		System.out.println("testLoadMyLessonsExist: " + PASSED);
	}

	/**********
	 * <p> Test: testLoadMyLessonsEmpty() </p>
	 *
	 * <p> Type: Coverage </p>
	 *
	 * <p> Description: loadMyLessons("Jerry") should return an empty (but non-null) list because
	 * Jerry has not created any lessons.</p>
	 */
	@Test
	void testLoadMyLessonsEmpty() {
		LessonsLearnedList lessonList = new LessonsLearnedList();
		try {
			lessonList = ModelContributorLessons.loadMyLessons("Jerry");
		} catch (Exception e) {
			System.out.println("Error: Unable to load lesson list" + e);
		}
		assertNotNull(lessonList, "loadMyLessons should never return null");
		assertEquals(0, lessonList.size(), "Jerry should have no lessons");

		System.out.println("testLoadMyLessonsEmpty: " + PASSED);
	}

	/**********
	 * <p> Test: testDeleteLesson() </p>
	 *
	 * <p> Type: Coverage </p>
	 *
	 * <p> Description: When Alice deletes one of her own lessons, deleteLesson() should return an
	 * empty string, the change should reach the database (Alice drops from 3 lessons to 2), and
	 * Bob's lessons should be untouched.</p>
	 *
	 * @throws Exception if loading lessons from the database fails
	 */
	@Test
	void testDeleteLesson() throws Exception {
		String result = ModelContributorLessons.deleteLesson(aliceLesson1, "Alice");

		assertEquals("", result, "Owner should be able to delete their own lesson");
		assertEquals(2, ModelContributorLessons.loadMyLessons("Alice").size(),
				"The delete should have reached the database");
		assertEquals(2, ModelContributorLessons.loadMyLessons("Bob").size(),
				"Bob's lessons must be untouched");
		System.out.println("testDeleteLesson: " + PASSED);
	}

	/**********
	 * <p> Test: testLoadMyLessonsOnlyReturnsOwnLessons() </p>
	 *
	 * <p> Type: Boundary - in bounds </p>
	 *
	 * <p> Description: The lessons loaded for one contributor must never include another
	 * contributor's lessons. Checks that none of Bob's lesson IDs appear in Alice's list, and
	 * that Bob's list contains only his own two lessons.</p>
	 *
	 * @throws Exception if loading lessons from the database fails
	 */
	@Test
	void testLoadMyLessonsOnlyReturnsOwnLessons() throws Exception {
		LessonsLearnedList aliceList = ModelContributorLessons.loadMyLessons("Alice");
		for (int i = 0; i < aliceList.size(); i++) {
			assertEquals("Alice", aliceList.getLesson(i).getCreatorUsername());
			assertNotEquals(bobLesson1.getId(), aliceList.getLesson(i).getId());
			assertNotEquals(bobLesson2.getId(), aliceList.getLesson(i).getId());
		}

		LessonsLearnedList bobList = ModelContributorLessons.loadMyLessons("Bob");
		assertEquals(2, bobList.size(), "Bob should see only his 2 lessons");
		for (int i = 0; i < bobList.size(); i++) {
			assertEquals("Bob", bobList.getLesson(i).getCreatorUsername());
		}
		System.out.println("testLoadMyLessonsOnlyReturnsOwnLessons: " + PASSED);
	}

	/**********
	 * <p> Test: testCheckCanDeleteOwnLesson() </p>
	 *
	 * <p> Type: Boundary - in bounds </p>
	 *
	 * <p> Description: checkCanDelete() should return an empty string when the logged-in
	 * contributor is the creator of the selected lesson.</p>
	 */
	@Test
	void testCheckCanDeleteOwnLesson() {
		assertEquals("", ModelContributorLessons.checkCanDelete(aliceLesson1, "Alice"));
		System.out.println("testCheckCanDeleteOwnLesson: " + PASSED);
	}

	/**********
	 * <p> Test: testCheckCanDeleteSomeoneElsesLesson() </p>
	 *
	 * <p> Type: Boundary - out of bounds </p>
	 *
	 * <p> Description: A valid user Bob asking to delete a valid lesson that belongs to 
	 * Alice must be refused with a message, and that message must be a permission message
	 * rather than the database error.</p>
	 */
	@Test
	void testCheckCanDeleteSomeoneElsesLesson() {
		User bob = db.getAllUsers().stream()
                .filter(u -> u.getUserName().equals("Bob"))
                .findFirst()
                .orElse(null);
		String msg = ModelContributorLessons.checkCanDelete(aliceLesson1, bob.getUserName());
		assertFalse(msg.isEmpty(), "Bob must not be allowed to delete Alice's lesson");
		assertNotEquals(ModelContributorLessons.DATABASE_ERROR, msg,
				"Should be a permission message, not a DB error");
		System.out.println("testCheckCanDeleteSomeoneElsesLesson: " + PASSED);
	}

	/**********
	 * <p> Test: testDeleteLessonSomeoneElsesLesson() </p>
	 *
	 * <p> Type: Boundary (out of bounds) </p>
	 *
	 * <p> Description: deleteLesson() must refuse to delete a lesson the user did not create, and
	 * the lesson must still exist afterward (Alice still has all three lessons).</p>
	 *
	 * @throws Exception if loading lessons from the database fails
	 */
	@Test
	void testDeleteLessonSomeoneElsesLesson() throws Exception {
		String msg = ModelContributorLessons.deleteLesson(aliceLesson1, "Bob");

		assertFalse(msg.isEmpty(), "Delete of another user's lesson should be rejected");
		assertEquals(3, ModelContributorLessons.loadMyLessons("Alice").size(),
				"Alice's lessons must all still exist");
		System.out.println("testDeleteLessonSomeoneElsesLesson: " + PASSED);
	}

	/**********
	 * <p> Test: testCheckCanDeleteNoSelection() </p>
	 *
	 * <p> Type: Boundary </p>
	 *
	 * <p> Description: When no lesson is selected in the table, checkCanDelete() should
	 * return a non-empty message instead of throwing an exception.</p>
	 */
	@Test
	void testCheckCanDeleteNoSelection() {
		String msg = ModelContributorLessons.checkCanDelete(null, "Alice");
		assertFalse(msg.isEmpty(), "A null selection should return a message");
		System.out.println("testCheckCanDeleteNoSelection: " + PASSED);
	}

	/**********
	 * <p> Test: testDeleteLessonTwice() </p>
	 *
	 * <p> Type: Boundary </p>
	 *
	 * <p> Description: Deleting the same lesson a second time should report a problem 
	 * and the lesson count should remain at the post-first-delete value of 2.</p>
	 *
	 * @throws Exception if loading lessons from the database fails
	 */
	@Test
	void testDeleteLessonTwice() throws Exception {
		assertEquals("", ModelContributorLessons.deleteLesson(aliceLesson1, "Alice"));
		String second = ModelContributorLessons.deleteLesson(aliceLesson1, "Alice");
		assertFalse(second.isEmpty(), "Second delete should report a problem");
		assertEquals(2, ModelContributorLessons.loadMyLessons("Alice").size());
		System.out.println("testDeleteLessonTwice: " + PASSED);
	}

}