package guiAITools;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;

/**********
 * <p>
 * Title: ModelAIToolsTest Class
 * </p>
 *
 * <p>
 * Description: JUnit 5 tests for the AI Tools page. These tests check that the
 * tool category ComboBox holds the selected value and that the controller's
 * performAsk() method reports an error when no category has been chosen.
 * </p>
 *
 * <p>
 * The controls used here are never attached to a Scene, so the tests run them
 * directly on the test thread. No database or logged-in user is needed.
 * </p>
 */
class ModelAIToolsTest {

	/**********
	 * <p>
	 * Method: initJavaFX()
	 * </p>
	 *
	 * <p>
	 * Description: Runs once before all tests. Starts the JavaFX runtime so
	 * controls can be created.
	 * </p>
	 */
	@BeforeAll
	static void initJavaFX() {
		try {
			Platform.startup(() -> {
			});
		} catch (IllegalStateException e) {
			// Toolkit already running from another test class; that's fine
		}
	}

	/**********
	 * <p>
	 * Method: clearComboBox()
	 * </p>
	 *
	 * <p>
	 * Description: Runs before each test. Replaces the page's ComboBox with a new
	 * one populated from ViewAITools.CATEGORIES with nothing selected and clears
	 * the message label, so no test sees state left behind by another.
	 * </p>
	 */
	@BeforeEach
	void clearComboBox() {
		ViewAITools.combobox_ToolSelect = new ComboBox<String>(
				FXCollections.observableArrayList(ViewAITools.CATEGORIES));
		ViewAITools.label_Message.setText("");

		ViewAITools.text_Description.setText("");
	}

	// *******************************************************
	// Controller Tests
	// *******************************************************

	/**********
	 * <p>
	 * Method: testCategorySelected()
	 * </p>
	 *
	 * <p>
	 * Description: Selects Write in the ComboBox and verifies that getValue()
	 * returns that same category.
	 * </p>
	 */
	@Test
	void testCategorySelected() {
		ViewAITools.combobox_ToolSelect.getSelectionModel().select("Write");

		assertEquals("Write", ViewAITools.combobox_ToolSelect.getValue());
	}

	/**********
	 * <p>
	 * Method: testCategoryNotSelected()
	 * </p>
	 *
	 * <p>
	 * Description: Calls performAsk() with no category selected and verifies that
	 * the ComboBox is still empty and that the message label shows the error.
	 * </p>
	 */
	@Test
	void testCategoryNotSelected() {
		ControllerAITools.performAsk();

		assertNull(ViewAITools.combobox_ToolSelect.getValue());
		assertEquals("Please select a tool before asking.", ViewAITools.label_Message.getText());
	}

	// *******************************************************
	// Model Tests
	// *******************************************************
	@Test
	void testModelValidateInput() {
		String category = "Write";
		String msg = "";

		msg = ModelAITools.validateInput(category, null);

		assertNotEquals(msg, "Please select a tool category.");
	}

	@Test
	void testModelValidateInputNull() {
		String category = null;
		String msg = "";

		msg = ModelAITools.validateInput(category, null);

		assertEquals(msg, "Please select a tool category.");
	}

	@Test
	void testModelValidateInputEmptyString() {
		String category = "";
		String msg = "";

		msg = ModelAITools.validateInput(category, null);

		assertEquals(msg, "Please select a tool category.");
	}

	/***
	 * Boundary Test - Correct length within range Coverage Test - Covers Validating
	 */
	@Test
	void testModelValidateDescription() {
		String category = "Write";
		String description = "Validating user input description";
		String msg = "";

		msg = ModelAITools.validateInput(category, description);
		assertEquals(msg, "");
	}

	/***
	 * Boundary test - too long
	 */
	@Test
	void testModelValidateDescription_TooLong() {
		String category = "Write";
		String description = "a".repeat(2001);
		String msg = "";

		msg = ModelAITools.validateInput(category, description);
		assertEquals(msg, "Description cannot exceed " + ModelAITools.DESCRIPTION_MAX + " characters.");
	}

	/***
	 * Boundary test - Exact Max
	 */
	@Test
	void testModelValidateDescription_ExactMax() {
		String category = "Write";
		String description = "a".repeat(2000);
		String msg = "";

		msg = ModelAITools.validateInput(category, description);
		assertEquals(msg, "");
	}

	/***
	 * Boundary test - too short
	 */
	@Test
	void testModelValidateDescription_TooShort() {
		String category = "Write";
		String description = "a".repeat(19);
		String msg = "";

		msg = ModelAITools.validateInput(category, description);
		assertEquals(msg, "Description must be at least " + ModelAITools.DESCRIPTION_MIN + " characters.");
	}

	/***
	 * Boundary test - Exact Minimum
	 */
	@Test
	void testModelValidateDescription_Exact() {
		String category = "Write";
		String description = "a".repeat(20);
		String msg = "";

		msg = ModelAITools.validateInput(category, description);
		assertEquals(msg, "");
	}

}