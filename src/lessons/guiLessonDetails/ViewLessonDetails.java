package lessons.guiLessonDetails;

import java.time.format.DateTimeFormatter;

import entityClasses.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lessons.LessonsEvaluator;


/*******
 * <p> Title: ViewLessonDetails Class. </p>
 *
 * <p> Description: The Java/FX-based Lesson Details page. A contributor reaches it by selecting
 * a lesson on the My Lessons page and pressing "View Lesson". It shows all of the lesson's core
 * information (Story 3), lets the contributor change the fields that are not locked and save
 * them (Story 4), and delete the lesson (Story 5).</p>
 *
 * <p> A locked field is disabled (grayed out) and its label ends in "(Locked)", so the
 * contributor can tell at a glance which fields can be managed. When any field is locked, the
 * Delete button is grayed out as well.</p>
 *
 * <p> The page follows the same three-area layout as the other Foundations pages, and the form
 * uses the same positions as the Create Lesson page so the two look alike.</p>
 *
 * @author Shane McPhillips
 *
 * @version 1.00	2026-10-07 Initial version for HW2
 */

public class ViewLessonDetails {

	/*-*******************************************************************************************

	Attributes

	 */

	// These are the application values required by the user interface
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;


	/**
	 * Formatter used to display the lesson creation date and time in the format
	 * yyyy-MM-dd HH:mm.
	 */
	protected static final DateTimeFormatter DATE_FORMAT =
	        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	
	/**
	 * Base label for the lesson title field, including the permitted character limits.
	 */
	protected static final String TITLE_LABEL = "Title (" + LessonsEvaluator.TITLE_MIN + "-"
	        + LessonsEvaluator.TITLE_MAX + " characters):";

	/**
	 * Base label for the lesson description field, including its character limit.
	 */
	protected static final String DESCRIPTION_LABEL = "Description ("
	        + LessonsEvaluator.DESCRIPTION_MIN + "-2,000 characters):";

	/**
	 * Base label for the lesson category field.
	 */
	protected static final String CATEGORY_LABEL = "Category:";

	/**
	 * Base label for the lesson effort field, measured in hours.
	 */
	protected static final String EFFORT_LABEL = "Effort (hours):";

	
	/**
	 * Label that displays the title of the current page.
	 */
	protected static Label label_PageTitle = new Label();

	/**
	 * Label that displays details about the currently logged-in user.
	 */
	protected static Label label_UserDetails = new Label();

	/**
	 * Button that allows the user to navigate to the account update page.
	 */
	protected static Button button_UpdateThisUser = new Button("Account Update");


	/**
	 * Horizontal line that separates the page title area from the content below it.
	 */
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

	// GUI Area 2: the lesson's creation date, a note explaining locks, and the lesson form
	/**
	 * Label that displays the lesson's creation date.
	 */
	protected static Label label_Created = new Label();

	/**
	 * Label that explains which lesson fields are locked and why.
	 */
	protected static Label label_LockNote = new Label();

	/**
	 * Label that identifies the lesson title field.
	 */
	protected static Label label_Title = new Label(TITLE_LABEL);

	/**
	 * Text field that displays or allows editing of the lesson title.
	 */
	protected static TextField text_Title = new TextField();

	/**
	 * Label that identifies the lesson description field.
	 */
	protected static Label label_Description = new Label(DESCRIPTION_LABEL);

	/**
	 * Text area that displays or allows editing of the lesson description.
	 */
	protected static TextArea text_Description = new TextArea();

	/**
	 * Label that displays the current character count for the lesson description.
	 */
	protected static Label label_DescriptionCount = new Label();

	/**
	 * Label that identifies the lesson category field.
	 */
	protected static Label label_Category = new Label(CATEGORY_LABEL);

	/**
	 * Combo box that displays or allows selection of the lesson category.
	 */
	protected static ComboBox <String> combobox_Category = new ComboBox <String>();

	/**
	 * Label that identifies the lesson effort field, measured in hours.
	 */
	protected static Label label_Effort = new Label(EFFORT_LABEL);

	/**
	 * Text field that displays or allows editing of the lesson effort.
	 */
	protected static TextField text_Effort = new TextField();

	/**
	 * Button that saves changes made to the lesson details.
	 */
	protected static Button button_Save = new Button("Save Changes");

	/**
	 * Button that deletes the current lesson.
	 */
	protected static Button button_Delete = new Button("Delete Lesson");

	/**
	 * Button that opens the interface for adding experience to the current lesson.
	 */
	protected static Button button_AddExperience = new Button("Add Experience");

	/**
	 * Confirmation alert displayed when the user attempts to delete the lesson.
	 */
	protected static Alert alert_DeleteLesson = new Alert(AlertType.CONFIRMATION);

	
	/**
	 * Label that displays error messages or confirmations related to lesson changes.
	 */
	protected static Label label_Message = new Label();


	/**
	 * Horizontal line that separates the main content area from the bottom navigation controls.
	 */
	protected static Line line_Separator4 = new Line(20, 525, width-20,525);

	// GUI Area 3: This is last of the GUI areas. It is used for returning to the My Lessons
	// page, quitting the application, and logging out.
	/**
	 * Button that returns the user to the My Lessons page.
	 */
	protected static Button button_Return = new Button("Return");

	/**
	 * Button that allows the user to log out of the application.
	 */
	protected static Button button_Logout = new Button("Logout");

	/**
	 * Button that allows the user to quit the application.
	 */
	protected static Button button_Quit = new Button("Quit");

	// This is the end of the GUI objects for the page.

	// These attributes are used to configure the page and populate it with this user's information
	private static ViewLessonDetails theView;		// Used to determine if instantiation of the
	                                                // class is needed

	/**
	 * JavaFX stage used to display the lesson details page.
	 */
	protected static Stage theStage;

	/**
	 * Pane that contains and organizes all GUI widgets on the page.
	 */
	protected static Pane theRootPane;

	/**
	 * The currently logged-in user.
	 */
	protected static User theUser;

	/**
	 * Database ID of the lesson being displayed.
	 */
	protected static int theLessonId;

	/**
	 * Scene containing the graphical interface for viewing and editing lesson details.
	 */
	protected static Scene theLessonDetailsScene;	// The shared Scene each invocation populates


	/*-*******************************************************************************************

	Constructors

	 */

	/**********
	 * <p> Method: displayLessonDetails(Stage ps, User user, int lessonId) </p>
	 *
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the Lesson Details page to be displayed.
	 *
	 * It first sets up every shared attribute so we don't have to pass parameters.  It then
	 * checks to see if the page has been set up.  If not, it instantiates the class and
	 * initializes all the static aspects of the GUI widgets.
	 *
	 * Finally, it asks the Controller to load the lesson and show the page.  Only the lesson's id
	 * is passed in, not the lesson itself, so the page always shows the lesson as it is in the
	 * database now.</p>
	 *
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 *
	 * @param user specifies the User (the contributor) viewing the lesson
	 *
	 * @param lessonId specifies the database id of the lesson to show
	 *
	 */
	public static void displayLessonDetails(Stage ps, User user, int lessonId) {
		theStage = ps;
		theUser = user;
		theLessonId = lessonId;
		if (theView == null) theView = new ViewLessonDetails();	// Instantiate singleton
		ControllerLessonDetails.repaintTheWindow();
	}

	/**********
	 * <p> Method: ViewLessonDetails() </p>
	 *
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object.</p>
	 *
	 * <p> This is a singleton and is only performed once.  Subsequent uses fill in the changeable
	 * fields using the displayLessonDetails method.</p>
	 *
	 */
	private ViewLessonDetails() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theLessonDetailsScene = new Scene(theRootPane, width, height);

		// GUI Area 1
		label_PageTitle.setText("Lesson Details");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);

		setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
		button_UpdateThisUser.setOnAction((_) -> {ControllerLessonDetails.performUpdate(); });

		// GUI Area 2: the form. Labels sit in a left column at x = 20 and the inputs in a right
		// column at x = 270, as on the Create Lesson page.
		setupLabelUI(label_Created, "Arial", 14, 240, Pos.BASELINE_LEFT, 20, 105);
		setupLabelUI(label_LockNote, "Arial", 14, 510, Pos.BASELINE_LEFT, 270, 105);
		label_LockNote.setTextFill(Color.DARKORANGE);

		setupLabelUI(label_Title, "Arial", 16, 240, Pos.BASELINE_LEFT, 20, 142);
		setupTextUI(text_Title, 510, 270, 138);

		setupLabelUI(label_Description, "Arial", 16, 240, Pos.BASELINE_LEFT, 20, 182);
		text_Description.setWrapText(true);
		text_Description.setFont(Font.font("Arial", 14));
		text_Description.setLayoutX(270);
		text_Description.setLayoutY(178);
		text_Description.setPrefSize(510, 100);

		// A live character count helps the contributor stay within the description limits
		setupLabelUI(label_DescriptionCount, "Arial", 12, 240, Pos.BASELINE_LEFT, 20, 208);
		text_Description.textProperty().addListener((_, _, newValue) -> {
			label_DescriptionCount.setText(newValue.trim().length() + " characters");
		});

		setupLabelUI(label_Category, "Arial", 16, 240, Pos.BASELINE_LEFT, 20, 295);
		combobox_Category.getItems().setAll(LessonsEvaluator.CATEGORIES);
		combobox_Category.setPromptText("<Select a Category>");
		combobox_Category.setLayoutX(270);
		combobox_Category.setLayoutY(291);
		combobox_Category.setPrefWidth(250);

		setupLabelUI(label_Effort, "Arial", 16, 240, Pos.BASELINE_LEFT, 20, 335);
		setupTextUI(text_Effort, 120, 270, 331);

		setupButtonUI(button_Save, "Dialog", 16, 150, Pos.CENTER, 270, 375);
		button_Save.setOnAction((_) -> {ControllerLessonDetails.performSave(); });

		setupButtonUI(button_Delete, "Dialog", 16, 150, Pos.CENTER, 430, 375);
		button_Delete.setOnAction((_) -> {ControllerLessonDetails.performDelete(); });

		setupButtonUI(button_AddExperience, "Dialog", 16, 170, Pos.CENTER, 610, 375);
		button_AddExperience.setOnAction((_) -> {ControllerLessonDetails.performAddExperience(); });

		// The delete confirmation follows the Admin Delete User page: Yes deletes, No cancels
		alert_DeleteLesson.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		alert_DeleteLesson.setTitle("Delete Lesson Confirmation");
		alert_DeleteLesson.setHeaderText("You are about to permanently delete this lesson!");
		alert_DeleteLesson.setContentText("Do you want to delete this lesson?");

		// The message area wraps so several validation errors can be shown at once
		setupLabelUI(label_Message, "Arial", 15, width - 40, Pos.TOP_LEFT, 20, 420);
		label_Message.setMaxWidth(width - 40);
		label_Message.setPrefHeight(100);
		label_Message.setWrapText(true);

		// GUI Area 3
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
		button_Return.setOnAction((_) -> {ControllerLessonDetails.performReturn(); });

		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
		button_Logout.setOnAction((_) -> {ControllerLessonDetails.performLogout(); });

		setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 540);
		button_Quit.setOnAction((_) -> {ControllerLessonDetails.performQuit(); });

		// This is the end of the GUI initialization code

		// Place all of the widget items into the Root Pane's list of children
		theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, button_UpdateThisUser, line_Separator1,
			label_Created, label_LockNote,
			label_Title, text_Title, label_Description, text_Description, label_DescriptionCount,
			label_Category, combobox_Category, label_Effort, text_Effort,
			button_Save, button_Delete, button_AddExperience, label_Message,
			line_Separator4, button_Return, button_Logout, button_Quit);
	}


	/*-********************************************************************************************

	Display helpers used by the Controller

	 */

	/**********
	 * Show whether one field is locked: a locked field is disabled (grayed out) and its label
	 * ends in "(Locked)"; an unlocked field is enabled and keeps its normal label.
	 *
	 * @param label		the field's label
	 * @param input		the field's input widget
	 * @param text		the label text without the lock marker
	 * @param locked	true if the field is locked
	 */
	protected static void showLock(Label label, Control input, String text, boolean locked) {
		label.setText(locked ? text + " (Locked)" : text);
		input.setDisable(locked);
	}

	/**********
	 * Show one or more error messages in red.
	 *
	 * @param message	the error message(s), one per line
	 */
	protected static void showError(String message) {
		label_Message.setTextFill(Color.RED);
		label_Message.setText(message);
	}

	/**********
	 * Show a confirmation message in green.
	 *
	 * @param message	the confirmation message
	 */
	protected static void showSuccess(String message) {
		label_Message.setTextFill(Color.GREEN);
		label_Message.setText(message);
	}


	/*-********************************************************************************************

	Helper methods to reduce code length

	 */

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
	private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x,
			double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);
	}

	/**********
	 * Private local method to initialize the standard fields for a text field
	 *
	 * @param t		The TextField object to be initialized
	 * @param w		The width of the TextField
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private static void setupTextUI(TextField t, double w, double x, double y){
		t.setFont(Font.font("Arial", 14));
		t.setPrefWidth(w);
		t.setLayoutX(x);
		t.setLayoutY(y);
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
	private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x,
			double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);
	}
}
