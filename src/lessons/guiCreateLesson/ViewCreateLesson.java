package lessons.guiCreateLesson;

import entityClasses.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
 * <p>
 * Title: ViewCreateLesson Class.
 * </p>
 *
 * <p>
 * Description: The Java/FX-based Create Lesson page. A contributor uses this
 * page to add a new lesson learned to the system by entering its "core
 * information": title, description, category, and effort (User Story 1).
 * </p>
 *
 * <p>
 * The page follows the same three-area layout as the other Foundations pages:
 * Area 1 shows the page title, the user, and the Account Update button; Area 2
 * holds the lesson form; Area 3 holds the Return, Logout, and Quit buttons.
 * Like the other pages, it is a singleton: the widgets are created once, and
 * later visits only reset the form.
 * </p>
 *
 * <p>
 * The View only creates, places, and displays widgets. Every button hands its
 * work to ControllerCreateLesson, and the rules for a valid lesson live in
 * lessons.LessonsEvaluator.
 * </p>
 *
 * @author Shane McPhillips
 *
 * @version 1.00 2026-10-07 Initial version for HW2
 */

public class ViewCreateLesson {

	/*-*******************************************************************************************
	
	Attributes
	
	 */

	// These are the application values required by the user interface
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

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

	// This is a separator and it is used to partition the GUI for various tasks
	/**
	 * Horizontal line that separates the page title area from the content below it.
	 */
	protected static Line line_Separator1 = new Line(20, 95, width - 20, 95);

	/**
	 * Label that identifies the lesson title field and displays the permitted
	 * character limits.
	 */
	protected static Label label_Title = new Label(
			"Title (" + LessonsEvaluator.TITLE_MIN + "-" + LessonsEvaluator.TITLE_MAX + " characters):");

	/**
	 * Text field where the contributor enters the lesson title.
	 */
	protected static TextField text_Title = new TextField();

	/**
	 * Label that identifies the lesson description field and displays its character
	 * limit.
	 */
	protected static Label label_Description = new Label(
			"Description (" + LessonsEvaluator.DESCRIPTION_MIN + "-2,000 characters):");

	/**
	 * Text area where the contributor enters the lesson description.
	 */
	protected static TextArea text_Description = new TextArea();

	/**
	 * Label that displays the current character count for the lesson description.
	 */
	protected static Label label_DescriptionCount = new Label();

	/**
	 * Label that identifies the lesson category selection field.
	 */
	protected static Label label_Category = new Label("Category:");

	/**
	 * Combo box that allows the contributor to select a category for the lesson.
	 */
	protected static ComboBox<String> combobox_Category = new ComboBox<String>();

	/**
	 * Label that identifies the lesson effort field, measured in hours.
	 */
	protected static Label label_Effort = new Label("Effort (hours):");

	/**
	 * Text field where the contributor enters the estimated effort required for the
	 * lesson.
	 */
	protected static TextField text_Effort = new TextField();

	/**
	 * Button that saves the lesson information entered in the form.
	 */
	protected static Button button_Save = new Button("Save");

	/**
	 * Button that clears the information entered in the lesson form.
	 */
	protected static Button button_Clear = new Button("Clear");

	/**
	 * Button that opens the page displaying the contributor's lessons.
	 */
	protected static Button button_ViewLessons = new Button("View My Lessons");

	/**
	 * Label that displays validation errors or confirmation messages.
	 */
	protected static Label label_Message = new Label();

	// This is a separator and it is used to partition the GUI for various tasks
	/**
	 * Horizontal line that separates the main content area from the bottom
	 * navigation controls.
	 */
	protected static Line line_Separator4 = new Line(20, 525, width - 20, 525);

	/**
	 * Button that allows the contributor to return to the previous page.
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

	// These attributes are used to configure the page and populate it with this
	// user's information
	private static ViewCreateLesson theView; // Used to determine if instantiation of the class
												// is needed
	/**
	 * The JavaFX stage used to display the current page.
	 */
	protected static Stage theStage;

	/**
	 * The pane that contains and organizes all GUI widgets on the page.
	 */
	protected static Pane theRootPane;

	/**
	 * The currently logged-in user who will own the lessons created on this page.
	 */
	protected static User theUser;
	private static Scene theCreateLessonScene; // The shared Scene each invocation populates // populates

	/*-*******************************************************************************************
	
	Constructors
	
	 */

	/**********
	 * <p>
	 * Method: displayCreateLesson(Stage ps, User user)
	 * </p>
	 *
	 * <p>
	 * Description: This method is the single entry point from outside this package
	 * to cause the Create Lesson page to be displayed.
	 *
	 * It first sets up every shared attribute so we don't have to pass parameters.
	 *
	 * It then checks to see if the page has been set up. If not, it instantiates
	 * the class and initializes all the static aspects of the GUI widgets.
	 *
	 * Each visit starts with an empty form, so a lesson half-typed on an earlier
	 * visit, or the message from the last save, is never mistaken for a new one.
	 * </p>
	 *
	 * @param ps   specifies the JavaFX Stage to be used for this GUI and it's
	 *             methods
	 *
	 * @param user specifies the User (the contributor) for this GUI and it's
	 *             methods
	 *
	 */
	public static void displayCreateLesson(Stage ps, User user) {

		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;

		// If not yet established, populate the static aspects of the GUI
		if (theView == null)
			theView = new ViewCreateLesson(); // Instantiate singleton if needed

		// Populate the dynamic aspects of the GUI with the data from the user and the
		// current
		// state of the system.
		label_UserDetails.setText("User: " + theUser.getUserName());
		ControllerCreateLesson.performClear();

		// Set the title for the window, display the page, and wait for the user to do
		// something
		theStage.setTitle("CSE 360 Foundations: Create a Lesson Learned");
		theStage.setScene(theCreateLessonScene);
		theStage.show();
	}

	/**********
	 * <p>
	 * Method: ViewCreateLesson()
	 * </p>
	 *
	 * <p>
	 * Description: This method initializes all the elements of the graphical user
	 * interface. This method determines the location, size, font, color, and change
	 * and event handlers for each GUI object.
	 * </p>
	 *
	 * <p>
	 * This is a singleton and is only performed once. Subsequent uses fill in the
	 * changeable fields using the displayCreateLesson method.
	 * </p>
	 *
	 */
	private ViewCreateLesson() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theCreateLessonScene = new Scene(theRootPane, width, height);

		// GUI Area 1
		label_PageTitle.setText("Create a Lesson Learned");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);

		setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
		button_UpdateThisUser.setOnAction((_) -> {
			ControllerCreateLesson.performUpdate();
		});

		// GUI Area 2: the form. Labels sit in a left column at x = 20 and the inputs in
		// a right
		// column at x = 270, so every input lines up regardless of label length.
		setupLabelUI(label_Title, "Arial", 16, 240, Pos.BASELINE_LEFT, 20, 112);
		setupTextUI(text_Title, "e.g., Commit small changes before refactoring", 510, 270, 108);

		setupLabelUI(label_Description, "Arial", 16, 240, Pos.BASELINE_LEFT, 20, 152);
		text_Description
				.setPromptText("What happened, what you learned, and what you would do " + "differently next time.");
		text_Description.setWrapText(true);
		text_Description.setFont(Font.font("Arial", 14));
		text_Description.setLayoutX(270);
		text_Description.setLayoutY(148);
		text_Description.setPrefSize(510, 110);

		// A live character count helps the contributor reach the 20-character minimum
		// and stay
		// under the 2,000-character maximum without guessing.
		setupLabelUI(label_DescriptionCount, "Arial", 12, 240, Pos.BASELINE_LEFT, 20, 178);
		text_Description.textProperty().addListener((_, _, newValue) -> {
			label_DescriptionCount.setText(newValue.trim().length() + " characters");
		});

		setupLabelUI(label_Category, "Arial", 16, 240, Pos.BASELINE_LEFT, 20, 280);
		combobox_Category.getItems().setAll(LessonsEvaluator.CATEGORIES);
		combobox_Category.setPromptText("<Select a Category>");
		combobox_Category.setLayoutX(270);
		combobox_Category.setLayoutY(276);
		combobox_Category.setPrefWidth(250);

		setupLabelUI(label_Effort, "Arial", 16, 240, Pos.BASELINE_LEFT, 20, 320);
		setupTextUI(text_Effort, "e.g., 3.5", 120, 270, 316);

		setupButtonUI(button_Save, "Dialog", 16, 120, Pos.CENTER, 270, 360);
		button_Save.setOnAction((_) -> {
			ControllerCreateLesson.performSave();
		});

		setupButtonUI(button_Clear, "Dialog", 16, 120, Pos.CENTER, 400, 360);
		button_Clear.setOnAction((_) -> {
			ControllerCreateLesson.performClear();
		});

		setupButtonUI(button_ViewLessons, "Dialog", 16, 180, Pos.CENTER, 600, 360);
		button_ViewLessons.setOnAction((_) -> {
			ControllerCreateLesson.performViewLessons();
		});

		// The message area wraps so several validation errors can be shown at once
		setupLabelUI(label_Message, "Arial", 15, width - 40, Pos.TOP_LEFT, 20, 405);
		label_Message.setMaxWidth(width - 40);
		label_Message.setPrefHeight(110);
		label_Message.setWrapText(true);

		// GUI Area 3
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
		button_Return.setOnAction((_) -> {
			ControllerCreateLesson.performReturn();
		});

		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
		button_Logout.setOnAction((_) -> {
			ControllerCreateLesson.performLogout();
		});

		setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 540);
		button_Quit.setOnAction((_) -> {
			ControllerCreateLesson.performQuit();
		});

		// This is the end of the GUI initialization code

		// Place all of the widget items into the Root Pane's list of children
		theRootPane.getChildren().addAll(label_PageTitle, label_UserDetails, button_UpdateThisUser, line_Separator1,
				label_Title, text_Title, label_Description, text_Description, label_DescriptionCount, label_Category,
				combobox_Category, label_Effort, text_Effort, button_Save, button_Clear, button_ViewLessons,
				label_Message, line_Separator4, button_Return, button_Logout, button_Quit);
	}

	/*-********************************************************************************************
	
	Message helpers. Errors are red and confirmations green, so the contributor can tell at a
	glance whether the save worked.
	
	 */

	/**********
	 * Show one or more error messages in red.
	 *
	 * @param message the error message(s), one per line
	 */
	protected static void showError(String message) {
		label_Message.setTextFill(Color.RED);
		label_Message.setText(message);
	}

	/**********
	 * Show a confirmation message in green.
	 *
	 * @param message the confirmation message
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
	 * @param l  The Label object to be initialized
	 * @param ff The font to be used
	 * @param f  The size of the font to be used
	 * @param w  The width of the Label
	 * @param p  The alignment (e.g. left, centered, or right)
	 * @param x  The location from the left edge (x axis)
	 * @param y  The location from the top (y axis)
	 */
	private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y) {
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);
	}

	/**********
	 * Private local method to initialize the standard fields for a text field
	 *
	 * @param t      The TextField object to be initialized
	 * @param prompt The grey hint shown while the field is empty
	 * @param w      The width of the TextField
	 * @param x      The location from the left edge (x axis)
	 * @param y      The location from the top (y axis)
	 */
	private static void setupTextUI(TextField t, String prompt, double w, double x, double y) {
		t.setFont(Font.font("Arial", 14));
		t.setPromptText(prompt);
		t.setPrefWidth(w);
		t.setLayoutX(x);
		t.setLayoutY(y);
	}

	/**********
	 * Private local method to initialize the standard fields for a button
	 *
	 * @param b  The Button object to be initialized
	 * @param ff The font to be used
	 * @param f  The size of the font to be used
	 * @param w  The width of the Button
	 * @param p  The alignment (e.g. left, centered, or right)
	 * @param x  The location from the left edge (x axis)
	 * @param y  The location from the top (y axis)
	 */
	private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y) {
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);
	}
}
