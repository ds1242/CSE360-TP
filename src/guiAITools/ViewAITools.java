package guiAITools;

import java.util.List;

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

/**********
 * <p> Title: ViewAITools Class </p>
 *
 * <p> Description: The JavaFX view for the AI tools page. It builds the form, the response
 * area and the navigation buttons, and offers helper methods for the controller.
 * </p>
 */
public class ViewAITools {
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

	// GUI Area 2: The lesson form. Each label states the rule for its field, so the contributor
	// sees the limits before making a mistake rather than only after an error message.
	
	protected static Label label_Description = new Label("Description ("
			+ ModelAITools.DESCRIPTION_MIN + "-2,000 characters):");
	protected static TextArea text_Description = new TextArea();
	protected static Label label_DescriptionCount = new Label();

	protected static Label label_Category = new Label("Select which tool to use:");
	protected static ComboBox <String> combobox_ToolSelect = new ComboBox <String>();

	protected static Button button_Ask = new Button("Ask");
	protected static Button button_Clear = new Button("Clear");

	// The message area shows validation errors or status messages
	protected static Label label_Message = new Label();

	// AI Response section where the controller can send output below the buttons
	protected static TextArea text_AIResponse = new TextArea();

	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator4 = new Line(20, 525, width-20, 525);

	// GUI Area 3: This is last of the GUI areas.  It is used for returning to the Contributor
	// home page, quitting the application, and logging out.
	protected static Button button_Return = new Button("Return");
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");

	// This is the end of the GUI objects for the page.

	// These attributes are used to configure the page and populate it with this user's information
	private static ViewAITools theView;		// Used to determine if instantiation of the class is needed

	protected static Stage theStage;			// The Stage that JavaFX has established for us
	protected static Pane theRootPane;			// The Pane that holds all the GUI widgets
	protected static User theUser;				// The current logged in User

	private static Scene AIToolScene;	// The shared Scene each invocation populates
	
	/** The tool categories shown in the dropdown. */
	public static final List<String> CATEGORIES = List.of(
			"Brainstorm", "Write", "Search");


	/*-*******************************************************************************************

	Constructors

	 */

	/**********
	 * <p> Method: displayViewAITools(Stage ps, User user) </p>
	 *
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the Create Lesson page to be displayed.
	 * </p>
	 *
	 * @param ps specifies the JavaFX Stage to be used for this GUI
	 * @param user specifies the User (the contributor) for this GUI
	 */
	public static void displayViewAITools(Stage ps, User user) {

		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;

		// If not yet established, populate the static aspects of the GUI
		if (theView == null) theView = new ViewAITools();	// Instantiate singleton if needed

		// Populate the dynamic aspects of the GUI with the data from the user and current state
		label_UserDetails.setText("User: " + theUser.getUserName());
		ControllerAITools.performClear();

		// Set the title for the window, display the page, and wait for the user to do something
		theStage.setTitle("CSE 360 Foundations: Interact with an AI");
		theStage.setScene(AIToolScene);
		theStage.show();
	}
	
	/**********
	 * <p> Method: promptFor(String category) </p>
	 *
	 * <p> Description: This method returns the prompt text for the description box based on
	 * the selected category.
	 * </p>
	 *
	 * @param category the selected tool category, may be null
	 * @return the prompt text to display
	 */
	protected static String promptFor(String category) {
	    if (category == null) return "Pick a topic from the dropdown...";
	    String output = "";
	    switch (category) {
	        case "Brainstorm":
	        	output = "Enter a topic or idea you want to brainstorm...";
	        	break;
	        case "Write":
	        	output = "Enter the outline or content you want written or refined...";
	        	break;
	        case "Search":
	        	output = "Enter keywords, questions, or topics you want to search for...";
	        	break;
	        default:
	        	output = "Pick a topic from the dropdown...";
	        	break;
	    }
	    return output;
	}

	/**********
	 * <p> Method: ViewAITools() </p>
	 *
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * </p>
	 */
	private ViewAITools() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		AIToolScene = new Scene(theRootPane, width, height);

		// GUI Area 1
		label_PageTitle.setText("Create a Lesson Learned");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);

		setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
		button_UpdateThisUser.setOnAction((_) -> { ControllerAITools.performUpdate(); });

		// GUI Area 2: the form.
		setupLabelUI(label_Category, "Arial", 16, 240, Pos.BASELINE_LEFT, 20, 100);
		combobox_ToolSelect.getItems().setAll(CATEGORIES);
		combobox_ToolSelect.setPromptText("<Select a Category>");
		combobox_ToolSelect.setLayoutX(270);
		combobox_ToolSelect.setLayoutY(100);
		combobox_ToolSelect.setPrefWidth(250);
		
		combobox_ToolSelect.valueProperty().addListener((_, _, c) ->
        text_Description.setPromptText(promptFor(c)));

		setupLabelUI(label_Description, "Arial", 16, 240, Pos.BASELINE_LEFT, 20, 135);
		text_Description.setWrapText(true);
		text_Description.setFont(Font.font("Arial", 14));
		text_Description.setLayoutX(270);
		text_Description.setLayoutY(135);
		text_Description.setPrefSize(510, 110);

		// A live character count helps the contributor reach the 20-character minimum
		setupLabelUI(label_DescriptionCount, "Arial", 12, 240, Pos.BASELINE_LEFT, 20, 178);
		text_Description.textProperty().addListener((_, _, newValue) -> {
			label_DescriptionCount.setText(newValue.trim().length() + " characters");
		});

		// Buttons moved up to the left and directly below the description text (Y = 265)
		setupButtonUI(button_Ask, "Dialog", 16, 110, Pos.CENTER, 20, 265);
		button_Ask.setOnAction((_) -> { ControllerAITools.performAsk(); });

		setupButtonUI(button_Clear, "Dialog", 16, 110, Pos.CENTER, 140, 265);
		button_Clear.setOnAction((_) -> { ControllerAITools.performClear(); });

		// The message area for validation errors / status
		setupLabelUI(label_Message, "Arial", 14, width - 40, Pos.TOP_LEFT, 20, 310);
		label_Message.setMaxWidth(width - 40);
		label_Message.setPrefHeight(30);
		label_Message.setWrapText(true);

		// AI Response Section below the buttons and message area
		text_AIResponse.setWrapText(true);
		text_AIResponse.setFont(Font.font("Arial", 14));
		text_AIResponse.setPromptText("AI response will appear here...");
		text_AIResponse.setLayoutX(20);
		text_AIResponse.setLayoutY(350);
		text_AIResponse.setPrefSize(width - 40, 160);
		text_AIResponse.setEditable(false); // Set to true if users should be able to edit the output

		// GUI Area 3
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
		button_Return.setOnAction((_) -> { ControllerAITools.performReturn(); });

		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
		button_Logout.setOnAction((_) -> { ControllerAITools.performLogout(); });

		setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 540);
		button_Quit.setOnAction((_) -> { ControllerAITools.performQuit(); });

		// Place all of the widget items into the Root Pane's list of children
		theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, button_UpdateThisUser, line_Separator1,
			label_Description, text_Description, label_DescriptionCount, label_Category, 
			combobox_ToolSelect, button_Ask, button_Clear, label_Message,
			text_AIResponse, line_Separator4, button_Return, button_Logout, button_Quit);
	}


	/*-********************************************************************************************

	Controller Helper Methods for Sending Data to UI

	 */

	/**********
	 * <p> Method: setAIResponse(String response) </p>
	 *
	 * <p> Description: This method sends the AI response text from the controller to the
	 * response section below the buttons.
	 * </p>
	 *
	 * @param response The text generated by the AI
	 */
	public static void setAIResponse(String response) {
		text_AIResponse.setText(response);
	}


	/*-********************************************************************************************

	Message helpers. Errors are red and confirmations green.

	 */

	/**********
	 * <p> Method: showError(String message) </p>
	 *
	 * <p> Description: This method shows a message in red in the message area.
	 * </p>
	 *
	 * @param message the error text to display
	 */
	protected static void showError(String message) {
		label_Message.setTextFill(Color.RED);
		label_Message.setText(message);
	}

	/**********
	 * <p> Method: showSuccess(String message) </p>
	 *
	 * <p> Description: This method shows a message in green in the message area.
	 * </p>
	 *
	 * @param message the confirmation text to display
	 */
	protected static void showSuccess(String message) {
		label_Message.setTextFill(Color.GREEN);
		label_Message.setText(message);
	}


	/*-********************************************************************************************

	Helper methods to reduce code length

	 */

	/**********
	 * <p> Method: setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y) </p>
	 *
	 * <p> Description: This method sets the font, width, alignment and position of a label.
	 * </p>
	 *
	 * @param l the label to set up
	 * @param ff the font family
	 * @param f the font size
	 * @param w the minimum width
	 * @param p the alignment
	 * @param x the x position
	 * @param y the y position
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
	 * <p> Method: setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y) </p>
	 *
	 * <p> Description: This method sets the font, width, alignment and position of a button.
	 * </p>
	 *
	 * @param b the button to set up
	 * @param ff the font family
	 * @param f the font size
	 * @param w the minimum width
	 * @param p the alignment
	 * @param x the x position
	 * @param y the y position
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