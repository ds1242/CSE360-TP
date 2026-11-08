package guiAITools;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;


/**********
 * <p> Title: ModelAIToolsTest Class </p>
 *
 * <p> Description: JUnit 5 tests for the AI Tools page. These tests check that the tool
 * category ComboBox holds the selected value and that the controller's performAsk() method
 * reports an error when no category has been chosen.</p>
 *
 * <p> The tests touch JavaFX controls, so each one runs its UI work on the JavaFX
 * Application Thread. No database or logged-in user is needed.</p>
 */
class ModelAIToolsTest {

	/**********
	 * <p> Method: initJavaFX() </p>
	 *
	 * <p> Description: Runs once before all tests. Starts the JavaFX runtime so controls can be
	 * created safely. If the runtime was already started (for example by another test class in
	 * the same JVM), the resulting IllegalStateException is ignored.</p>
	 */
	@BeforeAll
	static void initJavaFX() {
		try {
			Platform.startup(() -> {});
		} catch (IllegalStateException e) {
			// Toolkit already running from another test class; that's fine
		}
	}

	/**********
	 * <p> Method: runOnFx(Runnable) </p>
	 *
	 * <p> Description: Runs the given code on the JavaFX Application Thread and blocks until it
	 * finishes, so the calling test can make assertions on the result afterward.</p>
	 *
	 * @param r	the code to run on the JavaFX thread
	 * @throws Exception if the code does not finish within five seconds or the wait is
	 *                   interrupted
	 */
	private static void runOnFx(Runnable r) throws Exception {
		CountDownLatch latch = new CountDownLatch(1);
		Platform.runLater(() -> {
			try { r.run(); } finally { latch.countDown(); }
		});
		assertTrue(latch.await(5, TimeUnit.SECONDS), "FX thread timed out");
	}

	/**********
	 * <p> Method: freshCombo() </p>
	 *
	 * <p> Description: Runs before each test. Replaces the page's ComboBox with a new one
	 * populated from ViewAITools.CATEGORIES (with nothing selected) and clears the message
	 * label, so no test sees state left behind by another.</p>
	 *
	 * @throws Exception if the JavaFX thread times out
	 */
	void freshCombo() throws Exception {
		runOnFx(() -> {
			ViewAITools.combobox_ToolSelect = new ComboBox<>(
					FXCollections.observableArrayList(ViewAITools.CATEGORIES));
			ViewAITools.label_Message.setText("");
		});
	}

	/**********
	 * <p> Method: testCategorySelected() </p>
	 *
	 * <p> Description: Selects "Write" in the ComboBox and verifies that getValue() returns
	 * that same category.</p>
	 *
	 * @throws Exception if the JavaFX thread times out
	 */
	@Test
	void testCategorySelected() throws Exception {
		runOnFx(() -> ViewAITools.combobox_ToolSelect.getSelectionModel().select("Write"));

		assertEquals("Write", ViewAITools.combobox_ToolSelect.getValue());
	}

	
	/**********
	 * <p> Method: testCategoryNotSelected() </p>
	 *
	 * <p> Description: Calls performAsk() with no category selected and verifies that the
	 * ComboBox is still empty and that the message label shows the "select a tool" error.</p>
	 *
	 * @throws Exception if the JavaFX thread times out
	 */
	@Test
	void testCategoryNotSelected() throws Exception {
		runOnFx(() -> ControllerAITools.performAsk());

		assertNull(ViewAITools.combobox_ToolSelect.getValue());
		assertEquals("Please select a tool before asking.",
				ViewAITools.label_Message.getText());
	}
}