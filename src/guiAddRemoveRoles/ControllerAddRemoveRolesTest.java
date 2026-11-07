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
import java.sql.SQLException;

public class ControllerAddRemoveRolesTest {
    static String GREEN = "\u001B[32m";
    static String RESET = "\u001B[0m";
    static String PASSED = GREEN + "PASSED" + RESET;
    
    private Connection connection;
    private Database db;
    private Database originalDatabase;
    
    private static void injectDb(Class<?> cls, Database value) throws Exception {
        var f = cls.getDeclaredField("theDatabase");
        f.setAccessible(true);
        f.set(null, value);
    }

    // Boot up the JavaFX runtime thread so ComboBoxes can be instantiated safely
    @BeforeAll
    static void initJavaFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            System.out.println("Error: Unable to start JavaFX");
        }
    }

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

    // Coverage Test: Verify that a seeded user exists and can be queried correctly
    @Test
    void testSelectValidUserCoverage() {
        boolean exists = db.doesUserExist("Alice");
        assertTrue(exists, "Alice should exist in the database");
        
        assertEquals("Alice", db.getFirstName("Alice"));
        System.out.println("testSelectValidUserCoverage: " + PASSED);
    }
    
    // Boundary/Negative Test: Querying a non-existent user returns false/null safely
    @Test
    void testSelectInvalidUserBoundary() {
        boolean exists = db.doesUserExist("GhostUser");
        assertFalse(exists, "GhostUser should not exist");
        assertNull(db.getFirstName("GhostUser"), "Non-existent user first name should be null");
        System.out.println("testSelectInvalidUserBoundary: " + PASSED);
    }

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
//        	System.out.println("Caught: " + e);
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

    // Boundary Test: Selecting the unselectable dropdown header should do nothing
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
    
    
    // Checking if a user without Admin permissions can update a user's roles
    // This will fail as it is currently built.
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
//        	System.out.println("Caught: " + e);
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
    
    
    // coverage test to correctly remove a role
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
//            System.out.println("Caught: " + e);
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
    
    
    // boundary test to remove a role by a non-admin
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
//        	System.out.println("Caught: " + e);
        }

        User jerryAfter = db.getAllUsers().stream()
                .filter(u -> u.getUserName().equals("Barry"))
                .findFirst()
                .orElse(null);

        assertNotNull(jerryAfter);
        assertFalse(jerryAfter.getNewRole1(),
                "A non-admin must not be able to grant roles through the controller");
        System.out.println("testPerformAddRole_NonAdminRoleUpdate: " + PASSED);
    }
    
}