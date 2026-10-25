package lessons.guiLessonExperience;

import entityClasses.LessonExperience;
import entityClasses.TeammateEffort;
import entityClasses.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/*******
 * <p> Title: ViewLessonExperience Class. </p>
 *
 * <p> Description: The Java/FX-based Lesson Experience page. After a new attempt to leverage a
 * lesson, the contributor records here what was done, how it was done, how long it took, and the
 * effort each teammate put in (User Story 6). The list on the left holds the lesson's existing
 * entries; selecting one loads it into the form on the right to be edited or deleted.</p>
 *
 * <p> Validation errors can run to several lines, more than fits under the form, so they are
 * shown in a dialog, as the Admin pages do for their errors. Confirmations are shown in green
 * below the form.</p>
 *
 * <p> The page follows the same three-area layout as the other Foundations pages.</p>
 *
 * @author Shane McPhillips
 *
 * @version 1.00	2026-10-07 Initial version for HW2
 */

public class ViewLessonExperience {

	/*-*******************************************************************************************

	Attributes

	 */

	// These are the application values required by the user interface
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;


	// GUI Area 1: It informs the user about the purpose of this page, whose account is being used,
	// and a button to allow this user to update the account settings
	protected static Label label_PageTitle = new Label();
	protected static Label label_UserDetails = new Label();
	protected static Button button_UpdateThisUser = new Button("Account Update");

	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

	// GUI Area 2, top: which lesson this is, and whether the form is adding or editing
	protected static Label label_Lesson = new Label();
	protected static Label label_Mode = new Label();

	// GUI Area 2, left: the lesson's existing experience entries
	protected static Label label_Entries = new Label("Experience entries:");
	protected static ListView<LessonExperience> list_Experiences = new ListView<>();
	protected static Button button_NewEntry = new Button("New Entry");
	protected static Button button_DeleteEntry = new Button("Delete Entry");
	protected static Alert alert_DeleteEntry = new Alert(AlertType.CONFIRMATION);

	// GUI Area 2, right: the form for one entry
	protected static Label label_WhatWasDone = new Label("What was done (20-2,000 characters):");
	protected static TextArea text_WhatWasDone = new TextArea();
	protected static Label label_HowItWasDone = new Label("How it was done (20-2,000 characters):");
	protected static TextArea text_HowItWasDone = new TextArea();
	protected static Label label_Duration = new Label("How long it took (hours):");
	protected static TextField text_Duration = new TextField();

	protected static Label label_Teammates = new Label("Effort by each teammate:");
	protected static TextField text_TeammateName = new TextField();
	protected static TextField text_TeammateEffort = new TextField();
	protected static Button button_AddTeammate = new Button("Add");
	protected static ListView<TeammateEffort> list_Teammates = new ListView<>();
	protected static Button button_RemoveTeammate = new Button("Remove");

	protected static Button button_Save = new Button("Save New Entry");

	// Confirmations (green) and page-level problems (red) are shown here; validation errors
	// are shown in alert_Error
	protected static Label label_Message = new Label();
	protected static Alert alert_Error = new Alert(AlertType.WARNING);

	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator4 = new Line(20, 525, width-20,525);

	// GUI Area 3: This is last of the GUI areas.  It is used for returning to the My Lessons
	// page, quitting the application, and logging out.
	protected static Button button_Return = new Button("Return");
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");

	// This is the end of the GUI objects for the page.

	// These attributes are used to configure the page and populate it with this user's information
	private static ViewLessonExperience theView;	// Used to determine if instantiation of the
													// class is needed

	protected static Stage theStage;			// The Stage that JavaFX has established for us
	protected static Pane theRootPane;			// The Pane that holds all the GUI widgets
	protected static User theUser;				// The current logged in User
	protected static int theLessonId;			// The database id of the lesson
	protected static LessonExperience theSelectedExperience;	// The entry being edited, or null
																// when adding a new entry

	protected static Scene theLessonExperienceScene;	// The shared Scene each invocation
														// populates


	/*-*******************************************************************************************

	Constructors

	 */

	/**********
	 * <p> Method: displayLessonExperience(Stage ps, User user, int lessonId) </p>
	 *
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the Lesson Experience page to be displayed.
	 *
	 * It first sets up every shared attribute so we don't have to pass parameters.  It then
	 * checks to see if the page has been set up.  If not, it instantiates the class and
	 * initializes all the static aspects of the GUI widgets.
	 *
	 * Finally, it asks the Controller to load the lesson and its experience entries and show the
	 * page.</p>
	 *
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 *
	 * @param user specifies the User (the contributor) adding the experience
	 *
	 * @param lessonId specifies the database id of the lesson
	 *
	 */
	public static void displayLessonExperience(Stage ps, User user, int lessonId) {
		theStage = ps;
		theUser = user;
		theLessonId = lessonId;
		if (theView == null) theView = new ViewLessonExperience();	// Instantiate singleton
		ControllerLessonExperience.repaintTheWindow();
	}

	/**********
	 * <p> Method: ViewLessonExperience() </p>
	 *
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object.</p>
	 *
	 * <p> This is a singleton and is only performed once.  Subsequent uses fill in the changeable
	 * fields using the displayLessonExperience method.</p>
	 *
	 */
	private ViewLessonExperience() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theLessonExperienceScene = new Scene(theRootPane, width, height);

		// GUI Area 1
		label_PageTitle.setText("Lesson Experience");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);

		setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
		button_UpdateThisUser.setOnAction((_) -> {ControllerLessonExperience.performUpdate(); });

		// GUI Area 2, top
		setupLabelUI(label_Lesson, "Arial", 18, 500, Pos.BASELINE_LEFT, 20, 103);
		label_Lesson.setMaxWidth(500);		// A long title is cut short with "..."

		setupLabelUI(label_Mode, "Arial", 14, 260, Pos.BASELINE_RIGHT, 520, 107);
		label_Mode.setTextFill(Color.DARKBLUE);

		// GUI Area 2, left: the existing entries. Selecting one loads it into the form.
		setupLabelUI(label_Entries, "Arial", 14, 250, Pos.BASELINE_LEFT, 20, 135);
		list_Experiences.setLayoutX(20);
		list_Experiences.setLayoutY(155);
		list_Experiences.setPrefSize(250, 293);
		list_Experiences.setStyle("-fx-font: 13 Arial;");
		list_Experiences.setPlaceholder(new Label("No experience yet."));
		list_Experiences.getSelectionModel().selectedItemProperty().addListener(
				(_, _, newValue) -> {
					if (newValue != null) ControllerLessonExperience.performSelectExperience(newValue);
				});

		setupButtonUI(button_NewEntry, "Dialog", 14, 120, Pos.CENTER, 20, 456);
		button_NewEntry.setOnAction((_) -> {ControllerLessonExperience.performNewEntry(); });

		setupButtonUI(button_DeleteEntry, "Dialog", 14, 120, Pos.CENTER, 150, 456);
		button_DeleteEntry.setOnAction((_) -> {ControllerLessonExperience.performDeleteEntry(); });

		// The delete confirmation follows the Admin Delete User page: Yes deletes, No cancels
		alert_DeleteEntry.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		alert_DeleteEntry.setTitle("Delete Experience Confirmation");
		alert_DeleteEntry.setHeaderText("You are about to permanently delete this experience entry!");
		alert_DeleteEntry.setContentText("Do you want to delete the selected entry?");

		// GUI Area 2, right: the form
		setupLabelUI(label_WhatWasDone, "Arial", 14, 490, Pos.BASELINE_LEFT, 290, 135);
		setupTextAreaUI(text_WhatWasDone, 490, 52, 290, 155);

		setupLabelUI(label_HowItWasDone, "Arial", 14, 490, Pos.BASELINE_LEFT, 290, 212);
		setupTextAreaUI(text_HowItWasDone, 490, 52, 290, 232);

		setupLabelUI(label_Duration, "Arial", 14, 170, Pos.BASELINE_LEFT, 290, 296);
		setupTextUI(text_Duration, "e.g., 2.5", 100, 470, 292);

		setupLabelUI(label_Teammates, "Arial", 14, 490, Pos.BASELINE_LEFT, 290, 326);
		setupTextUI(text_TeammateName, "Teammate name", 130, 290, 346);
		setupTextUI(text_TeammateEffort, "What they did (20-2,000 characters)", 270, 425, 346);

		setupButtonUI(button_AddTeammate, "Dialog", 14, 80, Pos.CENTER, 700, 345);
		button_AddTeammate.setOnAction((_) -> {ControllerLessonExperience.performAddTeammate(); });

		list_Teammates.setLayoutX(290);
		list_Teammates.setLayoutY(380);
		list_Teammates.setPrefSize(405, 68);
		list_Teammates.setStyle("-fx-font: 13 Arial;");
		list_Teammates.setPlaceholder(new Label("Add at least one teammate."));

		setupButtonUI(button_RemoveTeammate, "Dialog", 14, 80, Pos.CENTER, 700, 380);
		button_RemoveTeammate.setOnAction((_) ->
				{ControllerLessonExperience.performRemoveTeammate(); });

		setupButtonUI(button_Save, "Dialog", 14, 180, Pos.CENTER, 290, 456);
		button_Save.setOnAction((_) -> {ControllerLessonExperience.performSave(); });

		setupLabelUI(label_Message, "Arial", 14, width - 40, Pos.BASELINE_LEFT, 20, 495);
		label_Message.setMaxWidth(width - 40);

		alert_Error.setTitle("Lesson Experience");
		alert_Error.setHeaderText("Please check the following:");

		// GUI Area 3
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
		button_Return.setOnAction((_) -> {ControllerLessonExperience.performReturn(); });

		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
		button_Logout.setOnAction((_) -> {ControllerLessonExperience.performLogout(); });

		setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 540);
		button_Quit.setOnAction((_) -> {ControllerLessonExperience.performQuit(); });

		// This is the end of the GUI initialization code

		// Place all of the widget items into the Root Pane's list of children
		theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, button_UpdateThisUser, line_Separator1,
			label_Lesson, label_Mode, label_Entries, list_Experiences, button_NewEntry,
			button_DeleteEntry, label_WhatWasDone, text_WhatWasDone, label_HowItWasDone,
			text_HowItWasDone, label_Duration, text_Duration, label_Teammates, text_TeammateName,
			text_TeammateEffort, button_AddTeammate, list_Teammates, button_RemoveTeammate,
			button_Save, label_Message,
			line_Separator4, button_Return, button_Logout, button_Quit);
	}


	/*-********************************************************************************************

	Display helpers used by the Controller

	 */

	/**********
	 * Enable or disable every widget in GUI Area 2. The form is disabled when the lesson cannot
	 * be used, so nothing can be typed that could never be saved.
	 *
	 * @param disabled	true to disable the form
	 */
	protected static void setFormDisabled(boolean disabled) {
		list_Experiences.setDisable(disabled);
		button_NewEntry.setDisable(disabled);
		button_DeleteEntry.setDisable(disabled);
		text_WhatWasDone.setDisable(disabled);
		text_HowItWasDone.setDisable(disabled);
		text_Duration.setDisable(disabled);
		text_TeammateName.setDisable(disabled);
		text_TeammateEffort.setDisable(disabled);
		button_AddTeammate.setDisable(disabled);
		list_Teammates.setDisable(disabled);
		button_RemoveTeammate.setDisable(disabled);
		button_Save.setDisable(disabled);
	}

	/**********
	 * Show one or more error messages in a dialog, so every line can be read.
	 *
	 * @param message	the error message(s), one per line
	 */
	protected static void showError(String message) {
		label_Message.setText("");
		alert_Error.setContentText(message);
		alert_Error.showAndWait();
	}

	/**********
	 * Show a problem with the page as a whole (for example, the lesson could not be found) in red
	 * below the form, where it stays while the form is disabled.
	 *
	 * @param message	the message
	 */
	protected static void showProblem(String message) {
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
	 * @param prompt	The grey hint shown while the field is empty
	 * @param w		The width of the TextField
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private static void setupTextUI(TextField t, String prompt, double w, double x, double y){
		t.setFont(Font.font("Arial", 13));
		t.setPromptText(prompt);
		t.setPrefWidth(w);
		t.setLayoutX(x);
		t.setLayoutY(y);
	}

	/**********
	 * Private local method to initialize the standard fields for a text area
	 *
	 * @param t		The TextArea object to be initialized
	 * @param w		The width of the TextArea
	 * @param h		The height of the TextArea
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private static void setupTextAreaUI(TextArea t, double w, double h, double x, double y){
		t.setFont(Font.font("Arial", 13));
		t.setWrapText(true);
		t.setPrefSize(w, h);
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
