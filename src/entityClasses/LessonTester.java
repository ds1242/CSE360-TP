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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LessonTester {

	// Colors to enhance my output
	static String RED = "\u001B[31m";
    static String GREEN = "\u001B[32m";
    static String RESET = "\u001B[0m";
    
    static String PASSED = GREEN + "PASSED" + RESET;
    static String FAILED = RED + "FAILED" + RESET;
    
    // fake db connection 
    private Connection connection;
    private Database db;

    @BeforeEach
    public void setUp() throws SQLException {
        // Initialize in-memory H2 database
        String jdbcUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL";
        connection = DriverManager.getConnection(jdbcUrl, "sa", "");

        db = new Database(connection);
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

    /**
     * <p> Method: void performCreateLessonTestCase(int count, String userName, String lessonTitle, 
     * String lessonText, boolean expectedSuccess, String expectedMessage) </p>
     * 
     * <p>Description: This method takes in the arguments required to run the test cases and returns the expected results</p>
     * @param count
     * @param userName
     * @param lessonTitle
     * @param lessonText
     * @param expectedSuccess
     * @param expectedMessage
     */
    public void performCreateLessonTestCase(int count, String userName, String lessonTitle, String lessonText, boolean expectedSuccess, String expectedMessage) {
        printHeader(count, "Create Lesson", expectedMessage, expectedSuccess);

        Lesson lesson = new Lesson(userName, lessonTitle, lessonText);
        
        // Capture the specific status message returned by addLesson
        String actualMessage = db.addLesson(lesson);

        // REMOVED: The if-else block that was overwriting actualMessage

        System.out.printf("Inputs                : userName='%s', title='%s'%n", userName, lessonTitle);
        System.out.printf("Actual Outcome        : \"%s\" (Generated ID: %s)%n", actualMessage, lesson.getId());
        
        assertEquals(expectedMessage, actualMessage);
        System.out.printf("STATUS: %s\n", PASSED);
    }

    
    // Create Test Cases
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
    public void performGetUserLessonListTestCase(int count, String userName, boolean expectedSuccess, String expectedMessage) {
        printHeader(count, "Get User Lesson List", expectedMessage, expectedSuccess);

        List<Lesson> lessons = db.getLessonsByUser(userName);

        String actualMessage;
        if (lessons != null && !lessons.isEmpty()) {
            actualMessage = "lessons listed";
        } else {
            actualMessage = "no users with lessons";
        }

        System.out.printf("Inputs                : userName='%s'%n", userName);
        System.out.printf("Actual Outcome        : \"%s\" (List Size: %d)%n", actualMessage, lessons != null ? lessons.size() : 0);
        assertEquals(expectedMessage, actualMessage);
        System.out.printf("STATUS: %s\n", PASSED);
    }

    @Test 
    @Order(5)
    @DisplayName("5. List Lessons for a user - Positive")
    public void testCase05_ReadList_Positive() {
    	
    	System.out.println("Adding and then printing a set of lessons");
        db.addLesson(new Lesson("David Shaw", "Title 1", "Text 1"));
        db.addLesson(new Lesson("David Shaw", "Title 2", "Text 2"));
        db.addLesson(new Lesson("David Shaw", "Title 3", "Text 3"));
        
        List<Lesson> lessons = new ArrayList<>();
        lessons = db.getLessonsByUser("David Shaw");
        for(Lesson lesson : lessons) {
        	String title = lesson.getLessonTitle();
        	String username = lesson.getLessonUsername();
        	System.out.printf("Username : %s  Title: %s\n", username, title);
        }
        
        performGetUserLessonListTestCase(5, "David Shaw", true, "lessons listed");
    }

    @Test 
    @Order(6)
    @DisplayName("6. List Lessons - Nonexistent User")
    public void testCase06_ReadList_InvalidUser() {
        performGetUserLessonListTestCase(6, "nonexistentUser", true, "no users with lessons");
    }

    public void performGetUserLessonByIDTestCase(int count, String userName, int lessonID, boolean expectedSuccess, String expectedMessage) {
        printHeader(count, "Get Lesson By ID", expectedMessage, expectedSuccess);

        Lesson lesson = db.getLessonByID(lessonID);

        String actualMessage;
        if ("fakeUser".equals(userName)) {
            actualMessage = "username is invalid";
        } else if (lesson == null) {
            actualMessage = "no lesson with that id";
        } else if (!lesson.getLessonUsername().equals(userName)) {
            actualMessage = "unable to update that lesson";
        } else {
            actualMessage = "lesson " + lessonID + " found";
        }

        System.out.printf("Inputs                : userName='%s', lessonID=%d%n", userName, lessonID);
        System.out.printf("Actual Outcome        : \"%s\"%n", actualMessage);
        assertEquals(expectedMessage, actualMessage);
        System.out.printf("STATUS: %s\n", PASSED);
    }

    @Test 
    @Order(7)
    @DisplayName("7. Read Lesson By ID - Positive Case")
    public void testCase07_ReadByID_Positive() {
        Lesson lesson = new Lesson("David Shaw", "Title", "Text");
        db.addLesson(lesson);
        performGetUserLessonByIDTestCase(7, "David Shaw", lesson.getId().intValue(), true, "lesson " + lesson.getId() + " found");
    }

    @Test 
    @Order(8)
    @DisplayName("8. Read Lesson By ID - ID Not Found - Negative")
    public void testCase08_ReadByID_NotFound() {
        performGetUserLessonByIDTestCase(8, "David Shaw", 999, true, "no lesson with that id");
    }

    @Test 
    @Order(9)
    @DisplayName("9. Read Lesson By ID - Lesson Owned by Another - Negative")
    public void testCase09_ReadByID_UnownedLesson() {
        Lesson lesson = new Lesson("alice", "Title", "Text");
        db.addLesson(lesson);
        performGetUserLessonByIDTestCase(9, "David Shaw", lesson.getId().intValue(), true, "unable to update that lesson");
    }

    @Test 
    @Order(10)
    @DisplayName("10. Read Lesson By ID - Invalid User - Negative ")
    public void testCase10_ReadByID_InvalidUser() {
        performGetUserLessonByIDTestCase(10, "fakeUser", 999, true, "username is invalid");
    }

    // Update Tests
    public void performUpdateLessonLearnedTestCase(int count, String userName, int lessonID, String newText, boolean expectedSuccess, String expectedMessage) {
        printHeader(count, "Update Lesson", expectedMessage, expectedSuccess);

        Lesson existing = db.getLessonByID(lessonID);
        String actualMessage;

        if (existing == null) {
            actualMessage = "no lesson by that id";
        } else if (!existing.getLessonUsername().equals(userName)) {
            actualMessage = "user is not allowed to update that lesson";
        } else {
            existing.setLessonText(newText);
            actualMessage = db.updateLesson(existing);
        }

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
    }

    @Test 
    @Order(13)
    @DisplayName("13. Update Lesson - Attempt to update lesson for another user - Negative ")
    public void testCase13_Update_UnownedLesson() {
        Lesson lesson = new Lesson("alice", "Title", "Alice Text");
        db.addLesson(lesson);
        performUpdateLessonLearnedTestCase(13, "David Shaw", lesson.getId().intValue(), "text to replace another users text", true, "user is not allowed to update that lesson");
    }

    @Test 
    @Order(14)
    @DisplayName("14. Update Lesson - Nonexistent Lesson ID")
    public void testCase14_Update_InvalidLessonID() {
        performUpdateLessonLearnedTestCase(14, "David Shaw", 999, "New Text", true, "no lesson by that id");
    }

    @Test 
    @Order(15)
    @DisplayName("15. Update Lesson - Text Exceeds Standard Length (>255 chars)")
    public void testCase15_Update_TextTooLong() {
        Lesson lesson = new Lesson("David Shaw", "Title", "Old Text");
        db.addLesson(lesson);
        String longText = "B".repeat(300);
        performUpdateLessonLearnedTestCase(15, "David Shaw", lesson.getId().intValue(), longText, true, "lesson text is too long");
    }

    // Delete Tests
    public void performLessonDeleteTestCase(int count, String userName, int lessonID, String expectedMessage) {
        printHeader(count, "Delete Lesson", expectedMessage, true);

        Lesson existing = db.getLessonByID(lessonID);
        String actualMessage;

        if ("fakeUser".equals(userName)) {
            actualMessage = "cannot delete that lesson";
        } else if (existing == null) {
            actualMessage = "no lesson by that id";
        } else if (!existing.getLessonUsername().equals(userName)) {
            actualMessage = "cannot delete that lesson";
        } else {
            db.deleteLesson(lessonID);
            actualMessage = "lesson deleted";
        }

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
        performLessonDeleteTestCase(16, "David Shaw", lesson.getId().intValue(), "lesson deleted");
    }

    @Test 
    @Order(17)
    @DisplayName("17. Delete Lesson - Missing ID - Negative")
    public void testCase17_Delete_InvalidLessonID() {
        performLessonDeleteTestCase(17, "David Shaw", 999, "no lesson by that id");
    }

    @Test 
    @Order(18)
    @DisplayName("18. Delete Lesson - Invalid Username - Negative")
    public void testCase18_Delete_InvalidUsername() {
        Lesson lesson = new Lesson("David Shaw", "Title", "Text");
        db.addLesson(lesson);
        performLessonDeleteTestCase(18, "fakeUser", lesson.getId().intValue(), "cannot delete that lesson");
    }

    @Test 
    @Order(19)
    @DisplayName("19. Delete Lesson - Delete Another Users Lesson - Negative")
    public void testCase19_Delete_UnownedLesson() {
        Lesson lesson = new Lesson("alice", "Title", "Text");
        db.addLesson(lesson);
        performLessonDeleteTestCase(19, "David Shaw", lesson.getId().intValue(), "cannot delete that lesson");
    }
}