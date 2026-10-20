package guiRole1ListLessons;

/*******
 * <p> Title: ControllerRole1ListLessons Class. </p>
 *
 * <p> Description: The controller actions for the Role1 List Lessons page, performed in response
 * to the user's use of the JavaFX GUI widgets defined by the View class. </p>
 *
 * @author Edis Jakupovic
 */

public class ControllerRole1ListLessons {

	/*-*******************************************************************************************

	User Interface Actions for this page

	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and
	the Model is often just a stub, or will be a singleton instantiated object.

	 */

	/**
	 * Default constructor is not used.
	 */
	public ControllerRole1ListLessons() {}

	/**********
	 * <p> Method: repaintTheWindow() </p>
	 *
	 * <p> Description: This method determines the current state of the window and then establishes
	 * the appropriate list of widgets in the Pane to show the proper set of current values.  The
	 * table is refilled here, so a lesson added or removed since the last visit is shown. </p>
	 *
	 */
	protected static void repaintTheWindow() {
		ViewRole1ListLessons.theRootPane.getChildren().clear();
		ViewRole1ListLessons.theRootPane.getChildren().addAll(
			ViewRole1ListLessons.label_PageTitle,
			ViewRole1ListLessons.label_UserDetails,
			ViewRole1ListLessons.line_Separator1,
			ViewRole1ListLessons.table_Lessons,
			ViewRole1ListLessons.line_Separator4,
			ViewRole1ListLessons.button_Return,
			ViewRole1ListLessons.button_Logout,
			ViewRole1ListLessons.button_Quit
		);
		ViewRole1ListLessons.populateLessonsTable();
		ViewRole1ListLessons.theStage.setTitle("CSE 360 Foundations: List Lessons Learned");
		ViewRole1ListLessons.theStage.setScene(ViewRole1ListLessons.theListLessonsScene);
		ViewRole1ListLessons.theStage.show();
	}

	/**********
	 * <p> Method: performReturn() </p>
	 *
	 * <p> Description: This method returns the user to the Role1 home page. </p>
	 *
	 */
	protected static void performReturn() {
		guiRole1.ViewRole1Home.displayRole1Home(ViewRole1ListLessons.theStage,
				ViewRole1ListLessons.theUser);
	}

	/**********
	 * <p> Method: performLogout() </p>
	 *
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page. </p>
	 *
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewRole1ListLessons.theStage);
	}

	/**********
	 * <p> Method: performQuit() </p>
	 *
	 * <p> Description: This method terminates the execution of the program. </p>
	 *
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}
