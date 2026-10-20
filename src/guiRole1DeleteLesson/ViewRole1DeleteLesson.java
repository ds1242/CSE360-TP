package guiRole1DeleteLesson;

import database.Database;
import entityClasses.LessonsLearned;
import entityClasses.User;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/*******
 * <p> Title: ViewRole1DeleteLesson Class. </p>
 *
 * <p> Description: The Java/FX-based Role1 Delete Lesson Page, which lets the Contributor remove
 * one of the lessons learned that Contributor has added. </p>
 *
 * @author Edis Jakupovic
 *
 */

public class ViewRole1DeleteLesson {
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	// This is the first entry of the ComboBox.  It is a prompt rather than a lesson, which is how
	// the page knows that nothing has been chosen yet and the Delete button must stay hidden.
	protected static final String SELECT_PROMPT = "<Select a Lesson>";

	protected static Label label_PageTitle = new Label();
	protected static Label label_UserDetails = new Label();
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

	protected static Label label_SelectLesson = new Label("Select a lesson to be deleted: ");
	protected static ComboBox <String> combobox_SelectLesson = new ComboBox <String>();

	protected static Button button_DeleteLesson = new Button("Delete Lesson");
	protected static Alert alert_DeleteLesson = new Alert(AlertType.CONFIRMATION);

	protected static Line line_Separator4 = new Line(20, 525, width-20,525);
	protected static Button button_Return = new Button("Return");
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");

	private static ViewRole1DeleteLesson theView;
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	protected static Stage theStage;
	protected static Pane theRootPane;
	protected static User theUser;
	public static Scene theDeleteLessonScene = null;

	// NOTE: we initialize the selected lesson to the prompt because that is the initial value of
	// the combo box.  If not set to this, the else branch of repaintTheWindow would run before
	// anything had been chosen.
	protected static String theSelectedLesson = SELECT_PROMPT;

	/**********
	 * <p> Method: displayRole1DeleteLesson(Stage ps, User user) </p>
	 *
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the Delete Lesson page to be displayed.  The ComboBox is refilled on every visit, as a
	 * lesson may have been added or removed since the last time this page was shown. </p>
	 *
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 *
	 * @param user specifies the User for this GUI and it's methods
	 *
	 */
	public static void displayRole1DeleteLesson(Stage ps, User user) {
		theStage = ps;
		theUser = user;

		if (theView == null) theView = new ViewRole1DeleteLesson();

		populateComboBox();

		ControllerRole1DeleteLesson.doSelectLesson();
		ControllerRole1DeleteLesson.repaintTheWindow();
	}

	/**********
	 * <p> Method: populateComboBox() </p>
	 *
	 * <p> Description: This method fills the ComboBox with the titles of this Contributor's own
	 * lessons.  The title is used as the entry because it is the one short field that identifies
	 * a lesson to the person who wrote it. </p>
	 *
	 */
	protected static void populateComboBox() {
		List<String> titleList = new ArrayList<String>();
		titleList.add(SELECT_PROMPT);
		for (LessonsLearned lesson : theDatabase.getAllLessons(theUser.getUserName()))
			titleList.add(lesson.getTitle());
		combobox_SelectLesson.setItems(FXCollections.observableArrayList(titleList));
		combobox_SelectLesson.getSelectionModel().select(0);
	}


	/**********
	 * <p> Method: ViewRole1DeleteLesson() </p>
	 *
	 * <p> Description: This method initializes the location, size, font, and event handler of
	 * every element of the graphical user interface; it is a singleton performed only once. </p>
	 *
	 */
	public ViewRole1DeleteLesson() {
		theRootPane = new Pane();
		theDeleteLessonScene = new Scene(theRootPane, width, height);

		label_PageTitle.setText("Delete a Lesson Learned");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("Contributor: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);

		setupLabelUI(label_SelectLesson, "Arial", 20, 300, Pos.BASELINE_LEFT, 20, 130);

		setupComboBoxUI(combobox_SelectLesson, "Dialog", 16, 400, 320, 125);
		populateComboBox();
		combobox_SelectLesson.getSelectionModel().selectedItemProperty().addListener(
			(@SuppressWarnings("unused") ObservableValue<? extends String> observable,
			 @SuppressWarnings("unused") String oldvalue,
			 @SuppressWarnings("unused") String newValue) -> {
				ControllerRole1DeleteLesson.doSelectLesson();
				ControllerRole1DeleteLesson.repaintTheWindow();
			}
		);

		setupButtonUI(button_DeleteLesson, "Dialog", 18, 300, Pos.CENTER, width/2-150, 375);
		button_DeleteLesson.setOnAction((_) -> { ControllerRole1DeleteLesson.performChange();});
		alert_DeleteLesson.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		alert_DeleteLesson.setTitle("Delete Lesson Confirmation");
		alert_DeleteLesson.setHeaderText("You are about to permanently remove this lesson!");
		alert_DeleteLesson.setContentText("Are you sure you want to proceed?");

		// GUI footer buttons
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
		setupButtonUI(button_Quit,   "Dialog", 18, 210, Pos.CENTER, 570, 540);
		button_Return.setOnAction((_) -> { ControllerRole1DeleteLesson.performReturn(); });
		button_Logout.setOnAction((_) -> { ControllerRole1DeleteLesson.performLogout(); });
		button_Quit.setOnAction((_)   -> { ControllerRole1DeleteLesson.performQuit(); });
	}

	/**********
	 * Private local method to initialize the standard fields for a label
	 *
	 * @param l		The Label object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Label
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y) {
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);
	}

	/**********
	 * Private local method to initialize the standard fields for a button
	 *
	 * @param b		The Button object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	protected static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y) {
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);
	}

	/**********
	 * Private local method to initialize the standard fields for a ComboBox
	 *
	 * @param c		The ComboBox object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the ComboBox
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	protected static void setupComboBoxUI(ComboBox <String> c, String ff, double f, double w, double x, double y) {
		c.setStyle("-fx-font: " + f + " " + ff + ";");
		c.setMinWidth(w);
		c.setLayoutX(x);
		c.setLayoutY(y);
	}
}
