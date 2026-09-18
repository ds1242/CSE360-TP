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


public class ViewAdminListUsers {
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	protected static Label label_PageTitle = new Label();
	protected static Label label_UserDetails = new Label();
	protected static Button button_UpdateThisUser = new Button("Account Update");
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

	protected static Label label_SelectUser = new Label("All Users");
	protected static ComboBox <String> combobox_SelectUser = new ComboBox <String>();

    protected static TableView<User> table_Users = new TableView<>();
    protected static TableColumn<User, String> tablecol_Username = new TableColumn<>("Username");
    protected static TableColumn<User, String> tablecol_Name = new TableColumn<>("Name");
    protected static TableColumn<User, String> tablecol_Email = new TableColumn<>("Email");

    protected static TableColumn<User, String> tablecol_Roles = new TableColumn<>("Roles");
    protected static TableColumn<User, String> tablecol_Roles_Admin = new TableColumn<>("Admin");
    protected static TableColumn<User, String> tablecol_Roles_Role1 = new TableColumn<>("Role1");
    protected static TableColumn<User, String> tablecol_Roles_Role2 = new TableColumn<>("Role2");



    protected static Line line_Separator4 = new Line(20, 525, width-20,525);
	protected static Button button_Return = new Button("Return");
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");

	private static ViewAdminListUsers theView;
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	protected static Stage theStage;
	protected static Pane theRootPane;
	protected static User theUser;
	public static Scene theListUsersScene = null;

    // NOTE: we initialize selected user to this particular value because it's the initial
    // value of the combo box. If not set to this, the else branch of
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
	 * <p> Method: ViewAdminListUsers() </p>
	 *
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object. </p>
	 *
	 * This is a singleton, so this is performed just one.  Subsequent uses fill in the changeable
	 * fields using the displayAddRempoveRoles method.</p>
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
