/**
 * 
 */
package cse360.GRP.ADES.evaluator.textLengthEvaluation;

import javafx.scene.control.TextField;

/*******
 * <p> Title: InputField Class. </p>
 * 
 * <p> Description: The InputField class is used to 
 * correct javafx.scene.control.TextField from accepting paste operations.
 * page. </p>
 * 
 * <p> Copyright: Shane McPhillips © 2026 </p>
 * 
 * @author Shane McPhillips
 * 
 * @version 1.00		2026-09-14 Initial version
 *  
 */
public class InputField extends TextField {

	@Override
	public void paste() {
		//Disable pasting...
	}
}
