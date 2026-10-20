package guiRole1AddLesson;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import entityClasses.User;
import cse360.GRP.ADES.evaluator.textLengthEvaluation.TextLengthChangeListener;
import cse360.GRP.ADES.evaluator.textLengthEvaluation.TextLengthEvaluator;


/*******
 * <p> Title: ViewRole1AddLesson Class. </p>
 *
 * <p> Description: The Java/FX-based Role1 Add Lesson Page, which gathers the title and the core
 * information of a new lesson learned from a Contributor. </p>
 *
 * @author Edis Jakupovic
 *
 */

public class ViewRole1AddLesson {

	/*-*******************************************************************************************

	Attributes

	 */

	// These are the application values required by the user interface

	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	protected static final int MAX_TITLE_LENGTH = TextLengthEvaluator.MAX_TEXT_LENGTH;
	protected static final int MAX_CORE_INFORMATION_LENGTH = 1000;

	// These are the widget attributes for the GUI. There are 3 areas for this GUI.

	// GUI Area 1: It informs the user about the purpose of this page and names the account the
	// lesson will be attributed to
	protected static Label label_PageTitle = new Label();
	protected static Label label_UserDetails = new Label();

	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

	// GUI Area 2: These are the widgets that gather the new lesson.  The title is a TextField
	// because a title is one short phrase, while the core information is a TextArea because it is
	// the lesson itself and runs to paragraphs that a single line could neither show nor accept.
	protected static Label label_Title =
			new Label("Title (" + MAX_TITLE_LENGTH + " characters maximum):");
	protected static TextField text_Title = new TextField();
	protected static Label label_CoreInformation =
			new Label("Core Information (" + MAX_CORE_INFORMATION_LENGTH + " characters maximum):");
	protected static TextArea text_CoreInformation = new TextArea();
	protected static Button button_SaveLesson = new Button("Save Lesson");

	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator4 = new Line(20, 525, width-20,525);

	// GUI Area 3: This is the last of the GUI areas.  It is used for returning to the previous
	// page, for logging out, and for quitting the application.
	protected static Button button_Return = new Button("Return");
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");

	// These alerts report the outcome of an attempt to save.  They are separate objects so the
	// heading of each states plainly whether the lesson was stored or not.
	protected static Alert alert_SaveFailed = new Alert(AlertType.WARNING);
	protected static Alert alert_SaveSucceeded = new Alert(AlertType.INFORMATION);

	// This is the end of the GUI objects for the page.

	// These attributes are used to configure the page and populate it with this user's information
	private static ViewRole1AddLesson theView;	// Used to determine if instantiation of the class
												// is needed

	protected static Stage theStage;			// The Stage that JavaFX has established for us
	protected static Pane theRootPane;			// The Pane that holds all the GUI widgets
	protected static User theUser;				// The current logged in User


	private static Scene theViewRole1AddLessonScene;	// The shared Scene each invocation populates

	/*-*******************************************************************************************

	Constructors

	 */


	/**********
	 * <p> Method: displayRole1AddLesson(Stage ps, User user) </p>
	 *
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the Role1 Add Lesson page to be displayed.  Both input widgets are emptied here, as they are
	 * static and would otherwise still hold the previous visit's text. </p>
	 *
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 *
	 * @param user specifies the User for this GUI and it's methods
	 *
	 */
	public static void displayRole1AddLesson(Stage ps, User user) {

		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;

		// If not yet established, populate the static aspects of the GUI
		if (theView == null) theView = new ViewRole1AddLesson();	// Instantiate singleton if needed

		// Populate the dynamic aspects of the GUI with the data from the user and the current
		// state of the system.
		label_UserDetails.setText("Contributor: " + theUser.getUserName());

		// Start with an empty form so nothing from a previous visit is stored by accident
		text_Title.clear();
		text_CoreInformation.clear();

		// Set the title for the window, display the page, and wait for the user to do something
		theStage.setTitle("CSE 360 Foundations: Add a Lesson Learned");
		theStage.setScene(theViewRole1AddLessonScene);
		theStage.show();
	}

	/**********
	 * <p> Method: ViewRole1AddLesson() </p>
	 *
	 * <p> Description: This method initializes the location, size, font, and event handler of
	 * every element of the graphical user interface; it is a singleton performed only once. </p>
	 *
	 */
	private ViewRole1AddLesson() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theViewRole1AddLessonScene = new Scene(theRootPane, width, height);	// Create the scene

		// Establish the headings of the two outcome alerts
		alert_SaveFailed.setTitle("Lesson Not Added");
		alert_SaveFailed.setHeaderText("This lesson was not added to the system");

		alert_SaveSucceeded.setTitle("Lesson Added");
		alert_SaveSucceeded.setHeaderText("This lesson has been added to the system");

		// Populate the window with the title and other common widgets and set their static state

		// GUI Area 1
		label_PageTitle.setText("Add a Lesson Learned");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("Contributor: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);

		// GUI Area 2
		setupLabelUI(label_Title, "Arial", 18, 400, Pos.BASELINE_LEFT, 20, 110);
		setupTextUI(text_Title, "Arial", 18, 500, Pos.BASELINE_LEFT, 20, 140, true);

		text_Title.textProperty().addListener(
				new TextLengthChangeListener(text_Title, MAX_TITLE_LENGTH));

		// Wrapping is turned on so a long paragraph stays visible instead of scrolling sideways
		// out of view.
		setupLabelUI(label_CoreInformation, "Arial", 18, 500, Pos.BASELINE_LEFT, 20, 190);
		setupTextAreaUI(text_CoreInformation, "Arial", 16, 740, 240, 20, 220, true);
		text_CoreInformation.textProperty().addListener(
				new TextLengthChangeListener(text_CoreInformation, MAX_CORE_INFORMATION_LENGTH));

		// The one action of this page is centered directly beneath the field it acts on
		setupButtonUI(button_SaveLesson, "Dialog", 18, 250, Pos.CENTER, width/2-125, 475);
		button_SaveLesson.setOnAction((_) -> {ControllerRole1AddLesson.performSaveLesson(); });

		// GUI Area 3
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
		button_Return.setOnAction((_) -> {ControllerRole1AddLesson.performReturn(); });

		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
		button_Logout.setOnAction((_) -> {ControllerRole1AddLesson.performLogout(); });

		setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 540);
		button_Quit.setOnAction((_) -> {ControllerRole1AddLesson.performQuit(); });

		// This is the end of the GUI initialization code

		// Place all of the widget items into the Root Pane's list of children
         theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, line_Separator1,
			label_Title, text_Title, label_CoreInformation, text_CoreInformation,
			button_SaveLesson,
	        line_Separator4, button_Return, button_Logout, button_Quit);
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


	/**********
	 * Private local method to initialize the standard fields for a text field
	 *
	 * @param t		The TextField object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the TextField
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 * @param e		The editable state of the TextField
	 */
	private static void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x,
			double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);
		t.setEditable(e);
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

