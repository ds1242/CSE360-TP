package cse360.GRP.ADES.evaluator.textLengthEvaluation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextInputControl;

/*******
 * <p> Title: TextLengthChangeListener Class. </p>
 *
 * <p> Description: The TextLengthChangeListener class is used as an event handler listener for incoming changes to text-box fields and
 * checks for compliance with correct text length.
 * page. </p>
 *
 * <p> Copyright: Shane McPhillips © 2026 </p>
 *
 * @author Shane McPhillips
 *
 * @version 1.00		2026-09-14 Initial version
 *
 */

public class TextLengthChangeListener implements ChangeListener<String> {

	//TextInputControl, rather than TextField, so the same listener can watch the multi-line
	//TextArea a lesson learned is typed into as well as the single-line fields.
	private TextInputControl textField; //Text control that the listener is watching for.

	private boolean shouldClearText = false; //Option to clear text upon character count error.
	private boolean isTextUpdating = false; //Check for if the text field is being adjusted (asynchronous task prevention)
	private int maxLength = TextLengthEvaluator.MAX_TEXT_LENGTH; //Maximum this control allows.

	public TextLengthChangeListener(TextInputControl textField) {
		this(textField, false);
	}

	public TextLengthChangeListener(TextInputControl textField, boolean shouldClearText) {
		this.textField = textField;
		this.shouldClearText = shouldClearText;
	}

	//Use this constructor for a control whose maximum is not the default 32 characters.
	public TextLengthChangeListener(TextInputControl textField, int maxLength) {
		this(textField, false, maxLength);
	}

	public TextLengthChangeListener(TextInputControl textField, boolean shouldClearText,
			int maxLength) {
		this.textField = textField;
		this.shouldClearText = shouldClearText;
		this.maxLength = maxLength;
	}

	//Needed method for implement class. Controls text-field text length and shows dialogue
	//if text is over allowed length.
	@Override
	public void changed(ObservableValue<? extends String> observed, String oldText, String newText) {
		if (isTextUpdating) {
			//Already being processed.
			return;
		}
		//Compare length to max allowed length.
        String errMessage = TextLengthEvaluator.evaluateText(newText, maxLength);

		if (errMessage != "") {
			//Show dialogue box for warning on input length.
			TextLengthEvaluator.instance().showAlertDialogue(errMessage);

			//Must use run-later to disable UI updates from effecting current method.
			javafx.application.Platform.runLater(() -> {
				this.isTextUpdating = true; //Set currently updating text
				try {
					//Clear the text field if option is present. Replace the text field with old text to correct length.
					if (this.shouldClearText) {
						textField.clear();
					} else {
						textField.setText(newText.substring(0, maxLength));
					}
				} finally {
					this.isTextUpdating = false; //Set no longer updating text
				}
			});
		}
	}
}
