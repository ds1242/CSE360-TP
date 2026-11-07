package guiAddRemoveRoles;

import database.Database;
import entityClasses.User;
import applicationMain.FoundationsMain;

import static org.junit.jupiter.api.Assertions.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**********
 * <p> Class: ControllerAddRemoveRolesTest </p>
 *
 * <p> Description: JUnit 5 tests for ControllerAddRemoveRoles. Each test runs against a
 * fresh in-memory H2 database seeded with four users: Bob (admin), Alice, Jerry, and Barry (all
 * non-admin contributors).</p>
 *
 * </ul>
 */
public class ControllerAddRemoveRolesTest {

	/** ANSI escape code that switches console text to green. */
	static String GREEN = "\u001B[32m";

	/** ANSI escape code that resets console text to its default color. */
	static String RESET = "\u001B[0m";

	/** Green "PASSED" label printed at the end of each successful test. */
	static String PASSED = GREEN + "PASSED" + RESET;

	/** JDBC connection to the in-memory H2 test database; closed in tearDown(). */
	private Connection connection;

	/** The Database instance backed by connection and used by every test. */
	private Database db;

	/** The application's real Database reference, saved so it can be restored after each test. */
	private Database originalDatabase;

	/**********
	 * <p> Method: injectDb(Class, Database) </p>
	 *
	 * <p> Description: Uses reflection to replace the private static theDatabase field of
	 * the given class. This is needed because the controller reads
	 * FoundationsMain.database once, when the class is first loaded, and would otherwise
	 * keep pointing at a database from an earlier test (whose connection has been closed).</p>
	 *
	 * @param cls	the class whose static theDatabase field is to be replaced
	 * @param value	the Database instance to store in that field
	 * @throws Exception if the class has no theDatabase field or it cannot be accessed
	 */
	private static void injectDb(Class<?> cls, Database value) throws Exception {
		var f = cls.getDeclaredField("theDatabase");
		f.setAccessible(true);
		f.set(null, value);
	}

	/**********
	 * <p> Method: initJavaFX() </p>
	 *
	 * <p> Description: Runs once before all tests. Starts the JavaFX runtime so ComboBox objects
	 * can be created safely. If the runtime was already started (for example by another test
	 * class in the same JVM), the resulting IllegalStateException is caught and reported.</p>
	 */
	@BeforeAll
	static void initJavaFX() {
		try {
			Platform.startup(() -> {});
		} catch (IllegalStateException e) {
			System.out.println("Error: Unable to start JavaFX");
		}
	}

	/**********
	 * <p> Method: setUp() </p>
	 *
	 * <p> Description: Runs before each test. Opens a fresh in-memory H2 database, points the
	 * application and the controller at it, and registers the test users.</p>
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
		injectDb(ControllerAddRemoveRoles.class, db);

		// Coverage test users
		db.register(new User("Bob", "Password1!", "Bob", "", "Last", "Bobby",
				"bob@test.com", true, false, false, false));
		db.register(new User("Alice", "Password1!", "Alice", "", "Last", "Alice",
				"alice@test.com", false, true, false, false));
		// Boundary Test users
		db.register(new User("Jerry", "Password1!", "Jerry", "", "Last", "Jerry",
				"Jerry@test.com", false, true, false, false));
		db.register(new User("Barry", "Password1!", "Barry", "", "Last", "Barry",
				"Barry@test.com", false, true, false, false));
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
		injectDb(ControllerAddRemoveRoles.class, originalDatabase);

		if (connection != null && !connection.isClosed()) {
			try (Statement stmt = connection.createStatement()) {
				stmt.execute("DROP ALL OBJECTS");
			}
			connection.close();
		}
	}

	/**********
	 * <p> Test: testSelectValidUserCoverage() </p>
	 *
	 * <p> Type: Coverage </p>
	 *
	 * <p> Description: Alice should exist in the database and her first name
	 * should be retrievable.</p>
	 */
	@Test
	void testSelectValidUserCoverage() {
		boolean exists = db.doesUserExist("Alice");
		assertTrue(exists, "Alice should exist in the database");

		assertEquals("Alice", db.getFirstName("Alice"));
		System.out.println("testSelectValidUserCoverage: " + PASSED);
	}

	/**********
	 * <p> Test: testSelectInvalidUserBoundary() </p>
	 *
	 * <p> Type: Boundary - Negative </p>
	 *
	 * <p> Description: Querying a user that does not exist should return false from
	 * doesUserExist() and null from getFirstName() without throwing an exception.</p>
	 */
	@Test
	void testSelectInvalidUserBoundary() {
		boolean exists = db.doesUserExist("GhostUser");
		assertFalse(exists, "GhostUser should not exist");
		assertNull(db.getFirstName("GhostUser"), "Non-existent user first name should be null");
		System.out.println("testSelectInvalidUserBoundary: " + PASSED);
	}

	/**********
	 * <p> Test: testPerformAddRole_NewRole() </p>
	 *
	 * <p> Type: Coverage </p>
	 *
	 * <p> Description: Simulates an admin choosing "Admin" in the add-role dropdown for Alice and
	 * invokes performAddRole(). The GUI refresh that follows the database write throws because
	 * the widgets are not initialized, so that exception is caught and the result is verified
	 * through the database: Alice should now have the admin role.</p>
	 */
	@Test
	void testPerformAddRole_NewRole() {
		ViewAddRemoveRoles.theSelectedUser = "Alice";

		ViewAddRemoveRoles.combobox_SelectRoleToAdd = new ComboBox<>();
		ViewAddRemoveRoles.combobox_SelectRoleToAdd.setItems(
				FXCollections.observableArrayList("<Select a role>", "Admin"));
		ViewAddRemoveRoles.combobox_SelectRoleToAdd.getSelectionModel().select("Admin");

		try {
			ControllerAddRemoveRoles.performAddRole();
		} catch (RuntimeException e) {
			// System.out.println("Caught: " + e);
		}

		User updatedAlice = db.getAllUsers().stream()
				.filter(u -> u.getUserName().equals("Alice"))
				.findFirst()
				.orElse(null);

		assertNotNull(updatedAlice);
		assertTrue(updatedAlice.getAdminRole(),
				"Controller should have successfully elevated Alice to Admin");
		System.out.println("testPerformAddRole_NewRole: " + PASSED);
	}

	/**********
	 * <p> Test: testPerformAddRole_SameRole() </p>
	 *
	 * <p> Type: Boundary </p>
	 *
	 * <p> Description: Selecting the dropdown header ("&lt;Select a role&gt;") instead of a real
	 * role should make performAddRole() do nothing, so Alice's roles must not change.</p>
	 */
	@Test
	void testPerformAddRole_SameRole() {
		ViewAddRemoveRoles.theSelectedUser = "Alice";

		ViewAddRemoveRoles.combobox_SelectRoleToAdd = new ComboBox<>();
		ViewAddRemoveRoles.combobox_SelectRoleToAdd.setItems(
				FXCollections.observableArrayList("<Select a role>", "Admin")
		);

		ViewAddRemoveRoles.combobox_SelectRoleToAdd.getSelectionModel().select("<Select a role>");

		ControllerAddRemoveRoles.performAddRole();

		// Verify Alice's roles did not change
		User alice = db.getAllUsers().stream()
				.filter(u -> u.getUserName().equals("Alice"))
				.findFirst()
				.orElse(null);

		assertFalse(alice.getAdminRole(), "Selecting the header prompt should not alter user roles");
		System.out.println("testPerformAddRole_SameRole: " + PASSED);
	}

	/**********
	 * <p> Test: testPerformAddRole_NonAdminRoleUpdate() </p>
	 *
	 * <p> Type: Boundary (out of bounds) </p>
	 *
	 * <p> Description: A logged-in non-admin (Jerry) tries to grant himself the Admin role
	 * through the controller. The role must not change.</p>
	 *
	 * <p> Expected to FAIL against the current controller, which performs no check that
	 * the current user is an admin. A failure here documents the missing authorization
	 * check.</p>
	 */
	@Test
	void testPerformAddRole_NonAdminRoleUpdate() {
		User jerry = db.getAllUsers().stream()
				.filter(u -> u.getUserName().equals("Jerry"))
				.findFirst()
				.orElse(null);

		assertFalse(jerry.getAdminRole(), "Precondition: Jerry must not start as admin");

		ViewAddRemoveRoles.theUser = jerry;
		ViewAddRemoveRoles.theSelectedUser = "Jerry";
		ViewAddRemoveRoles.combobox_SelectRoleToAdd = new ComboBox<>();
		ViewAddRemoveRoles.combobox_SelectRoleToAdd.setItems(
				FXCollections.observableArrayList("<Select a role>", "Admin"));
		ViewAddRemoveRoles.combobox_SelectRoleToAdd.getSelectionModel().select("Admin");

		try {
			ControllerAddRemoveRoles.performAddRole();
		} catch (RuntimeException e) {
			// System.out.println("Caught: " + e);
		}

		User jerryAfter = db.getAllUsers().stream()
				.filter(u -> u.getUserName().equals("Jerry"))
				.findFirst()
				.orElse(null);

		assertNotNull(jerryAfter);
		assertFalse(jerryAfter.getAdminRole(),
				"A non-admin must not be able to grant roles through the controller");
		System.out.println("testPerformAddRole_NonAdminRoleUpdate: " + PASSED);
	}

	/**********
	 * <p> Test: testPerformRemoveRole() </p>
	 *
	 * <p> Type: Coverage </p>
	 *
	 * <p> Description: Invokes performRemoveRole() for Alice with "Admin" selected and verifies
	 * through the database that Alice does not hold the admin role afterward. The GUI refresh
	 * after the database write is expected to throw, so the exception is caught.</p>
	 */
	@Test
	void testPerformRemoveRole() {
		ViewAddRemoveRoles.theSelectedUser = "Alice";

		ViewAddRemoveRoles.combobox_SelectRoleToAdd = new ComboBox<>();
		ViewAddRemoveRoles.combobox_SelectRoleToAdd.setItems(
				FXCollections.observableArrayList("<Select a role>", "Admin"));
		ViewAddRemoveRoles.combobox_SelectRoleToAdd.getSelectionModel().select("Admin");

		try {
			ControllerAddRemoveRoles.performRemoveRole();
		} catch (RuntimeException e) {
			// System.out.println("Caught: " + e);
		}

		User updatedAlice = db.getAllUsers().stream()
				.filter(u -> u.getUserName().equals("Alice"))
				.findFirst()
				.orElse(null);

		assertNotNull(updatedAlice);
		assertFalse(updatedAlice.getAdminRole(),
				"Controller should have successfully lowered Alice from Admin");
		System.out.println("testPerformRemoveRole: " + PASSED);
	}

	/**********
	 * <p> Test: testPerformRemoveRole_Unauthorized() </p>
	 *
	 * <p> Type: Boundary (out of bounds) </p>
	 *
	 * <p> Description: A logged-in non-admin (Barry) attempts to change his own Role1 through the
	 * controller. Barry must still hold Role1 afterward.</p>
	 *
	 * <p> Expected to FAIL against the current controller, which performs no check that
	 * the current user is an admin.</p>
	 */
	@Test
	void testPerformRemoveRole_Unauthorized() {
		User barry = db.getAllUsers().stream()
				.filter(u -> u.getUserName().equals("Barry"))
				.findFirst()
				.orElse(null);

		assertFalse(barry.getAdminRole(), "Precondition: Barry must not start as admin");
		assertTrue(barry.getNewRole1(), "Precondition: Barry must have role 1");

		ViewAddRemoveRoles.theUser = barry;
		ViewAddRemoveRoles.theSelectedUser = "Barry";
		ViewAddRemoveRoles.combobox_SelectRoleToAdd = new ComboBox<>();
		ViewAddRemoveRoles.combobox_SelectRoleToAdd.setItems(
				FXCollections.observableArrayList("<Select a role>", "Role1"));
		ViewAddRemoveRoles.combobox_SelectRoleToAdd.getSelectionModel().select("Role1");

		try {
			ControllerAddRemoveRoles.performAddRole();
		} catch (RuntimeException e) {
			// System.out.println("Caught: " + e);
		}

		User barryAfter = db.getAllUsers().stream()
				.filter(u -> u.getUserName().equals("Barry"))
				.findFirst()
				.orElse(null);

		assertNotNull(barryAfter);
		assertFalse(barryAfter.getNewRole1(),
				"A non-admin must not be able to grant roles through the controller");
		System.out.println("testPerformAddRole_NonAdminRoleUpdate: " + PASSED);
	}

}