package guiRole1UpdateLesson;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import cse360.GRP.ADES.evaluator.textLengthEvaluation.TextLengthChangeListener;


/*******
 * <p> Title: ViewRole1UpdateLesson Class. </p>
 *
 * <p> Description: The Java/FX-based Role1 Update Lesson Page, which lets the Contributor change
 * the core information and the experience of a lesson already added. </p>
 *
 * @author Edis Jakupovic
 *
 */

public class ViewRole1UpdateLesson {
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	// This is the first entry of the ComboBox.  It is a prompt rather than a lesson, which is how
	// the page knows that nothing has been chosen yet and the editing widgets must stay hidden.
	protected static final String SELECT_PROMPT = "<Select a Lesson>";

	// Both text areas hold prose and share the limit the Add Lesson page uses, which keeps what a
	// Contributor may type the same wherever a lesson is written.
	protected static final int MAX_TEXT_AREA_LENGTH = 1000;

	protected static Label label_PageTitle = new Label();
	protected static Label label_UserDetails = new Label();
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

	protected static Label label_SelectLesson = new Label("Select a lesson to update: ");
	protected static ComboBox <String> combobox_SelectLesson = new ComboBox <String>();

	// The title identifies the lesson in the database, so it is shown as text rather than in a
	// field.  Changing it here would move the lesson rather than edit it, and a Contributor
	// renaming a lesson is a separate operation this page does not offer.
	protected static Label label_Title = new Label("Title:");
	protected static Label label_TitleValue = new Label();

	protected static Label label_CoreInformation =
			new Label("Core Information (" + MAX_TEXT_AREA_LENGTH + " characters maximum):");
	protected static TextArea text_CoreInformation = new TextArea();
	protected static Label label_Experience =
			new Label("Experience (" + MAX_TEXT_AREA_LENGTH + " characters maximum):");
	protected static TextArea text_Experience = new TextArea();

	protected static Button button_UpdateLesson = new Button("Update Lesson");

	// These alerts report the outcome of an attempt to update.  They are separate objects so the
	// heading of each states plainly whether the change was stored or not.
	protected static Alert alert_UpdateFailed = new Alert(AlertType.WARNING);
	protected static Alert alert_UpdateSucceeded = new Alert(AlertType.INFORMATION);

	protected static Line line_Separator4 = new Line(20, 525, width-20,525);
	protected static Button button_Return = new Button("Return");
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");

	private static ViewRole1UpdateLesson theView;
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	protected static Stage theStage;
	protected static Pane theRootPane;
	protected static User theUser;
	public static Scene theUpdateLessonScene = null;

	// NOTE: we initialize the selected lesson to the prompt because that is the initial value of
	// the combo box.  If not set to this, the else branch of repaintTheWindow would run before
	// anything had been chosen.
	protected static String theSelectedLesson = SELECT_PROMPT;

	/**********
	 * <p> Method: displayRole1UpdateLesson(Stage ps, User user) </p>
	 *
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the Update Lesson page to be displayed.  The ComboBox is refilled on every visit, as a
	 * lesson may have been added or removed since the last time this page was shown. </p>
	 *
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 *
	 * @param user specifies the User for this GUI and it's methods
	 *
	 */
	public static void displayRole1UpdateLesson(Stage ps, User user) {
		theStage = ps;
		theUser = user;

		if (theView == null) theView = new ViewRole1UpdateLesson();

		populateComboBox();

		ControllerRole1UpdateLesson.doSelectLesson();
		ControllerRole1UpdateLesson.repaintTheWindow();
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
	 * <p> Method: ViewRole1UpdateLesson() </p>
	 *
	 * <p> Description: This method initializes the location, size, font, and event handler of
	 * every element of the graphical user interface; it is a singleton performed only once. </p>
	 *
	 */
	public ViewRole1UpdateLesson() {
		theRootPane = new Pane();
		theUpdateLessonScene = new Scene(theRootPane, width, height);

		label_PageTitle.setText("Update a Lesson Learned");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("Contributor: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);

		// Establish the headings of the two outcome alerts
		alert_UpdateFailed.setTitle("Lesson Not Updated");
		alert_UpdateFailed.setHeaderText("This lesson was not changed");

		alert_UpdateSucceeded.setTitle("Lesson Updated");
		alert_UpdateSucceeded.setHeaderText("This lesson has been changed");

		setupLabelUI(label_SelectLesson, "Arial", 20, 280, Pos.BASELINE_LEFT, 20, 120);

		setupComboBoxUI(combobox_SelectLesson, "Dialog", 16, 400, 300, 115);
		populateComboBox();
		combobox_SelectLesson.getSelectionModel().selectedItemProperty().addListener(
			(@SuppressWarnings("unused") ObservableValue<? extends String> observable,
			 @SuppressWarnings("unused") String oldvalue,
			 @SuppressWarnings("unused") String newValue) -> {
				ControllerRole1UpdateLesson.doSelectLesson();
				ControllerRole1UpdateLesson.repaintTheWindow();
			}
		);

		setupLabelUI(label_Title, "Arial", 18, 60, Pos.BASELINE_LEFT, 20, 165);
		setupLabelUI(label_TitleValue, "Arial", 18, 600, Pos.BASELINE_LEFT, 85, 165);

		setupLabelUI(label_CoreInformation, "Arial", 18, 500, Pos.BASELINE_LEFT, 20, 195);
		setupTextAreaUI(text_CoreInformation, "Arial", 16, 740, 120, 20, 218, true);
		text_CoreInformation.textProperty().addListener(
				new TextLengthChangeListener(text_CoreInformation, MAX_TEXT_AREA_LENGTH));

		setupLabelUI(label_Experience, "Arial", 18, 500, Pos.BASELINE_LEFT, 20, 348);
		setupTextAreaUI(text_Experience, "Arial", 16, 740, 90, 20, 371, true);
		text_Experience.textProperty().addListener(
				new TextLengthChangeListener(text_Experience, MAX_TEXT_AREA_LENGTH));

		// The one action of this page is centered beneath the fields it acts on
		setupButtonUI(button_UpdateLesson, "Dialog", 18, 250, Pos.CENTER, width/2-125, 478);
		button_UpdateLesson.setOnAction((_) -> { ControllerRole1UpdateLesson.performUpdate(); });

		// GUI footer buttons
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
		setupButtonUI(button_Quit,   "Dialog", 18, 210, Pos.CENTER, 570, 540);
		button_Return.setOnAction((_) -> { ControllerRole1UpdateLesson.performReturn(); });
		button_Logout.setOnAction((_) -> { ControllerRole1UpdateLesson.performLogout(); });
		button_Quit.setOnAction((_)   -> { ControllerRole1UpdateLesson.performQuit(); });
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

	/**********
	 * Private local method to initialize the standard fields for a text area, which needs a
	 * height as well as a width and is set to wrap long lines
	 *
	 * @param t		The TextArea object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the TextArea
	 * @param h		The height of the TextArea
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 * @param e		The editable state of the TextArea
	 */
	private static void setupTextAreaUI(TextArea t, String ff, double f, double w, double h,
			double x, double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setMinHeight(h);
		t.setMaxHeight(h);
		t.setLayoutX(x);
		t.setLayoutY(y);
		t.setWrapText(true);
		t.setEditable(e);
	}
}
