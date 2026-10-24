package guiAdminListUsers;

import database.Database;
import entityClasses.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/*******
 * <p> Title: ViewAdminListUsers Class. </p>
 *
 * <p> Description: The ViewSetOTP Page is used to set a one time password for a user. </p>
 *
 * @author Edis Jakupovic
 *
 * @version 1.00
 *
 */
public class ViewAdminListUsers {
	/**
	 * The width of the application window, obtained from the main application class.
	 */
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;

	/**
	 * The height of the application window, obtained from the main application class.
	 */
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	/**
	 * Label that displays the title of the current page.
	 */
	protected static Label label_PageTitle = new Label();

	/**
	 * Label that displays details about the selected user.
	 */
	protected static Label label_UserDetails = new Label();

	/**
	 * Button that allows the administrator to update the selected user's account.
	 */
	protected static Button button_UpdateThisUser = new Button("Account Update");

	/**
	 * Horizontal line that separates the page title area from the content below it.
	 */
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

	/**
	 * Label that identifies the list of users available for selection.
	 */
	protected static Label label_SelectUser = new Label("All Users");

	/**
	 * Combo box that allows the administrator to select a user from the available users.
	 */
	protected static ComboBox <String> combobox_SelectUser = new ComboBox <String>();

	/**
	 * Table that displays information about the users registered in the application.
	 */
	protected static TableView<User> table_Users = new TableView<>();

	/**
	 * Table column that displays each user's username.
	 */
	protected static TableColumn<User, String> tablecol_Username = new TableColumn<>("Username");

	/**
	 * Table column that displays each user's name.
	 */
	protected static TableColumn<User, String> tablecol_Name = new TableColumn<>("Name");

	/**
	 * Table column that displays each user's email address.
	 */
	protected static TableColumn<User, String> tablecol_Email = new TableColumn<>("Email");

	/**
	 * Table column that displays the roles assigned to each user.
	 */
	protected static TableColumn<User, String> tablecol_Roles = new TableColumn<>("Roles");

	/**
	 * Table column that indicates whether each user has the administrator role.
	 */
	protected static TableColumn<User, String> tablecol_Roles_Admin = new TableColumn<>("Admin");

	/**
	 * Table column that indicates whether each user has the Role1 role.
	 */
	protected static TableColumn<User, String> tablecol_Roles_Role1 = new TableColumn<>("Role1");

	/**
	 * Table column that indicates whether each user has the Role2 role.
	 */
	protected static TableColumn<User, String> tablecol_Roles_Role2 = new TableColumn<>("Role2");

	/**
	 * Horizontal line that separates the user list area from the navigation controls.
	 */
	protected static Line line_Separator4 = new Line(20, 525, width-20,525);

	/**
	 * Button that allows the administrator to return to the previous page in the
	 * application's navigation flow.
	 */
	protected static Button button_Return = new Button("Return");

	/**
	 * Button that allows the current user to log out of the application.
	 */
	protected static Button button_Logout = new Button("Logout");

	/**
	 * Button that allows the user to quit the application.
	 */
	protected static Button button_Quit = new Button("Quit");

	/**
	 * Reference to the ViewAdminListUsers instance, used to determine whether
	 * the view needs to be instantiated.
	 */
	private static ViewAdminListUsers theView;

	/**
	 * Reference to the application's database, used to access stored user information.
	 */
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**
	 * JavaFX stage used to display the user list page.
	 */
	protected static Stage theStage;

	/**
	 * Pane that contains and organizes the graphical components of the page.
	 */
	protected static Pane theRootPane;

	/**
	 * User whose information is associated with the current page.
	 */
	protected static User theUser;

	/**
	 * Scene containing the graphical interface for listing application users.
	 */
	public static Scene theListUsersScene = null;

	/******* Select a user notification */
	protected static String theSelectedUser = "<Select a User>";

	/**********
	 * <p> Method: displayListUsers(Stage ps, User user) </p>
	 *
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the AddRevove page to be displayed.
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
	 * @param user specifies the User whose roles will be updated
	 *
	 */
	public static void displayListUsers(Stage ps, User user) {
		theStage = ps;
		theUser = user;
		if (theView == null) theView = new ViewAdminListUsers();
		ControllerAdminListUsers.repaintTheWindow();
	}

	/**********
	 * <p> Constructor: ViewAdminListUsers() </p>
	 *
	 * <p> Description: This constructor is called just once, the first time a new account needs to
	 * be created.  It establishes all of the common GUI widgets for the page so they are only
	 * created once and reused when needed. </p>
	 *
	 */
	public ViewAdminListUsers() {
		theRootPane = new Pane();
		theListUsersScene = new Scene(theRootPane, width, height);

		label_PageTitle.setText("List Users");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);

		setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
		button_UpdateThisUser.setOnAction((_) -> { guiUserUpdate.ViewUserUpdate.displayUserUpdate(theStage, theUser); });

        // NOTE(Edis): folowed https://docs.oracle.com/javafx/2/ui_controls/table-view.htm to build table
        table_Users.setEditable(false);
        table_Users.setLayoutX(50);
        table_Users.setLayoutY(150);
        table_Users.setPrefSize(width - 100, 250);
        table_Users.setStyle("-fx-font: 16 Arial;");
        table_Users.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        populateUsersTable();

        // NOTE(Edis): still not quite sure why SimpleStringProperty is needed,
        // but google says so and it doesn't work without :/
        tablecol_Username.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserName()));
        tablecol_Name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPreferredFirstName()));
        tablecol_Email.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmailAddress()));
        table_Users.getColumns().add(tablecol_Username);
        table_Users.getColumns().add(tablecol_Name);
        table_Users.getColumns().add(tablecol_Email);
        table_Users.getColumns().add(tablecol_Roles);

        tablecol_Roles_Admin.setStyle("-fx-alignment: CENTER;");
        tablecol_Roles_Role1.setStyle("-fx-alignment: CENTER;");
        tablecol_Roles_Role2.setStyle("-fx-alignment: CENTER;");
        tablecol_Roles_Admin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdminRole() ? "✓" : ""));
        tablecol_Roles_Role1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNewRole1() ? "✓" : ""));
        tablecol_Roles_Role2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNewRole2() ? "✓" : ""));
        tablecol_Roles.getColumns().add(tablecol_Roles_Admin);
        tablecol_Roles.getColumns().add(tablecol_Roles_Role1);
        tablecol_Roles.getColumns().add(tablecol_Roles_Role2);

		// GUI footer buttons
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
        setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
        setupButtonUI(button_Quit,   "Dialog", 18, 210, Pos.CENTER, 570, 540);
		button_Return.setOnAction((_) -> { ControllerAdminListUsers.performReturn(); });
        button_Logout.setOnAction((_) -> { ControllerAdminListUsers.performLogout(); });
        button_Quit.setOnAction((_)   -> { ControllerAdminListUsers.performQuit(); });
	}

	/**********
	 * <p> Method: populateUsersTable() </p>
	 *
	 * <p> Description: This method is used to populate `table_Users` variable.
     * It retrieves all the user info from the database and sets the table with a
     * javafx observableArrayList. </p>
	 *
	 */
    protected static void populateUsersTable() {
        // NOTE: this is only in a separate function because the table needs to be refreshed
        // as data changes, so Controller will call this
        table_Users.setItems(FXCollections.observableArrayList(theDatabase.getAllUsers()));
    }

	/**********
	 * Private local method to initialize the standard fields for a label
	 *
	 * @param l		The Label object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
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
}
