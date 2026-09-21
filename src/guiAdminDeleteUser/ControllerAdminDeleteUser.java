package guiAdminDeleteUser;

import java.util.Optional;

import database.Database;
import javafx.scene.control.ButtonType;

/*******
 * <p> Title: ControllerAdminDeleteUser Class. </p>
 *
 * <p> Description: The Java/FX-based Set One-time Password Page. </p>
 *
 */
public class ControllerAdminDeleteUser {
	/**
	 * Default constructor is not used.
	 */
	public ControllerAdminDeleteUser() {}

	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**********
	 * <p> Method: doSelectUser() </p>
	 *
	 * <p> Description: This method uses the ComboBox widget, fetches which item in the ComboBox
	 * was selected (a user in this case), and establishes that user and the current user, setting
	 * easily accessible values without needing to do a query. </p>
	 *
	 */
	protected static void doSelectUser() {
		ViewAdminDeleteUser.theSelectedUser = (String)ViewAdminDeleteUser.combobox_SelectUser.getValue();
		theDatabase.getUserAccountDetails(ViewAdminDeleteUser.theSelectedUser);
	}

	/**********
	 * <p> Method: repaintTheWindow() </p>
	 *
	 * <p> Description: This method determines the current state of the window and then establishes
	 * the appropriate list of widgets in the Pane to show the proper set of current values. </p>
	 *
	 */
	protected static void repaintTheWindow() {
		ViewAdminDeleteUser.theRootPane.getChildren().clear();

		if (ViewAdminDeleteUser.theSelectedUser.compareTo("<Select a User>") == 0) {
			// Only show the request to select a user to be updated and the ComboBox
			ViewAdminDeleteUser.theRootPane.getChildren().addAll(
                ViewAdminDeleteUser.label_PageTitle, ViewAdminDeleteUser.label_UserDetails,
                ViewAdminDeleteUser.button_UpdateThisUser, ViewAdminDeleteUser.line_Separator1,
                ViewAdminDeleteUser.label_SelectUser, ViewAdminDeleteUser.combobox_SelectUser,
                ViewAdminDeleteUser.line_Separator4, ViewAdminDeleteUser.button_Return,
                ViewAdminDeleteUser.button_Logout, ViewAdminDeleteUser.button_Quit
            );
		}
		else {
			// Show all the fields as there is a selected user (as opposed to the prompt)
			ViewAdminDeleteUser.theRootPane.getChildren().addAll(
                ViewAdminDeleteUser.label_PageTitle, ViewAdminDeleteUser.label_UserDetails,
                ViewAdminDeleteUser.button_UpdateThisUser, ViewAdminDeleteUser.line_Separator1,
                ViewAdminDeleteUser.label_SelectUser,
                ViewAdminDeleteUser.combobox_SelectUser,
                ViewAdminDeleteUser.line_Separator4,
                ViewAdminDeleteUser.button_DeleteUser,
                ViewAdminDeleteUser.button_Return,
                ViewAdminDeleteUser.button_Logout,
                ViewAdminDeleteUser.button_Quit
            );
		}

		ViewAdminDeleteUser.theStage.setTitle("CSE 360 Foundation Code: Delete User Page");
		ViewAdminDeleteUser.theStage.setScene(ViewAdminDeleteUser.theDeleteUserScene);
		ViewAdminDeleteUser.theStage.show();
	}

	/**********
	 * <p> Method: performChange() </p>
	 *
	 * <p> Description: This method opens a confirmation dialog box. If yes
     * is selected, the selected user will be deleted. </p>
	 *
	 */
    protected static void performChange() {
        // NOTE: Optional<ButtonType> is needed to handle cases where dialog is closed abruptly
        Optional<ButtonType> result = ViewAdminDeleteUser.alert_DeleteUser.showAndWait();

        String stdoutMessage = result.isPresent() ? result.get().getText() : "No result present";
        System.out.println("*** ControllerAdminDeleteUser.performChange(): " + stdoutMessage);

        if (result.isPresent() && result.get() == ButtonType.YES) {
            String selectedUser = ViewAdminDeleteUser.theSelectedUser;
            theDatabase.deleteUser(selectedUser);
            System.out.print("*** ControllerAdminDeleteUser.performChange(): deleted user: " + selectedUser);
            // NOTE: need to refresh combobox entries after deleting a user
            ViewAdminDeleteUser.populateComboBox();
            // guiAdminHome.ViewAdminHome.displayAdminHome(ViewAdminDeleteUser.theStage, ViewAdminDeleteUser.theUser);
        }
    }

	/**********
	 * <p> Method: performReturn() </p>
	 *
	 * <p> Description: This method returns the user (who must be an Admin as only admins are the
	 * only users who have access to this page) to the Admin Home page. </p>
	 *
	 */
	protected static void performReturn() {
		guiAdminHome.ViewAdminHome.displayAdminHome(ViewAdminDeleteUser.theStage, ViewAdminDeleteUser.theUser);
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
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewAdminDeleteUser.theStage);
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
