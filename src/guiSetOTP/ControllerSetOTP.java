package guiSetOTP;

import java.util.Optional;

import database.Database;
import javafx.scene.control.ButtonType;

/*******
 * <p> Title: ControllerSetOTP Class. </p>
 *
 * <p> Description: The Java/FX-based Set One-time Password Page. </p>
 *
 * @author Edis Jakupovic
 *
 * @version 1.00        2025-09-15 Initial version
 */

public class ControllerSetOTP {
	/**
	 * Default constructor is not used.
	 */
	public ControllerSetOTP() {}

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
		ViewSetOTP.theSelectedUser = (String)ViewSetOTP.combobox_SelectUser.getValue();
		theDatabase.getUserAccountDetails(ViewSetOTP.theSelectedUser);
		setupSelectedUser();
	}

    /**********
     * <p> Method: setupSelectedUser() </p>
     *
     * <p> Description: </p>
     */
    private static void setupSelectedUser() {
        if (ViewSetOTP.theSelectedUser.compareTo("<Select a User>") == 0) {
            ViewSetOTP.label_UserDetails.setText("User: " + ViewSetOTP.theUser.getUserName());
            return;
        }
        ViewSetOTP.text_SetPassword.setText("");
    }


	/**********
	 * <p> Method: repaintTheWindow() </p>
	 *
	 * <p> Description: This method determines the current state of the window and then establishes
	 * the appropriate list of widgets in the Pane to show the proper set of current values. </p>
	 *
	 */
	protected static void repaintTheWindow() {
		ViewSetOTP.theRootPane.getChildren().clear();

		if (ViewSetOTP.theSelectedUser.compareTo("<Select a User>") == 0) {
			// Only show the request to select a user to be updated and the ComboBox
			ViewSetOTP.theRootPane.getChildren().addAll(
                ViewSetOTP.label_PageTitle, ViewSetOTP.label_UserDetails,
                ViewSetOTP.button_UpdateThisUser, ViewSetOTP.line_Separator1,
                ViewSetOTP.label_SelectUser, ViewSetOTP.combobox_SelectUser,
                ViewSetOTP.line_Separator4, ViewSetOTP.button_Return,
                ViewSetOTP.button_Logout, ViewSetOTP.button_Quit
            );
		}
		else {
			// Show all the fields as there is a selected user (as opposed to the prompt)
			ViewSetOTP.theRootPane.getChildren().addAll(
                ViewSetOTP.label_PageTitle, ViewSetOTP.label_UserDetails,
                ViewSetOTP.button_UpdateThisUser, ViewSetOTP.line_Separator1,
                ViewSetOTP.label_SelectUser,
                ViewSetOTP.combobox_SelectUser,
                ViewSetOTP.line_Separator4,
                ViewSetOTP.label_SetPassword,
                ViewSetOTP.text_SetPassword,
                ViewSetOTP.button_RandomPassword,
                ViewSetOTP.button_ChangePassword,
                ViewSetOTP.button_Return,
                ViewSetOTP.button_Logout,
                ViewSetOTP.button_Quit
            );
		}

		ViewSetOTP.theStage.setTitle("CSE 360 Foundation Code: Set One-Time Password Page");
		ViewSetOTP.theStage.setScene(ViewSetOTP.theSetOTPScene);
		ViewSetOTP.theStage.show();
	}

	/**********
	 * <p> Method: performChange() </p>
	 *
	 * <p> Description: This method opens a confirmation dialog box. If yes
     * is selected, the selected user's password will be set to the one-time
     * password given in the guiSetOTP Page "set password:" field. If no is
     * selected, nothing happens. </p>
	 *
	 */
    protected static void performChange() {
        // NOTE: Optional<ButtonType> is needed to handle cases where dialog is closed abruptly
        Optional<ButtonType> result = ViewSetOTP.alert_ChangePassword.showAndWait();

        String stdoutMessage = result.isPresent() ? result.get().getText() : "No result presnet";
        System.out.println("*** ControllerSetOTP.performChange(): " + stdoutMessage);

        if (result.isPresent() && result.get() == ButtonType.YES) {
            String selectedUser = ViewSetOTP.theSelectedUser;
            String newPassword = ViewSetOTP.text_SetPassword.getText();
            theDatabase.updatePassword(selectedUser, newPassword, true);

            guiAdminHome.ViewAdminHome.displayAdminHome(ViewSetOTP.theStage, ViewSetOTP.theUser);
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
		guiAdminHome.ViewAdminHome.displayAdminHome(ViewSetOTP.theStage, ViewSetOTP.theUser);
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
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewSetOTP.theStage);
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
