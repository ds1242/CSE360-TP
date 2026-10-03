package guiLessonsList;

import entityClasses.Lesson;
import guiLessonsList.ViewLessonList;

/*******
 * <p> Title: ControllerLessonList Class. </p>
 * 
 * <p> Description: The Java/FX-based Controller Lesson List.  This class provides the controller
 * actions basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This page is a stub for establish future roles for the application.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * <p> Copyright: David Shaw © 2026 </p>
 * 
 * @author David Shaw
 * 
 * @version 1.00		2026-10-02 Initial version
 */

public class ControllerLessonList {

	/********************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	 */

	/**
	 * Default constructor is not used.
	 */
	public ControllerLessonList() {
	}
	
	/**********
	 * <p> Method: repaintTheWindow() </p>
	 *
	 * <p> Description: This method determines the current state of the window and then establishes
	 * the appropriate list of widgets in the Pane to show the proper set of current values. </p>
	 *
	 */
	protected static void repaintTheWindow() {
		ViewLessonList.refreshLessons();
		ViewLessonList.theRootPane.getChildren().clear();
		ViewLessonList.theRootPane.getChildren().addAll(
				ViewLessonList.label_PageTitle,
				ViewLessonList.label_UserDetails,
				ViewLessonList.button_AddLesson,
				ViewLessonList.line_Separator1,
				ViewLessonList.listView_Lessons,
				ViewLessonList.line_Separator4,
				ViewLessonList.button_Logout,
				ViewLessonList.button_Quit
        );

		ViewLessonList.theStage.setTitle("Lesson List");
		ViewLessonList.theStage.setScene(ViewLessonList.theViewLessonListScene);
		ViewLessonList.theStage.show();
	}

	/**********
	 * <p> Method: performAddLesson() </p>
	 * 
	 * <p> Description: This method directs the user to the User Update Page so the user can change
	 * the user account attributes. </p>
	 * 
	 */
	protected static void performAddLesson () {
		guiEditLesson.ViewEditLesson.displayEditLesson(ViewLessonList.theStage, ViewLessonList.theUser, null);
	}	
	
	/**********
	 * <p> Method: performAddLesson() </p>
	 * 
	 * <p> Description: This method directs the user to the User Update Page so the user can change
	 * the user account attributes. </p>
	 * 
	 */
	protected static void performEditLesson (Lesson lesson) {
		guiEditLesson.ViewEditLesson.displayEditLesson(ViewLessonList.theStage, ViewLessonList.theUser, lesson);
	}	

	/**********
	 * <p> Method: performLogout() </p>
	 * 
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page where existing users can log in or potential new users with a invitation code can
	 * start the process of setting up an account. </p>
	 * 
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewLessonList.theStage);
	}
	
	/**********
	 * <p> Method: performQuit() </p>
	 * 
	 * <p> Description: This method terminates the execution of the program.  It leaves the
	 * database in a state where the normal login page will be displayed when the application is
	 * restarted.</p>
	 * 
	 */	
	protected static void performQuit() {
		System.exit(0);
	}
}
