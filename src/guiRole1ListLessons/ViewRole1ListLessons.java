package guiRole1ListLessons;

import database.Database;
import entityClasses.LessonsLearned;
import entityClasses.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/*******
 * <p> Title: ViewRole1ListLessons Class. </p>
 *
 * <p> Description: The Java/FX-based Role1 List Lessons Page, which shows the Contributor every
 * lesson learned that Contributor has added. </p>
 *
 * @author Edis Jakupovic
 *
 */

public class ViewRole1ListLessons {
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	// A lesson's core information runs to paragraphs, so a row shows only the first few lines of
	// it.  A fixed row height is what performs that limit: the cell wraps the text and the row
	// clips whatever does not fit, which keeps every row the same height no matter how long the
	// lesson is.  The whole of a lesson is read on its own page, not in this list.
	private static final int CORE_INFORMATION_LINES = 3;
	private static final double TABLE_FONT_SIZE = 14;
	// A wrapped line of 14 point Arial occupies about one and a half times the font size, and the
	// cell adds a few points of padding above and below.  Without that allowance the third line
	// is the one the row clips.
	private static final double TABLE_ROW_HEIGHT =
			CORE_INFORMATION_LINES * TABLE_FONT_SIZE * 1.5 + 10;

	protected static Label label_PageTitle = new Label();
	protected static Label label_UserDetails = new Label();
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

	protected static TableView<LessonsLearned> table_Lessons = new TableView<>();
	protected static TableColumn<LessonsLearned, String> tablecol_Title =
			new TableColumn<>("Title");
	protected static TableColumn<LessonsLearned, String> tablecol_CoreInformation =
			new TableColumn<>("Core Information");

	protected static Line line_Separator4 = new Line(20, 525, width-20,525);
	protected static Button button_Return = new Button("Return");
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");

	private static ViewRole1ListLessons theView;
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	protected static Stage theStage;
	protected static Pane theRootPane;
	protected static User theUser;
	public static Scene theListLessonsScene = null;

	/**********
	 * <p> Method: displayRole1ListLessons(Stage ps, User user) </p>
	 *
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the List Lessons page to be displayed. </p>
	 *
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 *
	 * @param user specifies the User for this GUI and it's methods
	 *
	 */
	public static void displayRole1ListLessons(Stage ps, User user) {
		theStage = ps;
		theUser = user;
		if (theView == null) theView = new ViewRole1ListLessons();
		ControllerRole1ListLessons.repaintTheWindow();
	}

	/**********
	 * <p> Method: ViewRole1ListLessons() </p>
	 *
	 * <p> Description: This method initializes the location, size, font, and event handler of
	 * every element of the graphical user interface; it is a singleton performed only once. </p>
	 *
	 */
	public ViewRole1ListLessons() {
		theRootPane = new Pane();
		theListLessonsScene = new Scene(theRootPane, width, height);

		label_PageTitle.setText("Lessons I Have Added");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("Contributor: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);

		// NOTE(Edis): folowed https://docs.oracle.com/javafx/2/ui_controls/table-view.htm to build table
		table_Lessons.setEditable(false);
		table_Lessons.setLayoutX(50);
		table_Lessons.setLayoutY(120);
		table_Lessons.setPrefSize(width - 100, 380);
		table_Lessons.setStyle("-fx-font: " + TABLE_FONT_SIZE + " Arial;");
		table_Lessons.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
		table_Lessons.setFixedCellSize(TABLE_ROW_HEIGHT);
		table_Lessons.setPlaceholder(new Label("You have not added any lessons yet."));

		populateLessonsTable();

		// NOTE(Edis): still not quite sure why SimpleStringProperty is needed,
		// but google says so and it doesn't work without :/
		tablecol_Title.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
		tablecol_Title.setMaxWidth(250);

		tablecol_CoreInformation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCoreInformation()));
		// The default cell shows one line and an ellipsis.  This one wraps instead, so the three
		// lines the row has room for are three lines of the lesson rather than one.
		tablecol_CoreInformation.setCellFactory(column -> {
			TableCell<LessonsLearned, String> cell = new TableCell<>() {
				private final Label label_Wrapped = new Label();
				{
					label_Wrapped.setWrapText(true);
					label_Wrapped.prefWidthProperty().bind(column.widthProperty().subtract(10));
				}

				@Override
				protected void updateItem(String coreInformation, boolean empty) {
					super.updateItem(coreInformation, empty);
					if (empty || coreInformation == null) {
						setGraphic(null);
					} else {
						label_Wrapped.setText(coreInformation);
						setGraphic(label_Wrapped);
					}
				}
			};
			cell.setStyle("-fx-alignment: TOP-LEFT;");
			return cell;
		});

		table_Lessons.getColumns().add(tablecol_Title);
		table_Lessons.getColumns().add(tablecol_CoreInformation);

		// GUI footer buttons
		setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
		setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
		setupButtonUI(button_Quit,   "Dialog", 18, 210, Pos.CENTER, 570, 540);
		button_Return.setOnAction((_) -> { ControllerRole1ListLessons.performReturn(); });
		button_Logout.setOnAction((_) -> { ControllerRole1ListLessons.performLogout(); });
		button_Quit.setOnAction((_)   -> { ControllerRole1ListLessons.performQuit(); });
	}

	/**********
	 * <p> Method: populateLessonsTable() </p>
	 *
	 * <p> Description: This method fills the table with this Contributor's own lessons. </p>
	 *
	 */
	protected static void populateLessonsTable() {
		// NOTE: this is only in a separate function because the table needs to be refreshed
		// as data changes, so Controller will call this
		table_Lessons.setItems(FXCollections.observableArrayList(
				theDatabase.getAllLessons(theUser.getUserName())));
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
}
