package entityClasses;

import database.Database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LessonTester {

    // Colors to enhance output
    static String RED = "\u001B[31m";
    static String GREEN = "\u001B[32m";
    static String RESET = "\u001B[0m";
    
    static String PASSED = GREEN + "PASSED" + RESET;
    static String FAILED = RED + "FAILED" + RESET;
    
    private Connection connection;
    private Database db;

    @BeforeEach
    public void setUp() throws SQLException {
        // Initialize in-memory H2 database
        String jdbcUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL";
        connection = DriverManager.getConnection(jdbcUrl, "sa", "");

        db = new Database(connection);

        // Register Users into the db for lessons 
        db.register(new User("David Shaw", "password", "David", "", "Shaw", "David",
                "david@test.com", false, true, false, false));
        db.register(new User("alice", "password", "Alice", "", "Smith", "Alice",
                "alice@test.com", false, true, false, false));
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DROP ALL OBJECTS");
            }
            connection.close();
        }
    }

    private void printHeader(int count, String title, String expected, boolean success) {
        System.out.println("================================================================================");
        System.out.printf("TEST CASE #%d: %s%n", count, title);
        System.out.printf("Expected Success Flag : %b%n", success);
        System.out.printf("Expected Return Msg   : \"%s\"%n", expected);
        System.out.println("--------------------------------------------------------------------------------");
    }

    // Create Tests
    public void performCreateLessonTestCase(int count, String userName, String lessonTitle, String lessonText, boolean expectedSuccess, String expectedMessage) {
        printHeader(count, "Create Lesson", expectedMessage, expectedSuccess);

        Lesson lesson = new Lesson(userName, lessonTitle, lessonText);
        String actualMessage = db.addLesson(lesson);

        System.out.printf("Inputs                : userName='%s', title='%s'%n", userName, lessonTitle);
        System.out.printf("Actual Outcome        : \"%s\" (Generated ID: %s)%n", actualMessage, lesson.getId());
        
        assertEquals(expectedMessage, actualMessage);
        System.out.printf("STATUS: %s\n", PASSED);
    }

    @Test 
    @Order(1)
    @DisplayName("1. Create Lesson - Valid positive test case")
    public void testCase01_Create_Positive() {
        performCreateLessonTestCase(1, "David Shaw", "Let's create a new post", "Valid lesson text to be put into the lesson", true, "lesson created");
    }

    @Test 
    @Order(2)
    @DisplayName("2. Create Lesson - Invalid Username - Negative")
    public void testCase02_Create_InvalidUser() {
        performCreateLessonTestCase(2, "invalidUser", "Title", "Text", false, "no matching username, unable to create a lesson");
    }

    @Test 
    @Order(3)
    @DisplayName("3. Create Lesson - Empty Lesson Text - Negative")
    public void testCase03_Create_NoLessonInfo() {
        performCreateLessonTestCase(3, "David Shaw", "Title", "", false, "no lesson learned information");
    }

    @Test 
    @Order(4)
    @DisplayName("4. Create Lesson - Text Exceeds Standard Length (>255 chars) - Negative")
    public void testCase04_Create_TextTooLong() {
        String longText = "A".repeat(300);
        performCreateLessonTestCase(4, "David Shaw", "Title", longText, false, "lesson learned text is too long unable to create a lesson");
    }

    // Read Tests
    public void performGetAllLessonsTestCase(int count, int expectedSize, boolean expectedSuccess, String expectedMessage) {
        printHeader(count, "Get All Lessons", expectedMessage, expectedSuccess);

        List<Lesson> lessons = db.getLessons();

        int actualSize = 0;
        String actualMessage = "no lessons found";
        if (lessons != null) {
            actualSize = lessons.size();
            if (!lessons.isEmpty()) {
                actualMessage = "lessons listed";
            }
        }

        System.out.printf("Actual Outcome        : \"%s\" (List Size: %d)%n", actualMessage, actualSize);
        
        assertNotNull(lessons, "Lessons list should not be null");
        assertEquals(expectedSize, actualSize, "List size does not match expected");
        assertEquals(expectedMessage, actualMessage);
        System.out.printf("STATUS: %s\n", PASSED);
    }

    @Test 
    @Order(5)
    @DisplayName("5. List all Lessons - Positive")
    public void testCase05_ReadAllList_Positive() {
        db.addLesson(new Lesson("David Shaw", "Title 1", "Text 1"));
        db.addLesson(new Lesson("David Shaw", "Title 2", "Text 2"));
        db.addLesson(new Lesson("David Shaw", "Title 3", "Text 3"));
        
        performGetAllLessonsTestCase(5, 3, true, "lessons listed");

        // matching information on individual lessons 
        List<Lesson> lessons = db.getLessons();
        for (int i = 0; i < lessons.size(); i++) {
            assertEquals("David Shaw", lessons.get(i).getLessonUsername());
            assertEquals("Title " + (i + 1), lessons.get(i).getLessonTitle());
        }
    }

    // Checks that users exist and have lessons entered
    public void performGetUserLessonListTestCase(int count, String userName, boolean expectedSuccess, String expectedMessage) {
        printHeader(count, "Get User Lesson List", expectedMessage, expectedSuccess);

        List<Lesson> lessons = db.getLessonsByUser(userName);

        int actualSize = 0;
        String actualMessage = "no users with lessons";
        if (lessons != null) {
            actualSize = lessons.size();
            if (!lessons.isEmpty()) {
                actualMessage = "lessons listed";
            }
        }

        System.out.printf("Inputs                : userName='%s'%n", userName);
        System.out.printf("Actual Outcome        : \"%s\" (List Size: %d)%n", actualMessage, actualSize);
        assertEquals(expectedMessage, actualMessage);
        System.out.printf("STATUS: %s\n", PASSED);
    }

    @Test 
    @Order(6)
    @DisplayName("6. List Lessons for a user - Positive")
    public void testCase06_ReadList_Positive() {
        db.addLesson(new Lesson("David Shaw", "Title 1", "Text 1"));
        db.addLesson(new Lesson("David Shaw", "Title 2", "Text 2"));
        db.addLesson(new Lesson("David Shaw", "Title 3", "Text 3"));
        
        performGetUserLessonListTestCase(6, "David Shaw", true, "lessons listed");
        
        // checking for lesson by user
        List<Lesson> lessons = db.getLessonsByUser("David Shaw");
        assertNotNull(lessons);
        assertEquals(3, lessons.size());
    }

    @Test 
    @Order(7)
    @DisplayName("7. List Lessons - Nonexistent User")
    public void testCase07_ReadList_InvalidUser() {
        performGetUserLessonListTestCase(7, "nonexistentUser", true, "no users with lessons");
    }

    public void performGetUserLessonByIDTestCase(int count, String userName, int lessonID, boolean expectedSuccess, String expectedMessage) {
        printHeader(count, "Get Lesson By ID", expectedMessage, expectedSuccess);

        String actualMessage = db.checkLessonAccess(lessonID, userName);

        System.out.printf("Inputs                : userName='%s', lessonID=%d%n", userName, lessonID);
        System.out.printf("Actual Outcome        : \"%s\"%n", actualMessage);
        assertEquals(expectedMessage, actualMessage);
        System.out.printf("STATUS: %s\n", PASSED);
    }

    @Test 
    @Order(8)
    @DisplayName("8. Read Lesson By ID - Positive Case")
    public void testCase08_ReadByID_Positive() {
        Lesson lesson = new Lesson("David Shaw", "Title", "Text");
        db.addLesson(lesson);
        performGetUserLessonByIDTestCase(8, "David Shaw", lesson.getId().intValue(), true, "lesson " + lesson.getId() + " found");
    }

    @Test 
    @Order(9)
    @DisplayName("9. Read Lesson By ID - ID Not Found - Negative")
    public void testCase09_ReadByID_NotFound() {
        performGetUserLessonByIDTestCase(9, "David Shaw", 999, false, "no lesson with that id");
    }

    @Test 
    @Order(10)
    @DisplayName("10. Read Lesson By ID - Lesson Owned by Another - Negative")
    public void testCase10_ReadByID_UnownedLesson() {
        Lesson lesson = new Lesson("alice", "Title", "Text");
        db.addLesson(lesson);
        performGetUserLessonByIDTestCase(10, "David Shaw", lesson.getId().intValue(), false, "unable to read that lesson");
    }

    @Test 
    @Order(11)
    @DisplayName("11. Read Lesson By ID - Invalid User - Negative")
    public void testCase11_ReadByID_InvalidUser() {
        performGetUserLessonByIDTestCase(11, "fakeUser", 999, false, "username is invalid");
    }

    // Update Tests
    public void performUpdateLessonLearnedTestCase(int count, String userName, int lessonID, String newText, boolean expectedSuccess, String expectedMessage) {
        printHeader(count, "Update Lesson", expectedMessage, expectedSuccess);

        // Build the lesson to send to the database. If the id doesn't exist,
        // build a stand-in so the database can report that itself.
        Lesson toUpdate = db.getLessonByID(lessonID);
        if (toUpdate == null) {
            toUpdate = new Lesson((long) lessonID, userName, "Title", newText, null, null);
        } else {
            toUpdate.setLessonText(newText);
        }

        String actualMessage = db.updateLesson(toUpdate, userName);

        System.out.printf("Inputs                : userName='%s', lessonID=%d, newText='%s'%n", userName, lessonID, newText);
        System.out.printf("Actual Outcome        : \"%s\"%n", actualMessage);
        assertEquals(expectedMessage, actualMessage);
        System.out.printf("STATUS: %s\n", PASSED);
    }

    @Test 
    @Order(12)
    @DisplayName("12. Update Lesson - Positive Case")
    public void testCase12_Update_Positive() {
        Lesson l = new Lesson("David Shaw", "Title", "Old Text");
        db.addLesson(l);
        performUpdateLessonLearnedTestCase(12, "David Shaw", l.getId().intValue(), "New Updated Text", true, "lesson updated");

        // confirm the new text was actually saved
        assertEquals("New Updated Text", db.getLessonByID(l.getId().intValue()).getLessonText());
    }

    @Test 
    @Order(13)
    @DisplayName("13. Update Lesson - Attempt to update lesson for another user - Negative")
    public void testCase13_Update_UnownedLesson() {
        Lesson lesson = new Lesson("alice", "Title", "Alice Text");
        db.addLesson(lesson);
        performUpdateLessonLearnedTestCase(13, "David Shaw", lesson.getId().intValue(), "text to replace another users text", false, "user is not allowed to update that lesson");

        // alice's lesson must be unchanged
        assertEquals("Alice Text", db.getLessonByID(lesson.getId().intValue()).getLessonText());
    }

    @Test 
    @Order(14)
    @DisplayName("14. Update Lesson - Nonexistent Lesson ID")
    public void testCase14_Update_InvalidLessonID() {
        performUpdateLessonLearnedTestCase(14, "David Shaw", 999, "New Text", false, "no lesson by that id");
    }

    @Test 
    @Order(15)
    @DisplayName("15. Update Lesson - Text Exceeds Standard Length (>255 chars)")
    public void testCase15_Update_TextTooLong() {
        Lesson lesson = new Lesson("David Shaw", "Title", "Old Text");
        db.addLesson(lesson);
        String longText = "B".repeat(300);
        performUpdateLessonLearnedTestCase(15, "David Shaw", lesson.getId().intValue(), longText, false, "lesson text is too long");

        // the old text must still be stored
        assertEquals("Old Text", db.getLessonByID(lesson.getId().intValue()).getLessonText());
    }

    // Delete Tests
    public void performLessonDeleteTestCase(int count, String userName, int lessonID, boolean expectedSuccess, String expectedMessage) {
        printHeader(count, "Delete Lesson", expectedMessage, expectedSuccess);

        String actualMessage = db.deleteLesson(lessonID, userName);

        System.out.printf("Inputs                : userName='%s', lessonID=%d%n", userName, lessonID);
        System.out.printf("Actual Outcome        : \"%s\"%n", actualMessage);
        assertEquals(expectedMessage, actualMessage);
        System.out.printf("STATUS: %s\n", PASSED);
    }

    @Test 
    @Order(16)
    @DisplayName("16. Delete Lesson - Positive Case")
    public void testCase16_Delete_Positive() {
        Lesson lesson = new Lesson("David Shaw", "Title", "Text");
        db.addLesson(lesson);
        performLessonDeleteTestCase(16, "David Shaw", lesson.getId().intValue(), true, "lesson deleted");

        // the lesson must be gone
        assertNull(db.getLessonByID(lesson.getId().intValue()), "Lesson should have been deleted");
    }

    @Test 
    @Order(17)
    @DisplayName("17. Delete Lesson - Missing ID - Negative")
    public void testCase17_Delete_InvalidLessonID() {
        performLessonDeleteTestCase(17, "David Shaw", 999, false, "no lesson by that id");
    }

    @Test 
    @Order(18)
    @DisplayName("18. Delete Lesson - Invalid Username - Negative")
    public void testCase18_Delete_InvalidUsername() {
        Lesson lesson = new Lesson("David Shaw", "Title", "Text");
        db.addLesson(lesson);
        performLessonDeleteTestCase(18, "fakeUser", lesson.getId().intValue(), false, "cannot delete that lesson");

        // the lesson must still be in the database
        assertNotNull(db.getLessonByID(lesson.getId().intValue()), "Lesson should still exist");
    }

    @Test 
    @Order(19)
    @DisplayName("19. Delete Lesson - Delete Another Users Lesson - Negative")
    public void testCase19_Delete_UnownedLesson() {
        Lesson lesson = new Lesson("alice", "Title", "Text");
        db.addLesson(lesson);
        performLessonDeleteTestCase(19, "David Shaw", lesson.getId().intValue(), false, "cannot delete that lesson");

        // the lesson must still be in the database
        assertNotNull(db.getLessonByID(lesson.getId().intValue()), "Lesson should still exist");
    }
}