package guiCurator;

import java.time.format.DateTimeFormatter;

import entityClasses.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lessons.LessonRecord;

/*******
 * <p> Title: ViewCurator Class. </p>
 *
 * <p> Description: The JavaFX-based Curator page, built the same way as the My
 * Lessons page (a singleton View with static widgets, driven by
 * ControllerCurator). It lists every lesson and lets the Curator choose a
 * contributor and turn Avg/Sum visibility on or off for that contributor with a
 * single check box. The default for every contributor is OFF.</p>
 *
 * @author Andrew Kannas
 */
public class ViewCurator {

    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    protected static Label label_PageTitle = new Label();
    protected static Label label_UserDetails = new Label();
    protected static Button button_UpdateThisUser = new Button("Account Update");
    protected static Line line_Separator1 = new Line(20, 95, width - 20, 95);

    protected static Label label_LessonCount = new Label();
    protected static TableView<LessonRecord> table_Lessons = new TableView<>();
    protected static TableColumn<LessonRecord, String> tablecol_Title = new TableColumn<>("Title");
    protected static TableColumn<LessonRecord, String> tablecol_Category = new TableColumn<>("Category");
    protected static TableColumn<LessonRecord, String> tablecol_Effort = new TableColumn<>("Effort (hrs)");
    protected static TableColumn<LessonRecord, String> tablecol_Creator = new TableColumn<>("Contributor");
    protected static TableColumn<LessonRecord, String> tablecol_Created = new TableColumn<>("Date Created");
    protected static Button button_ViewLesson = new Button("View Lesson");

    protected static Label label_Contributor = new Label("Contributor:");
    protected static ComboBox<String> combo_Contributor = new ComboBox<>();
    protected static CheckBox check_ShowStats = new CheckBox("Show Avg and Sum to this contributor");

    protected static Label label_Message = new Label();
    protected static Line line_Separator4 = new Line(20, 525, width - 20, 525);

    protected static Button button_Return = new Button("Return");
    protected static Button button_Logout = new Button("Logout");
    protected static Button button_Quit = new Button("Quit");

    private static ViewCurator theView;

    protected static Stage theStage;
    protected static Pane theRootPane;
    protected static User theUser;
    protected static Scene theCuratorScene;

    /**********
     * <p> Method: displayCurator(Stage ps, User user) </p>
     *
     * <p> Description: The single entry point that displays the Curator page.
     * Stores the current stage and logged-in user, creates the View if it has
     * not already been created, and reloads the page information on every visit.</p>
     *
     * @param ps the JavaFX Stage to use
     * @param user the logged-in Curator
     */
    public static void displayCurator(Stage ps, User user) {
        theStage = ps;
        theUser = user;
        if (theView == null)
            theView = new ViewCurator();
        ControllerCurator.repaintTheWindow();
    }

    /**********
     * <p> Method: ViewCurator() </p>
     *
     * <p> Description: Constructs the Curator page interface. Creates the JavaFX
     * scene, configures the labels, buttons, lesson table, contributor selection
     * controls, and message area, and connects the interface controls to their
     * corresponding ControllerCurator methods.</p>
     */
    private ViewCurator() {

        theRootPane = new Pane();
        theCuratorScene = new Scene(theRootPane, width, height);

        label_PageTitle.setText("Curator: Lessons Learned");
        setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

        label_UserDetails.setText("User: " + theUser.getUserName());
        setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);

        setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
        button_UpdateThisUser.setOnAction((_) -> {
            ControllerCurator.performUpdate();
        });

        setupLabelUI(label_LessonCount, "Arial", 18, 400, Pos.BASELINE_LEFT, 20, 112);

        table_Lessons.setEditable(false);
        table_Lessons.setLayoutX(20);
        table_Lessons.setLayoutY(150);
        table_Lessons.setPrefSize(width - 40, 200);
        table_Lessons.setStyle("-fx-font: 15 Arial;");
        table_Lessons.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // Each column retrieves its displayed value directly from the corresponding LessonRecord.
        tablecol_Title.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitle()));
        tablecol_Category.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCategory()));
        tablecol_Effort
                .setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getDurationHours())));
        tablecol_Creator.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCreatorUsername()));
        tablecol_Created.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getCreatedAt() == null ? "" : c.getValue().getCreatedAt().format(DATE_FORMAT)));

        tablecol_Title.setPrefWidth(280);
        tablecol_Category.setPrefWidth(140);
        tablecol_Effort.setPrefWidth(100);
        tablecol_Creator.setPrefWidth(120);
        tablecol_Effort.setStyle("-fx-alignment: CENTER;");
        tablecol_Created.setStyle("-fx-alignment: CENTER;");

        table_Lessons.getColumns().add(tablecol_Title);
        table_Lessons.getColumns().add(tablecol_Category);
        table_Lessons.getColumns().add(tablecol_Effort);
        table_Lessons.getColumns().add(tablecol_Creator);
        table_Lessons.getColumns().add(tablecol_Created);

        setupButtonUI(button_ViewLesson, "Dialog", 16, 170, Pos.CENTER, 20, 360);
        button_ViewLesson.setOnAction((_) -> {
            ControllerCurator.performViewLesson();
        });

        setupLabelUI(label_Contributor, "Arial", 16, 100, Pos.BASELINE_LEFT, 20, 415);

        combo_Contributor.setLayoutX(125);
        combo_Contributor.setLayoutY(410);
        combo_Contributor.setPrefWidth(220);
        combo_Contributor.setPromptText("Select a contributor");
        combo_Contributor.setOnAction((_) -> {
            ControllerCurator.performSelectContributor();
        });

        check_ShowStats.setFont(Font.font("Arial", 16));
        check_ShowStats.setLayoutX(370);
        check_ShowStats.setLayoutY(414);
        check_ShowStats.setSelected(false);
        check_ShowStats.setDisable(true);
        check_ShowStats.setOnAction((_) -> {
            ControllerCurator.performToggleStats();
        });

        setupLabelUI(label_Message, "Arial", 16, width - 40, Pos.TOP_LEFT, 20, 465);
        label_Message.setMaxWidth(width - 40);
        label_Message.setWrapText(true);

        // Navigation buttons delegate their actions to the controller instead of handling navigation here.
        setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
        button_Return.setOnAction((_) -> {
            ControllerCurator.performReturn();
        });

        setupButtonUI(button_Logout, "Dialog", 18, 210, Pos.CENTER, 300, 540);
        button_Logout.setOnAction((_) -> {
            ControllerCurator.performLogout();
        });

        setupButtonUI(button_Quit, "Dialog", 18, 210, Pos.CENTER, 570, 540);
        button_Quit.setOnAction((_) -> {
            ControllerCurator.performQuit();
        });

        theRootPane.getChildren().addAll(label_PageTitle, label_UserDetails, button_UpdateThisUser, line_Separator1,
                label_LessonCount, table_Lessons, button_ViewLesson, label_Contributor, combo_Contributor,
                check_ShowStats, label_Message, line_Separator4, button_Return, button_Logout, button_Quit);
    }

    /**********
     * <p> Method: showError(String message) </p>
     *
     * <p> Description: Displays an error message in the message label.
     * Changes the message text color to red and displays the provided text.</p>
     *
     * @param message the error message
     */
    protected static void showError(String message) {
        label_Message.setTextFill(Color.RED);
        label_Message.setText(message);
    }

    /**********
     * <p> Method: showSuccess(String message) </p>
     *
     * <p> Description: Displays a confirmation message to the user by changing
     * the message label text color to green and setting its displayed text to
     * the provided confirmation message.</p>
     *
     * @param message the confirmation message to display
     */
    protected static void showSuccess(String message) {
        label_Message.setTextFill(Color.GREEN);
        label_Message.setText(message);
    }

    /**********
     * <p> Method: setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y) </p>
     *
     * <p> Description: Configures the visual appearance and positioning of a
     * JavaFX label. Sets the label's font, minimum width, alignment, and layout
     * coordinates based on the provided parameters.</p>
     *
     * @param l the JavaFX label to configure
     * @param ff the font family to apply
     * @param f the font size
     * @param w the minimum width of the label
     * @param p the alignment of the label
     * @param x the horizontal layout position
     * @param y the vertical layout position
     */
    private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y) {
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);
    }

    /**********
     * <p> Method: setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y) </p>
     *
     * <p> Description: Configures the visual appearance and positioning of a
     * JavaFX button. Sets the button's font, minimum width, alignment, and layout
     * coordinates based on the provided parameters.</p>
     *
     * @param b the JavaFX button to configure
     * @param ff the font family to apply
     * @param f the font size
     * @param w the minimum width of the button
     * @param p the alignment of the button
     * @param x the horizontal layout position
     * @param y the vertical layout position
     */
    private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y) {
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);
    }
}