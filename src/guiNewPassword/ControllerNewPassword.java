package guiNewPassword;


import cse360.GRP.ADES.evaluator.PasswordEvaluator;
import database.Database;
import javafx.stage.Stage;

/*******
 * <p> Title: ControllerNewPassword Class. </p>
 *
 * <p> Description: The Java/FX-based New Account Page.  This class provides the controller actions
 * to allow the user to establish a new password.
 *
 * The controller deals with the user pressing the "Change Password" button widget being click.  If also
 * supports the user click on the "Quit" button widget.
 *
 */
public class ControllerNewPassword {
	/**
	 * Default constructor is not used.
	 */
	public ControllerNewPassword() {}

	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**********
	 * <p> Method: public doChangePassword() </p>
	 *
	 * <p> Description: This method is called when the user has clicked on the Change Password button.
     * If password is valid, it will update the password information to the database. </p>
	 *
	 */
	protected static void doChangePassword() {
        String currentUsername = theDatabase.getCurrentUsername();
		String newPassword = ViewNewPassword.text_Password1.getText();

        String passwordErrorMessage = PasswordEvaluator.evaluatePassword(newPassword);
        boolean passwordsMatch =
            ViewNewPassword.text_Password1.getText().compareTo(ViewNewPassword.text_Password2.getText()) == 0;
        if (!passwordsMatch) {
            System.out.println("*** ControllerNewPassword.doChangePassword(): passwords don't match");
			// The two passwords are NOT the same, so clear the passwords, explain the passwords
			// must be the same, and clear the message as soon as the first character is typed.
			ViewNewPassword.text_Password1.setText("");
			ViewNewPassword.text_Password2.setText("");
			ViewNewPassword.alert_PasswordMismatchError.showAndWait();
        } else if (!passwordErrorMessage.equals("")) {
            System.out.println("*** ControllerNewPassword.doChangePassword(): password has errors");
            ViewNewPassword.alert_PasswordInvalidError.setContentText(passwordErrorMessage);
            ViewNewPassword.alert_PasswordInvalidError.showAndWait();
        } else {
            System.out.println("*** ControllerNewPassword.doChangePassword(): newPassword = " + newPassword);
            theDatabase.updatePassword(currentUsername, newPassword);
            Stage ts = ViewNewPassword.theStage;
            // NOTE: we return user to login page after resetting password
		    guiUserLogin.ViewUserLogin.displayUserLogin(ts);
		}
	}

	/**********
	 * <p> Method: public performQuit() </p>
	 *
	 * <p> Description: This method is called when the user has clicked on the Quit button.  Doing
	 * this terminates the execution of the application.  All important data must be stored in the
	 * database, so there is no cleanup required.  (This is important so we can minimize the impact
	 * of crashed.)
	 *
	 */
	protected static void performQuit() {
		System.out.println("Perform Quit");
		System.exit(0);
	}
}
