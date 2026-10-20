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
	}

	//Retrieve an instance of the singleton.
	public static TextLengthEvaluator instance() {
		if (instance == null) {
			instance = new TextLengthEvaluator();
		}
		return instance;
	}

    //Evalulate an input string to see if it is within the default maximum length requirement.
    public static String evaluateText(String newText) {
        return evaluateText(newText, MAX_TEXT_LENGTH);
    }

    //Evalulate an input string against a caller supplied maximum. A lesson learned needs a far
    //longer body than a username, so the maximum cannot be the same single constant for every
    //field. The existing one argument version keeps using MAX_TEXT_LENGTH, so every field that
    //was limited to 32 characters before is limited to 32 characters still.
    public static String evaluateText(String newText, int maxLength) {
        return evaluateText(newText, 0, maxLength);
    }

    //Evalulate an input string against a caller supplied minimum and maximum. A title may not
    //be left empty, so the field that holds it is checked with a minimum of one. The text is
    //trimmed before it is measured, so a field holding nothing but spaces counts as the empty
    //field the user actually left behind.
    public static String evaluateText(String newText, int minLength, int maxLength) {
        if (newText == null || newText.trim().length() < minLength) {
            if (minLength == 1) {
                return "Please enter a value! This field may not be left empty.";
            }
            return "Please enter at least " + minLength + " characters!";
        }
        if (newText.length() > maxLength) {
            return "Please correct the input to use a maximum of " + maxLength + " characters!";
        }
        return "";
    }

	//Call on alert box to show and wait for the user to click out of the box.
	public void showAlertDialogue(String errMessage) {
        this.textLengthAlert.setContentText(errMessage);
		this.textLengthAlert.showAndWait();
	}
}
