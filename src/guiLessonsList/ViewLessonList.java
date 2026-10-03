package guiLessonsList;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;


import database.Database;
import entityClasses.User;

import entityClasses.Lesson;


/*******
 * <p> Title: ViewLessonList Class. </p>
 * 
 * <p> Description: The Java/FX-based View Lesson List.  The page is a stub for some role needed for
 * the application.  The widgets on this page are likely the minimum number and kind for other role
 * pages that may be needed.</p>
 * 
 * <p> Copyright: David Shaw © 2026 </p>
 * 
 * @author David Shaw
 * 
 * @version 1.00		2026-10-02 Initial version
 *  
 */

public class ViewLessonList {
	
	/*********************************************************************************************

	Attributes
	
	 */
	
	// These are the application values required by the user interface
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;


	// These are the widget attributes for the GUI. There are 3 areas for this GUI.
	
	// GUI Area 1: It informs the user about the purpose of this page, whose account is being used,
	// and a button to allow this user to update the account settings
	protected static Label label_PageTitle = new Label();
	protected static Label label_UserDetails = new Label();
	protected static Button button_AddLesson = new Button("Add Lesson");
	
	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

	// GUI Area 2: 
	
	// List of the storing the lessons followed by UI elements to display
	private static List<Lesson> userLessons; 
	protected static ListView<Lesson> listView_Lessons = new ListView<>();
	private static ObservableList<Lesson> observableLessonList = FXCollections.observableArrayList();
	
	
	
	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator4 = new Line(20, 525, width-20,525);
	
	// GUI Area 3: This is last of the GUI areas.  It is used for quitting the application and for
	// logging out.
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");

	// This is the end of the GUI objects for the page.
	
	// These attributes are used to configure the page and populate it with this user's information
	private static ViewLessonList theView;		// Used to determine if instantiation of the class
												// is needed

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	protected static Stage theStage;			// The Stage that JavaFX has established for us	
	protected static Pane theRootPane;			// The Pane that holds all the GUI widgets
	protected static User theUser;				// The current logged in User
	

	public static Scene theViewLessonListScene;	// The shared Scene each invocation populates
	protected static final int theRole = 2;		// Admin: 1; Role1: 2; Role2: 3
	
	
	

	/*-*******************************************************************************************

	Constructors
	
	 */


	/**********
	 * <p> Method: displayRole1Home(Stage ps, User user) </p>
	 * 
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the Role1 Home page to be displayed.
	 * 
	 * It first sets up every shared attributes so we don't have to pass parameters.
	 * 
	 * It then checks to see if the page has been setup.  If not, it instantiates the class, 
	 * initializes all the static aspects of the GIUI widgets (e.g., location on the page, font,
	 * size, and any methods to be performed).
	 * 
	 * After the instantiation, the code then populates the elements that change based on the user
	 * and the system's current state.  It then sets the Scene onto the stage, and makes it visible
	 * to the user.
	 * 
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 * 
	 * @param user specifies the User for this GUI and it's methods
	 * 
	 */
	public static void displayLessonList(Stage ps, User user) {
		
		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;
		
		// If not yet established, populate the static aspects of the GUI
		if (theView == null) theView = new ViewLessonList();		// Instantiate singleton if needed
		
		// Populate the dynamic aspects of the GUI with the data from the user and the current
		// state of the system.
		theDatabase.getUserAccountDetails(user.getUserName());
		applicationMain.FoundationsMain.activeHomePage = theRole;
		
		label_UserDetails.setText("User: " + theUser.getUserName());

		// populate the user lesson list
		userLessons = theDatabase.getLessonsByUser(theUser.getUserName());
		
		observableLessonList.clear();
	    if (userLessons != null) {
	        observableLessonList.addAll(userLessons);
	    }
		
		
	    // Set the title for the window, display the page, and wait for the Admin to do something	
	    theStage.setTitle("Lessons Learned");	
	    theStage.setScene(theViewLessonListScene);
	    theStage.show();
	}
	
	/***********
	 * <p>Method: refreshLessons() </p>
	 * 
	 * <p>Description: this method refreshes the lessons so it updates correctly when a user clicks
	 * the delete button and removes a lesson </p>
	 * 
	 * 
	 */
	protected static void refreshLessons() {
	    userLessons = theDatabase.getLessonsByUser(theUser.getUserName());
	    observableLessonList.clear();
	    if (userLessons != null) {
	        observableLessonList.addAll(userLessons);
	    }
	}
	
	/**********
	 * <p> Method: ViewLessonList() </p>
	 * 
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object.</p>
	 * 
	 * This is a singleton and is only performed once.  Subsequent uses fill in the changeable
	 * fields using the displayRole2Home method.</p>
	 * 
	 */
	private ViewLessonList() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theViewLessonListScene = new Scene(theRootPane, width, height);	// Create the scene
		
		// Set the title for the window
		
		// Populate the window with the title and other common widgets and set their static state
		
		// GUI Area 1
		label_PageTitle.setText("Lessons Learned Page");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		
		setupButtonUI(button_AddLesson, "Dialog", 18, 170, Pos.CENTER, 610, 45);
		button_AddLesson.setOnAction((_) -> {ControllerLessonList.performAddLesson(); });
		
		// GUI Area 2
		
		listView_Lessons.setItems(observableLessonList);
		listView_Lessons.setLayoutX(20);
		listView_Lessons.setLayoutY(110);
		listView_Lessons.setPrefWidth(width - 40);
		listView_Lessons.setPrefHeight(400);
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		listView_Lessons.setCellFactory((_) -> new ListCell<Lesson>() {
			@Override
			protected void updateItem(Lesson lesson, boolean empty) {
				super.updateItem(lesson, empty);

				// if empty, just skip
				if (empty || lesson == null) {
					setText(null);
					setGraphic(null);
					return;
				}
				
				
				// initialize all of the stuff
				String createdStr = lesson.getCreatedAt() != null ? lesson.getCreatedAt().format(dtf) : "N/A";
				String updatedStr = lesson.getUpdatedAt() != null ? lesson.getUpdatedAt().format(dtf) : "N/A";
				String authorStr = "Author: " + (lesson.getLessonUsername() != null ? lesson.getLessonUsername() : "Unknown");
				Label labelAuthor = new Label(authorStr);
				Label labelTitle = new Label(lesson.getLessonTitle());
				Label labelText = new Label(lesson.getLessonText());
				Label labelDates = new Label("Created: " + createdStr + "  |  Updated: " + updatedStr);
				Button button_editLesson = new Button("Edit");
				setupButtonUI(button_editLesson, "Dialog", 12 , 60, Pos.CENTER, 0, 0);
				button_editLesson.setOnAction((_) -> {
					ControllerLessonList.performEditLesson(lesson);
				});
				Button button_deleteLesson = new Button("Delete");
				setupButtonUI(button_deleteLesson, "Dialog", 12 , 60, Pos.CENTER, 80, 0);
				button_deleteLesson.setOnAction((_) -> {
					theDatabase.deleteLesson(lesson.getId().intValue(), lesson.getLessonUsername());
					ControllerLessonList.repaintTheWindow();
				});


				labelTitle.setFont(Font.font("Arial", 16));
				labelAuthor.setFont(Font.font("Arial", 16));
				labelText.setFont(Font.font("Arial", 16));
				labelDates.setFont(Font.font("Dialog", 16));

				labelText.setWrapText(true);

				// Header
				HBox header = new HBox(labelTitle, button_editLesson, button_deleteLesson);
				HBox.setHgrow(labelTitle, Priority.ALWAYS);
				header.setSpacing(10);
				
				
				// Footer 
				HBox footer = new HBox(labelAuthor, labelDates);
				HBox.setHgrow(labelAuthor, Priority.ALWAYS);
				footer.setSpacing(20);

				// Entire row
				VBox rowLayout = new VBox(8, header, labelText, footer);
				rowLayout.setPadding(new Insets(10));

				setGraphic(rowLayout);
			}

		});

		
		// GUI Area 3
        setupButtonUI(button_Logout, "Dialog", 18, 250, Pos.CENTER, 20, 540);
        button_Logout.setOnAction((_) -> {ControllerLessonList.performLogout(); });
        
        setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 540);
        button_Quit.setOnAction((_) -> {ControllerLessonList.performQuit(); });

		// This is the end of the GUI initialization code
		
		// Place all of the widget items into the Root Pane's list of children
         theRootPane.getChildren().addAll(
			label_PageTitle, 
			label_UserDetails, 
			button_AddLesson, 
			line_Separator1, 
			listView_Lessons,
	        line_Separator4, 
	        button_Logout, 
	        button_Quit
        		 );
}
	
	
	/*********************************************************************************************

	Helper methods to reduce code length

	 */
	
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
