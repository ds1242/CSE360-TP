package database;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entityClasses.User;
import lessons.LessonRecord;

class DatabaseTest {

	private Database database;

	@BeforeEach
	void setUp() throws SQLException {
		String url = "jdbc:h2:mem:" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1";

		database = new Database(url);
		database.connectToDatabase();
	}

	@AfterEach
	void tearDown() {
		database.closeConnection();
	}

	private User createUser(String username) {
		return new User(username, "password123", "Test", "", "User", "Test", username + "@test.com", false, true, false,
				false);
	}

	private int createTestLesson() throws SQLException {
		LessonRecord lesson = database.createLesson("Test Lesson", "Test Description", "Testing", 2.0f, "testuser");

		return lesson.getId();
	}

	@Test
	void testGetUserListPredicateOfString() throws SQLException {

		// Test minimum index for an empty database is equal to 1
		List<String> emptyList = database.getUserList(name -> true);
		assertEquals(1, emptyList.size());

		// Add users
		database.register(createUser("Alice"));
		database.register(createUser("Adam"));
		database.register(createUser("Andrew"));
		database.register(createUser("Ally"));

		
		List<String> matching = database.getUserList(name -> name.startsWith("A"));
		
		// Test for maximum index being 4 with the above list
		assertEquals(5, matching.size());

		// Test out-of-bounds reads below maximum size
		assertThrows(IndexOutOfBoundsException.class, () -> matching.get(-1));

		// Tests out-of-bounds reads above maximum size
		assertThrows(IndexOutOfBoundsException.class, () -> matching.get(matching.size()));

		// Test out-of-bounds writes below minimum size
		assertThrows(IndexOutOfBoundsException.class, () -> matching.set(-1, "Invalid"));

		// Tests out-of-bounds writes above maximum size
		assertThrows(IndexOutOfBoundsException.class, () -> matching.set(matching.size(), "Invalid"));
		
		// Tests just above minimum
		assertEquals("Adam", matching.get(1));
		
		// Tests just above minimum
		assertEquals("Andrew", matching.get(4));
	}
	
	@Test
	void testGetUserListPredicateOfStringSQLException() throws SQLException {

	    database.closeConnection();

	    assertDoesNotThrow(() -> database.getUserList(name -> true));
	}

	@Test
	void testGetAllUsers() throws SQLException {

	    database.register(createUser("alice"));
	    database.register(createUser("bob"));
	    database.register(createUser("charlie"));
	    database.register(createUser("Andrew"));

	    List<User> users = database.getAllUsers();

	    // Tests minimum index
	    assertEquals("alice", users.get(0).getUserName());
	    
	    // Tests one above minimum index
	    assertEquals("bob", users.get(1).getUserName());

	    // Tests maximum index
	    assertEquals(4, users.size());
	    
	    // Tests one below maximum index
	    assertEquals(3, users.size()-1);

	    // Tests out-of-bounds read below minimum
	    assertThrows(IndexOutOfBoundsException.class,
	        () -> users.get(-1));

	    // Tests out-of-bounds read above maximum
	    assertThrows(IndexOutOfBoundsException.class,
	        () -> users.get(users.size()));
	}
	
	@Test
	void testGetAllUsersSQLException() throws SQLException {

	    database.closeConnection();

	    assertDoesNotThrow(() -> database.getAllUsers());
	}

	
	@Test
	void testGetExperiences() throws SQLException {

		int lessonId = createTestLesson();
		
		database.createExperience(lessonId, "First experience", "Testing", 1.0f, Collections.emptyList());
		database.createExperience(lessonId, "Second experience", "Development", 2.0f, Collections.emptyList());
		database.createExperience(lessonId, "Third experience", "Development", 2.0f, Collections.emptyList());
		database.createExperience(lessonId, "Fourth experience", "Development", 2.0f, Collections.emptyList());
		
		// Tests the maximum index of the experiences list
		assertEquals(4, database.getExperiences(lessonId).size());
		
		// Tests one below the maximum index of the experiences list
		assertEquals(3, database.getExperiences(lessonId).size()-1);

		// Tests minimum index of the experiences list
		assertEquals("First experience", database.getExperiences(lessonId).get(0).getWhatWasDone());
		
		// Tests one above the minimum index of the experiences list
		assertEquals("Second experience", database.getExperiences(lessonId).get(1).getWhatWasDone());

		// Tests out-of-bounds read below minimum size
		assertThrows(IndexOutOfBoundsException.class, () -> database.getExperiences(lessonId).get(-1));
		
		// Tests out-of-bounds read above maximum size
		assertThrows(IndexOutOfBoundsException.class, () -> database.getExperiences(lessonId).get(database.getExperiences(lessonId).size()));
	}
}
