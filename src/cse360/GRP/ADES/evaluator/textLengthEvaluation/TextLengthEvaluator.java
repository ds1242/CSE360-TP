package cse360.GRP.ADES.evaluator.textLengthEvaluation;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/*******
 * <p> Title: TextLengthEvaluator Class. </p>
 * 
 * <p> Description: The TextLengthEvaluator class is used as a singleton class to provide 
 * the event handler co-listener class a dialogue warning box.
 * This class also provides the home for the MAX_TEXT_LENGTH setting.
 * page. </p>
 * 
 * <p> Copyright: Shane McPhillips © 2026 </p>
 * 
 * @author Shane McPhillips
 * 
 * @version 1.00		2026-09-14 Initial version
 *  
 */

public class TextLengthEvaluator {
	private Alert textLengthAlert = new Alert(AlertType.WARNING); //Alert box
	public static final int MAX_TEXT_LENGTH = 32; //Set max length for all input texts
	
	private static TextLengthEvaluator instance = null; //Singleton instance
	
	//Constructor initializes alert box to correct settings.
	private TextLengthEvaluator() {
		textLengthAlert.setTitle("Incorrect Input Length!");
		textLengthAlert.setHeaderText("The input length is over the maximum allowed character count.");
		textLengthAlert.setContentText("Please correct the input to use a maximum of " 
		+ MAX_TEXT_LENGTH + " characters!");
	}
	
	//Retrieve an instance of the singleton.
	public static TextLengthEvaluator instance() {
		if (instance == null) {
			instance = new TextLengthEvaluator();
		}
		return instance;
	}
	
	//Call on alert box to show and wait for the user to click out of the box.
	public void showAlertDialogue() {
		this.textLengthAlert.showAndWait();
	}
}
