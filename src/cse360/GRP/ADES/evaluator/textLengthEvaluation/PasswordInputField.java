/**
 * 
 */
package cse360.GRP.ADES.evaluator.textLengthEvaluation;

import javafx.scene.control.PasswordField;

/*******
 * <p> Title: PasswordInputField Class. </p>
 * 
 * <p> Description: The PasswordInputField class is used to 
 * correct javafx.scene.control.PasswordField from accepting paste operations.
 * page. </p>
 * 
 * <p> Copyright: Shane McPhillips © 2026 </p>
 * 
 * @author Shane McPhillips
 * 
 * @version 1.00		2026-09-14 Initial version
 *  
 */
public class PasswordInputField extends PasswordField {
	
	@Override
	public void paste() {
		//Disable pasting...
	}

}
