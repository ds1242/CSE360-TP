package lessons.guiContributorLessons;

import java.time.format.DateTimeFormatter;

import entityClasses.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lessons.LessonRecord;
import lessons.LessonsEvaluator;


/*******
 * <p> Title: ViewContributorLessons Class. </p>
 *
 * <p> Description: The Java/FX-based "My Lessons" page. It lists every lesson learned the
 * logged-in contributor has created, showing each lesson's title, category, effort, and the
 * date it was created (User Story 2). A lesson selected in the table can be opened on the Lesson
 * Details page (Story 3), given experience information (Story 6), or deleted (Story 5).</p>
 *
 * <p> The lessons are loaded fresh from the database every time the page is displayed, so the
 * list always reflects lessons that were added, updated, or deleted since the last visit, and
 * it shows the same lessons after the application is restarted.</p>
 *
 * <p> The table is built the same way as the table on the Admin List Users page, so both list
 * pages in the application look and behave alike.</p>
 *
 * @author Shane McPhillips
 *
 * @version 1.00	2026-10-07 Initial version for HW2
 * @version 1.01	2026-10-07 View Lesson, Add Experience, and Delete Lesson buttons
 */

public class ViewContributorLessons {

	/*-*******************************************************************************************

	Attributes

	 */

	// These are the application values required by the user interface
	private static double width = applicationMain.HW2Main.WINDOW_WIDTH;
	private static double height = applicationMain.HW2Main.WINDOW_HEIGHT;

	// The date format used in the "Date Created" column. Year first so dates also sort correctly
	// as text.
	private static final DateTimeFormatter DATE_FORMAT =
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


	// GUI Area 1: It informs the user about the purpose of this page, whose account is being used,
	// and a button to allow this user to update the account settings
	protected static Label label_PageTitle = new Label();
	protected static Label label_UserDetails = new Label();
	protected static Button button_UpdateThisUser = new Button("Account Update");

	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

	// GUI Area 2: the count of lessons, the table of lessons, a message area for database errors,
	// and a shortcut to the Create Lesson page.
	protected static Label label_LessonCount = new Label();
	protected static Button button_CreateLesson = new Button("Create Lesson");

	protected static TableView<LessonRecord> table_Lessons = new TableView<>();
	protected static TableColumn<LessonRecord, String> tablecol_Title = new TableColumn<>("Title");
	protected static TableColumn<LessonRecord, String> tablecol_Category =
			new TableColumn<>("Category");
	protected static TableColumn<LessonRecord, String> tablecol_Effort =
			new TableColumn<>("Effort (hrs)");
	protected static TableColumn<LessonRecord, String> tablecol_Created =
			new TableColumn<>("Date Created");

	// Shown inside the table when the contributor has no lessons yet (worded as in Story 2)
	protected static Label label_EmptyTable =
			new Label(LessonsEvaluator.EMPTY_LIST_MESSAGE);

	// Actions on the lesson selected in the table
	protected static Button button_ViewLesson = new Button("View Lesson");
	protected static Button button_AddExperience = new Button("Add Experience");
	protected static Button button_DeleteLesson = new Button("Delete Lesson");
	protected static Alert alert_DeleteLesson = new Alert(AlertType.CONFIRMATION);

	// Shown below the buttons: errors in red (including a failure to load the lessons) and
	// confirmations in green
	protected static Label label_Message = new Label();

	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator4 = new Line(20, 525, width-20,525);

	// GUI Area 3: This is last of the GUI areas.  It is used for returning to the Contributor
	// home page, quitting the application, and logging out.
	protected static Button button_Return = new Button("Return");
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");

	// This is the end of the GUI objects for the page.

	// These attributes are used to configure the page and populate it with this user's information
	private static ViewContributorLessons theView;	// Used to determine if instantiation of the
													// class is needed

	protected static Stage theStage;			// The Stage that JavaFX has established for us
	protected static Pane theRootPane;			// The Pane that holds all the GUI widgets
	protected static User theUser;				// The current logged in User, whose lessons are
												// listed

	protected static Scene theContributorLessonsScene;	// The shared Scene each invocation
														// populates


	/*-*******************************************************************************************

	Constructors

	 */

	/**********
	 * <p> Method: displayContributorLessons(Stage ps, User user) </p>
	 *
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the My Lessons page to be displayed.
	 *
	 * It first sets up every shared attribute so we don't have to pass parameters.  It then
	 * checks to see if the page has been set up.  If not, it instantiates the class and
	 * initializes all the static aspects of the GUI widgets.
	 *
	 * Finally, it asks the Controller to load this user's lessons and show the page.  Loading
	 * happens on every visit, not just the first, so the list is never out of date.</p>
	 *
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 *
	 * @param user specifies the User (the contributor) whose lessons are listed
	 *
	 */
	public static void displayContributorLessons(Stage ps, User user) {
		theStage = ps;
		theUser = user;
		if (theView == null) theView = new ViewContributorLessons();	// Instantiate singleton
		ControllerContributorLessons.repaintTheWindow();
	}

	/**********
	 * <p> Method: ViewContributorLessons() </p>
	 *
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object.</p>
	 *
	 * <p> This is a singleton and is only performed once.  Subsequent uses fill in the changeable
	 * fields using the displayContributorLessons method.</p>
	 *
	 */
	private ViewContributorLessons() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theContributorLessonsScene = new Scene(theRootPane, width, height);

		// GUI Area 1
		label_PageTitle.setText("My Lessons Learned");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);

		setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
		button_UpdateThisUser.setOnAction((_) -> {ControllerContributorLessons.performUpdate(); });

		// GUI Area 2
		setupLabelUI(label_LessonCount, "Arial", 18, 400, Pos.BASELINE_LEFT, 20, 112);

		setupButtonUI(button_CreateLesson, "Dialog", 16, 170, Pos.CENTER, 610, 106);
		button_CreateLesson.setOnAction((_) -> {ControllerContributorLessons.performCreateLesson(); });

		// The table follows the approach used on the Admin List Users page
		table_Lessons.setEditable(false);
		table_Lessons.setLayoutX(20);
		table_Lessons.setLayoutY(150);
		table_Lessons.setPrefSize(width - 40, 270);
		table_Lessons.setStyle("-fx-font: 15 Arial;");
		table_Lessons.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

		// The placeholder is what TableView shows when it has no rows, which is exactly the
		// "empty list" case in Story 2.
		label_EmptyTable.setFont(Font.font("Arial", 16));
		table_Lessons.setPlaceholder(label_EmptyTable);

		// Each column reads one value from a LessonRecord. SimpleStringProperty wraps the value
		// in the observable form a TableColumn requires.
		tablecol_Title.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().getTitle()));
		tablecol_Category.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().getCategory()));
		tablecol_Effort.setCellValueFactory(cellData ->
				new SimpleStringProperty(String.valueOf(cellData.getValue().getDurationHours())));
		tablecol_Created.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().getCreatedAt() == null ? ""
						: cellData.getValue().getCreatedAt().format(DATE_FORMAT)));

		// Give the title the most room, since it is the longest value in each row
		tablecol_Title.setPrefWidth(330);
		tablecol_Category.setPrefWidth(160);
		tablecol_Effort.setPrefWidth(100);
		tablecol_Effort.setStyle("-fx-alignment: CENTER;");
		tablecol_Created.setStyle("-fx-alignment: CENTER;");

		table_Lessons.getColumns().add(tablecol_Title);
		table_Lessons.getColumns().add(tablecol_Category);
		table_Lessons.getColumns().add(tablecol_Effort);
		table_Lessons.getColumns().add(tablecol_Created);

		setupButtonUI(button_ViewLesson, "Dialog", 16, 170, Pos.CENTER, 20, 430);
		button_ViewLesson.setOnAction((_) -> {ControllerContributorLessons.performViewLesson(); });

		setupButtonUI(button_AddExperience, "Dialog", 16, 170, Pos.CENTER, 205, 430);
		button_AddExperience.setOnAction((_) ->
				{ControllerContributorLessons.performAddExperience(); });

		setupButtonUI(button_DeleteLesson, "Dialog", 16, 170, Pos.CENTER, 610, 430);
		button_DeleteLesson.setOnAction((_) -> {ControllerContributorLessons.performDeleteLesson(); });

		// The delete confirmation follows the Admin Delete User page: Yes deletes, No cancels
		alert_DeleteLesson.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		alert_DeleteLesson.setTitle("Delete Lesson Confirmation");
		alert_DeleteLesson.setHeaderText("You are about to permanently delete this lesson!");

		setupLabelUI(label_Message, "Arial", 16, width - 40, Pos.TOP_LEFT, 20, 475);
		label_Message.setMaxWidth(width - 40);
		label_Message.setWrapText(true);

		// GUI Area 3
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
		button_Return.setOnAction((_) -> {ControllerContributorLessons.performReturn(); });

		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
		button_Logout.setOnAction((_) -> {ControllerContributorLessons.performLogout(); });

		setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 540);
		button_Quit.setOnAction((_) -> {ControllerContributorLessons.performQuit(); });

		// This is the end of the GUI initialization code

		// Place all of the widget items into the Root Pane's list of children
		theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, button_UpdateThisUser, line_Separator1,
			label_LessonCount, button_CreateLesson, table_Lessons, button_ViewLesson,
			button_AddExperience, button_DeleteLesson, label_Message,
			line_Separator4, button_Return, button_Logout, button_Quit);
	}


	/*-********************************************************************************************

	Message helpers. Errors are red and confirmations green, as on the Create Lesson page.

	 */

	/**********
	 * Show an error message in red.
	 *
	 * @param message	the error message
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
