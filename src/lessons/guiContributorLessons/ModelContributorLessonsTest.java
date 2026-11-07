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
import entityClasses.User;
import guiAddRemoveRoles.ControllerAddRemoveRoles;

class ModelContributorLessonsTest {
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

	@Test
	void testModelContributorLessons() {
		fail("Not yet implemented");
	}

	@Test
	void testLoadMyLessons() {
		fail("Not yet implemented");
	}

	@Test
	void testCheckCanView() {
		fail("Not yet implemented");
	}

	@Test
	void testCheckCanAddExperience() {
		fail("Not yet implemented");
	}

	@Test
	void testCheckCanDelete() {
		fail("Not yet implemented");
	}

	@Test
	void testDeleteLesson() {
		fail("Not yet implemented");
	}

}
