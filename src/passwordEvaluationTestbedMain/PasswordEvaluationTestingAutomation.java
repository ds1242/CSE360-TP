package passwordEvaluationTestbedMain;
import cse360.GRP.ADES.evaluator.*;
import cse360.GRP.ADES.evaluator.textLengthEvaluation.*;


/*******
 * <p> Title: PasswordEvaluationTestingAutomation Class. </p>
 * 
 * <p> Description: A Java demonstration for semi-automated tests </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2022 </p>
 * 
 * @author Lynn Robert Carter
 * @author editions by Andrew Kannas
 * 
 * @version 1.00	2022-02-25 A set of semi-automated test cases
 * @version 2.00	2024-09-22 Updated for use at ASU
 * 
 */
public class PasswordEvaluationTestingAutomation {
	
	static int numPassed = 0;	// Counter of the number of passed tests
	static int numFailed = 0;	// Counter of the number of failed tests
	static int count = 0;

	/*
	 * This mainline displays a header to the console, performs a sequence of
	 * test cases, and then displays a footer with a summary of the results
	 */
	public static void main(String[] args) {
		/************** Test cases semi-automation report header **************/
		System.out.println("______________________________________");
		System.out.println("\nTesting Automation");

		/************** Start of the password test cases **************/
		
		// The performPassowrdtestCase methods use the evaluatePassword
		// method in the PasswordEvaluator class to evaluate the test cases
		
		// This is a properly written positive test
		// It tests a valid password is judged as correctly
		performPasswordTestCase(count++, "Aa!15678", true);
		
		// This is a properly written negative test
		// It tests an invalid password is judged correctly
		performPasswordTestCase(count++, "A!", false);
		
		// This is an improperly written negative test, because the password
		// is valid, but the second parameter asserts that it is not valid
		performPasswordTestCase(count++, "Aa!15678", false);
		
		// This is an improperly written positive test, because the password 
		// is not valid, but the second parameter asserts that it is valid
		performPasswordTestCase(count++, "A!", true);
		
		// This is an improperly written positive test, because the password 
		// is not valid, but the second parameter asserts that it is valid
		performPasswordTestCase(count++, "", true);
		
		// This is a properly written negative password test
		// Tests to make sure it fails without a Upper Case letter
		performPasswordTestCase(count++,  "aa!15678", false);
		
		// This is a properly written negative password test
		// Tests to make sure there it fails without a special character
		performPasswordTestCase(count++, "Aa156789", false);
		
		// This is a properly written negative password test
		// Tests to make sure it fails without a number character
		performPasswordTestCase(count++, "Aa!@#$%^&*", false);
		
		// This is a properly written negative password test
		// Tests to make sure it fails without a lower case character
		performPasswordTestCase(count++, "AA1$56789", false);
		
		// This is a properly written negative test case
		// Tests to make sure that the password is not over 32 characters
		performPasswordTestCase(count++, "Aa1()_-+={}[]|\\:;\"'<>,.?/;1236547", false);
		
		// This is a properly written negative password test
		// It tests to make sure minimum length requirement is met
		performPasswordTestCase(count++, "Aa1!123", false);
		
		// This is a properly written positive password test
		// Tests to make sure all characters are allowed 
		performPasswordTestCase(count++, "Aa156789~`!@#$%^&*()_", true);  
		
		// This is a properly written positive password test
		// Tests to make sure all characters are allowed 
		performPasswordTestCase(count++, "Aa1-+={}[]|\\:;\"'<>,.?/;", true);
		
		
		/************** Start of the username test cases **************/
		
		// The performUserNameTestCase methods use the evaluateUsername
		// method in the UsernameEvaluator class to evaluate the test cases
		
		
		// This is a properly written valid username test
		// It tests a valid username
		performUsernameTestCase(count++, "TP1user&name", true);
		
		// This is a properly written negative username test
		// It tests to see if the username length is long enough
		performUsernameTestCase(count++, "abc", false);
		
	
		// This is a properly written negative username test
		// It tests to see if the username length is to long
		performUsernameTestCase(count++, "ThisTestIsForOverThirtyTwoCharacte", false);
		
		// This is a properly written negative username test
		// It tests to see if a special character was used
		performUsernameTestCase(count++, "Whats$UpDoc", false);
		
		// This is a properly written negative username test
		// It tests to make sure it can't start with a number
		performUsernameTestCase(count++, "13WillyWonka", false);
		
		// This is a properly written negative username test
		// It tests to make sure it doesn't end in a non alphanumeric character
		
		// This is also showing an invalid error message for the '-'
		
		performUsernameTestCase(count++, "UseTheForce-", false);
		
		// This is a properly written negative username test
		// It tests to make sure it starts with A-Z, a-z
		performUsernameTestCase(count++, "-voldemort", false);
		
		/************** Start of the email test cases **************/
		
		// The performEmailTestCase methods use the checkEmailAddress method
		// in the EmailAdressEvaluator class to evaluate the test cases
		
		// This is a properly written positive Email test case
		// It tests that all valid characters are allowed in LP, and DP
		performEmailTestCase(count++, "a.b-c_13@v.A-baseball13.lol", true);
		
		// This is a properly written negative Email test case
		// It tests to make sure the email length is not to long
		performEmailTestCase(count++,"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
				+ "lmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNO"
				+ "PQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789ABCD"
				+ "EFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01KL"
				+ "MNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz012@abc.com", false);
		
		// This is a properly written negative Email test case
		// This tests to make sure the email is not empty
		performEmailTestCase(count++, "", false);
		
		// This is a properly written negative Email test case
		// It tests to make sure that there are not invalid special characters
		performEmailTestCase(count++, "YippyKiAy@yip&py.com", false);
		
		// This is a properly written negative Email test case
		// It tests to make sure that a letter proceeds '@'
		performEmailTestCase(count++, "YippyKiAy@-yippy.com", false);
		
		// This is a properly written negative Email test case
		// It tests that DP ends in an alphanumeric character
		performEmailTestCase(count++, "YippyKiAy@yippy.com-", false);
		
		// This is a properly written negative Email test case
		// It tests that LP starts with a alphanumeric character
		performEmailTestCase(count++, "-YippyKiAy@yippy.com", false);
		
		// This is a properly written negative Email test case
		// It tests that LP ends with a alphanumeric character
		performEmailTestCase(count++, "YippyKiAy-@yippy.com", false);
		
		// This is a properly written negative Email test case
		// It tests that it contains a '@' character
		performEmailTestCase(count++, "YippyKiAy13yippy.com", false);
		
		// This is a properly written negative Email test case
		// It tests that DP doesn't contain invalid special characters
		performEmailTestCase(count++, "YippyKiAy@yip&py.com", false);
		
		// This is a properly written negative Email test case
		// It tests that DP contains needs a '.' character
		
		// this is actually not needed unless we change the FSM to make a valid email to part DP
		
		performEmailTestCase(count++, "YippyKiAy@yippycom", false);
		
		// This is a properly written negative Email test case
		// It tests that it doesn't allow two '@' characters
		performEmailTestCase(count++, "a.b-c_13@v.A-base@ball13.lol", false);
		
		// This is a properly written negative Email test case
		// It tests that the DP is under 70 characters long
		performEmailTestCase(count++, "a.b-c_13@DomainPartIsTooLongDotComMon"
				+ "PriorToThisIsTwentyEightCharactersLong123.com", false);
		
		/************** Start of the update names test cases **************/
		
		// The performUpdateNamesTestCase methods use the evaluateText method
		// in the TextLengthEvaluator class to evaluate the test cases
		
		
		// This is a properly written positive update to the names inputs
		// It tests that the length and characters are correct
		performUpdateNamesTestCase(count++, "Perf@rmedC$rrect-ly", true);
		
		// This is a properly written negative update to the names input
		// It tests that the length is not over the 32 max length
		performUpdateNamesTestCase(count++, "PerformedIncorrectlyOverThirtyTwo", false);

		
		/************** End of the test cases **************/
		
		/************** Test cases semi-automation report footer **************/
		System.out.println("____________________________________________________________________________");
		System.out.println();
		System.out.println("Number of tests passed: "+ numPassed);
		System.out.println("Number of tests failed: "+ numFailed);
	}
	
	/*
	 * This method sets up the input value for the test from the input parameters,
	 * displays test execution information, invokes precisely the same recognizer
	 * that the interactive JavaFX mainline uses, interprets the returned value,
	 * and displays the interpreted result.
	 */
	private static void performPasswordTestCase(int testCase, String inputText, boolean expectedPass) {
				
		/************** Display an individual test case header **************/
		System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
		System.out.println("Input: \"" + inputText + "\"");
		System.out.println("______________");
		System.out.println("\nFinite state machine execution trace:");
		
		/************** Call the recognizer to process the input **************/
		String resultText= PasswordEvaluator.evaluatePassword(inputText);
		
		/************** Interpret the result and display that interpreted information **************/
		System.out.println();
		errorMessage(resultText, expectedPass, inputText);
	}
	
	/*
	 * This method sets up the input value for the test from the input parameters,
	 * displays test execution information, invokes precisely the same recognizer
	 * that the interactive JavaFX mainline uses, interprets the returned value,
	 * and displays the interpreted result.
	 */
	private static void performUsernameTestCase(int testCase, String inputText, boolean expectedPass) {
		
		/************** Display an individual test case header **************/
		System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
		System.out.println("Input: \"" + inputText + "\"");
		System.out.println("______________");
		System.out.println("\nFinite state machine execution trace:");
		
		/************** Call the recognizer to process the input **************/
		 
		String resultText= UsernameEvaluator.checkForValidUserName(inputText);
		
		/************** Interpret the result and display that interpreted information **************/
		System.out.println();
		errorMessage(resultText, expectedPass, inputText);
	}
	
	/*
	 * This method sets up the input value for the test from the input parameters,
	 * displays test execution information, invokes precisely the same recognizer
	 * that the interactive JavaFX mainline uses, interprets the returned value,
	 * and displays the interpreted result.
	 */
	private static void performEmailTestCase(int testCase, String inputText, boolean expectedPass) {
		
		/************** Display an individual test case header **************/
		System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
		System.out.println("Input: \"" + inputText + "\"");
		System.out.println("______________");
		System.out.println("\nFinite state machine execution trace:");
		
		/************** Call the recognizer to process the input **************/
		 
		String resultText= EmailAddressEvaluator.checkEmailAddress(inputText);
		
		/************** Interpret the result and display that interpreted information **************/
		System.out.println();
		
		errorMessage(resultText, expectedPass, inputText);
	}
	
	/*
	 * This method sets up the input value for the test from the input parameters,
	 * displays test execution information, invokes precisely the same recognizer
	 * that the interactive JavaFX mainline uses, interprets the returned value,
	 * and displays the interpreted result.
	 */
	private static void performUpdateNamesTestCase(int testCase, String inputText, boolean expectedPass) {
	
		/************** Display an individual test case header **************/
		System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
		System.out.println("Input: \"" + inputText + "\"");
		System.out.println("______________");
		System.out.println("\nFinite state machine execution trace:");
	
		/************** Call the recognizer to process the input **************/
	
		String resultText = TextLengthEvaluator.evaluateText(inputText);
		errorMessage(resultText, expectedPass, inputText);
	}

	/*
	 * This method sets up the error message for the test cases, and
	 * displays test execution information, the method will take in
	 * the result text from the validator methods, the expected pass
	 * from the test case, and the test case string.  It then proceeds
	 * to match the three arguments to make output the correct message.
	 */
	private static void errorMessage(String resultText, boolean expectedPass, String inputText) {
		//If the resulting text is empty, the recognizer accepted the input
		if (resultText != "") {
			 // If the test case expected the test to pass then this is a failure
			if (expectedPass) {
				System.out.println("***Failure*** The password <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be valid, so this is a failure!\n");
				System.out.println("Error message: " + resultText);
				numFailed++;
			}
			// If the test case expected the test to fail then this is a success
			else {			
				System.out.println("***Success*** The password <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be invalid, so this is a pass!\n");
				System.out.println("Error message: " + resultText);
				numPassed++;
			}
		}
		
		// If the resulting text is empty, the recognizer accepted the input
		else {	
			// If the test case expected the test to pass then this is a success
			if (expectedPass) {	
				System.out.println("***Success*** The password <" + inputText + 
						"> is valid, so this is a pass!");
				numPassed++;
			}
			// If the test case expected the test to fail then this is a failure
			else {
				System.out.println("***Failure*** The password <" + inputText + 
						"> was judged as valid" + 
						"\nBut it was supposed to be invalid, so this is a failure!");
				numFailed++;
			}
		}
	}


}
