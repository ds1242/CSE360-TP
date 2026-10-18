package guiAdminListUsers;

import database.Database;

/*******
 * <p> Title: ControllerAdminListUsers Class. </p>
 *
 * <p> Description: The Java/FX-based Set One-time Password Page. </p>
 *
 * @author Edis Jakupovic
 *
 * @version 1.00        2025-09-15 Initial version
 */

public class ControllerAdminListUsers {
	/**
	 * Default constructor is not used.
	 */
	public ControllerAdminListUsers() {}

	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**********
	 * <p> Method: repaintTheWindow() </p>
	 *
	 * <p> Description: This method determines the current state of the window and then establishes
	 * the appropriate list of widgets in the Pane to show the proper set of current values. </p>
	 *
	 */
	protected static void repaintTheWindow() {
		ViewAdminListUsers.theRootPane.getChildren().clear();
        ViewAdminListUsers.theRootPane.getChildren().addAll(
            ViewAdminListUsers.label_PageTitle,
            ViewAdminListUsers.label_UserDetails,
            ViewAdminListUsers.button_UpdateThisUser,
            ViewAdminListUsers.line_Separator1,
            ViewAdminListUsers.table_Users,
            ViewAdminListUsers.line_Separator4,
            ViewAdminListUsers.button_Return,
            ViewAdminListUsers.button_Logout,
            ViewAdminListUsers.button_Quit
        );
        ViewAdminListUsers.populateUsersTable();
		ViewAdminListUsers.theStage.setTitle("CSE 360 Foundation Code: Admin List Users Page");
		ViewAdminListUsers.theStage.setScene(ViewAdminListUsers.theListUsersScene);
		ViewAdminListUsers.theStage.show();
	}

	/**********
	 * <p> Method: performReturn() </p>
	 *
	 * <p> Description: This method returns the user (who must be an Admin as only admins are the
	 * only users who have access to this page) to the Admin Home page. </p>
	 *
	 */
	protected static void performReturn() {
		guiAdminHome.ViewAdminHome.displayAdminHome(ViewAdminListUsers.theStage, ViewAdminListUsers.theUser);
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
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewAdminListUsers.theStage);
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
