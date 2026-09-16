package guiNewPassword;

import cse360.GRP.ADES.evaluator.textLengthEvaluation.TextLengthChangeListener;
import entityClasses.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/*******
 * <p> Title: ViewNewPassword Class. </p>
 *
 * <p> Description: The ViewNewPassword Page is used setup a new password for the current user. </p>
 *
 */
public class ViewNewPassword {
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	private static Label label_ApplicationTitle = new Label("Foundation Application New Password Page");
    protected static Label label_NewUserCreation = new Label(" User Update Password.");
    protected static Label label_NewUserLine = new Label("Please enter a new password.");
    protected static PasswordField text_Password1 = new PasswordField();
    protected static PasswordField text_Password2 = new PasswordField();
    protected static Button button_ChangePassword = new Button("Change Password");

	protected static Alert alertUsernamePasswordError = new Alert(AlertType.INFORMATION);

    protected static Button button_Quit = new Button("Quit");

	// These attributes are used to configure the page and populate it with this user's information
	private static ViewNewPassword theView;		// Is instantiation of the class needed?

    private static Pane theRootPane;
	protected static Stage theStage;
	protected static User theUser;

	public static Scene theNewPasswordScene = null;

	/**********
	 * <p> Method: displayNewPassword(Stage ps, String ic) </p>
	 *
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the NewPassword page to be displayed.
	 *
	 * It first sets up very shared attributes so we don't have to pass parameters.
	 *
	 * It then checks to see if the page has been setup.  If not, it instantiates the class,
	 * initializes all the static aspects of the GUI widgets (e.g., location on the page, font,
	 * size, and any methods to be performed).
	 *
	 * After the instantiation, the code then populates the elements that change based on the user
	 * and the system's current state.  It then sets the Scene onto the stage, and makes it visible
	 * to the user.
	 *
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 *
	 */
	public static void displayNewPassword(Stage ps, User user) {
		theStage = ps;
		theUser = user;
        // NOTE: singleton pattern implemented here - theView is static class var
        // that future invocations of this method will use instead of creating new object
		if (theView == null) theView = new ViewNewPassword();

		text_Password1.setText("");
		text_Password2.setText("");

    	theRootPane.getChildren().clear();
    	theRootPane.getChildren().addAll(
            label_NewUserCreation,
            label_NewUserLine,
    		text_Password1,
            text_Password2,
            button_ChangePassword,
            button_Quit
        );

		theStage.setTitle("CSE 360 Foundation Code: User Update Password");
        theStage.setScene(theNewPasswordScene);
		theStage.show();
	}

	/**********
	 * <p> Constructor: ViewNewPassword() </p>
	 *
	 * <p> Description: This constructor is called just once, the first time a new account needs to
	 * be created.  It establishes all of the common GUI widgets for the page so they are only
	 * created once and reused when needed.
	 *
	 *
	 */
	private ViewNewPassword() {
		theRootPane = new Pane();
		theNewPasswordScene = new Scene(theRootPane, width, height);

		setupLabelUI(label_ApplicationTitle, "Arial", 28, width, Pos.CENTER, 0, 5);
    	setupLabelUI(label_NewUserCreation, "Arial", 32, width, Pos.CENTER, 0, 10);
    	setupLabelUI(label_NewUserLine, "Arial", 24, width, Pos.CENTER, 0, 70);
		setupTextUI(text_Password1, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 210, true);
		setupTextUI(text_Password2, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 260, true);
        text_Password1.setPromptText("Enter the Password");
        text_Password1.textProperty().addListener(new TextLengthChangeListener(text_Password1));
		text_Password2.setPromptText("Enter the Password Again");
		text_Password2.textProperty().addListener(new TextLengthChangeListener(text_Password2, true));

		alertUsernamePasswordError.setTitle("Passwords Do Not Match");
		alertUsernamePasswordError.setHeaderText("The two passwords must be identical.");
		alertUsernamePasswordError.setContentText("Correct the passwords and try again.");

        setupButtonUI(button_ChangePassword, "Dialog", 18, 200, Pos.CENTER, 475, 210);
        button_ChangePassword.setOnAction((_) -> { ControllerNewPassword.doChangePassword(); });

        setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 540);
        button_Quit.setOnAction((_) -> { ControllerNewPassword.performQuit(); });
	}

	/**********
	 * Private local method to initialize the standard fields for a label
	 */
	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
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
	private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);
	}

	/**********
	 * Private local method to initialize the standard fields for a text field
	 */
	private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);
		t.setEditable(e);
	}
}
