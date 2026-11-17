package guiAITools;

public class ControllerAITools {

	/*-*******************************************************************************************
	
	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object).
	
	 */
 
	/**
	 * Default constructor is not used.
	 */
	public ControllerAITools() {}

	/**********
	 * <p>
	 * Method: performAsk()
	 * </p>
	 
	 *
	 * <p>
	 * On success the form is cleared and a confirmation is shown, so the
	 * contributor can add another lesson straight away. On failure the form is left
	 * exactly as typed and every error is shown, so the contributor only needs to
	 * fix what is wrong.
	 * </p>
	 */
	protected static void performAsk() {
		String category = ViewAITools.combobox_ToolSelect.getValue();
		String description = ViewAITools.text_Description.getText();

		String validationError = ModelAITools.validateInput(category, description);
		if (!validationError.isEmpty()) {
		    ViewAITools.showError(validationError);
		    return;
		}

		try {
		    String aiResult = ModelAITools.askAI(category, description);
		    
		    ViewAITools.setAIResponse(aiResult);
		    ViewAITools.showSuccess("AI request completed successfully!");
		} catch (Exception e) {
		    ViewAITools.showError("Error communicating with AI: " + e.getMessage());
		}
		
	}

	/**********
	 * <p>
	 * Method: performClear()
	 * </p>
	 *
	 * <p>
	 * Description: Called when the contributor presses "Clear", and each time the
	 * page is displayed. Empties every field and removes any message.
	 * </p>
	 */
	protected static void performClear() {
		clearForm();

	}

	/**********
	 * <p>
	 * Method: performReturn()
	 * </p>
	 *
	 * <p>
	 * Description: This method returns the user to the Contributor Home page.
	 * </p>
	 */
	protected static void performReturn() {
		guiContributor.ViewContributorHome.displayContributorHome(ViewAITools.theStage, ViewAITools.theUser);
	}

	/**********
	 * <p>
	 * Method: performLogout()
	 * </p>
	 *
	 * <p>
	 * Description: This method logs out the current user and proceeds to the normal
	 * login page where existing users can log in or potential new users with a
	 * invitation code can start the process of setting up an account.
	 * </p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewAITools.theStage);
	}

	/**********
	 * <p>
	 * Method: performQuit()
	 * </p>
	 *
	 * <p>
	 * Description: This method terminates the execution of the program. It leaves
	 * the database in a state where the normal login page will be displayed when
	 * the application is restarted.
	 * </p>
	 */
	protected static void performQuit() {
		System.exit(0);
	}

	/**********
	 * <p>
	 * Method: performUpdate()
	 * </p>
	 * 
	 * <p>
	 * Description: This method directs the user to the User Update Page so the user
	 * can change the user account attributes.
	 * </p>
	 * 
	 */
	protected static void performUpdate() {
		guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewAITools.theStage, ViewAITools.theUser);
	}

	// Private helper: empty every input field. Shared by Save (after success) and
	// Clear.
	private static void clearForm() {

	}
}
