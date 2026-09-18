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
 * 
 * @version 1.00	2022-02-25 A set of semi-automated test cases
 * @version 2.00	2024-09-22 Updated for use at ASU
 * 
 */
public class PasswordEvaluationTestingAutomation {
	
	static int numPassed = 0;	// Counter of the number of passed tests
	static int numFailed = 0;	// Counter of the number of failed tests

	/*
	 * This mainline displays a header to the console, performs a sequence of
	 * test cases, and then displays a footer with a summary of the results
	 */
	public static void main(String[] args) {
		/************** Test cases semi-automation report header **************/
		System.out.println("______________________________________");
		System.out.println("\nTesting Automation");

		/************** Start of the test cases **************/
		
		// This is a properly written positive test
		performPasswordTestCase(1, "Aa!15678", true);
		
		// This is a properly written negative test
		performPasswordTestCase(2, "A!", false);
		
		// This is an improperly written negative test, because the password
		// is valid, but the second parameter asserts that it is not valid
		performPasswordTestCase(3, "Aa!15678", false);
		
		// These are improperly written positive test, because the password 
		// is not valid, but the second parameter asserts that it is valid
		performPasswordTestCase(4, "A!", true);
		performPasswordTestCase(5, "", true);
		// Add more test cases here
		
		// This is a properly written valid username test
		// It properly tests a valid username
		performUsernameTestCase(6, "TP1user&name", true);
		
		// This is a properly written negative username test
		// It tests to see if the username length is long enough
		performUsernameTestCase(7, "abc", false);
		
		// This is a properly written negative username test
		// It tests to see if there is any input
		performUsernameTestCase(8, "", false);
		
		// This is a properly written negative username test
		// It tests to see if the username length is to long
		performUsernameTestCase(9, "ThisTestIsForOverThirtyTwoCharacte", false);
		
		// This is a properly written negative username test
		// It tests to see if a special character was used
		performUsernameTestCase(10, "Whats$UpDoc", false);
		
		// This is a properly written negative username test
		// It tests to make sure it can't start with a number
		performUsernameTestCase(11, "13WillyWonka", false);
		
		// This is a properly written negative username test
		// It tests to make sure it doesn't end in a non alphanumeric character
		performUsernameTestCase(12, "UseTheForce-", false);
		
		// This is a properly written negative username test
		// It tests to make sure it starts with A-Z, a-z
		performUsernameTestCase(13, "-voldemort", false);
		
		// This is a properly written positive Email test case
		// It tests that all valid characters are allowed in LP, and DP
		performEmailTestCase(14, "a.b-c_13@v.A-baseball13.lol", true);
		
		// This is a properly written negative Email test case
		// It tests to make sure the email length is not to long
		performEmailTestCase(15, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz012"
				+ "3456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789A"
				+ "BCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJ"
				+ "KLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz012@ab.com", false);
		
		// This is a properly written negative Email test case
		// This tests to make sure the email is not empty
		performEmailTestCase(16, "", false);
		
		// This is a properly written negative Email test case
		// It tests to make sure that there are not invalid special characters
		performEmailTestCase(17, "YippyKiAy@yip&py.com", false);
		
		// This is a properly written negative Email test case
		// It tests to make sure that a letter proceeds '@'
		performEmailTestCase(18, "YippyKiAy@-yippy.com", false);
		
		// This is a properly written negative Email test case
		// It tests that DP ends in an alphanumeric character
		performEmailTestCase(19, "YippyKiAy@yippy.com-", false);
		
		// This is a properly written negative Email test case
		// It tests that LP starts with a alphanumeric character
		performEmailTestCase(20, "-YippyKiAy@yippy.com", false);
		
		// This is a properly written negative Email test case
		// It tests that LP ends with a alphanumeric character
		performEmailTestCase(21, "YippyKiAy-@yippy.com", false);
		
		// This is a properly written negative Email test case
		// It tests that it contains a '@' character
		performEmailTestCase(22, "YippyKiAy13yippy.com", false);
		
		// This is a properly written negative Email test case
		// It tests that DP doesn't contain invalid special characters
		performEmailTestCase(23, "YippyKiAy@yip&py.com", false);
		
		// This is a properly written negative Email test case
		// It tests that DP contains needs a '.' character
		
		// this is actually not needed unless we change the FSM to make a valid email to part DP
		
		performEmailTestCase(24, "YippyKiAy@yippycom", false);
		
		// This is a properly written negative Email test case
		// It tests that it doesn't allow two '@' characters
		performEmailTestCase(25, "a.b-c_13@v.A-base@ball13.lol", false);
		
		// This is a properly written negative Email test case
		// It tests that the domain part is under 70 characters long
		performEmailTestCase(26, "a.b-c_13@DomainPartIsTooLongDotComMon"
				+ "PriorToThisIsTwentyEightCharactersLong123.com", false);
		
		// This is a properly written positive update to the names inputs
		// It tests that the length and characters are correct
		performUpdateNamesTestCase(27, "Perf@rmedC$rrect-ly", true);
		
		// This is a properly written negative update to the names input
		// It tests that the length is not over the 32 max length
		performUpdateNamesTestCase(28, "PerformedIncorrectlyOverThirtyTwo", false);

		
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
		
		// If the resulting text is empty, the recognizer accepted the input
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
		
		// If the resulting text is empty, the recognizer accepted the input
		if (resultText != "") {
			 // If the test case expected the test to pass then this is a failure
			if (expectedPass) {
				System.out.println("***Failure*** The UserName <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be valid, so this is a failure!\n");
				System.out.println("Error message: " + resultText);
				numFailed++;
			}
			// If the test case expected the test to fail then this is a success
			else {			
				System.out.println("***Success*** The UserName <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be invalid, so this is a pass!\n");
				System.out.println("Error message: " + resultText);
				numPassed++;
			}
		}
		
		// If the resulting text is empty, the recognizer accepted the input
		else {	
			// If the test case expected the test to pass then this is a success
			if (expectedPass) {	
				System.out.println("***Success*** The UserName <" + inputText + 
						"> is valid, so this is a pass!");
				numPassed++;
			}
			// If the test case expected the test to fail then this is a failure
			else {
				System.out.println("***Failure*** The UserName <" + inputText + 
						"> was judged as valid" + 
						"\nBut it was supposed to be invalid, so this is a failure!");
				numFailed++;
			}
		}
	}
	
private static void performEmailTestCase(int testCase, String inputText, boolean expectedPass) {
		
		/************** Display an individual test case header **************/
		System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
		System.out.println("Input: \"" + inputText + "\"");
		System.out.println("______________");
		System.out.println("\nFinite state machine execution trace:");
		
		/************** Call the recognizer to process the input **************/
		 
		String resultText= EmailAddressRecognizer.checkEmailAddress(inputText);
		
		/************** Interpret the result and display that interpreted information **************/
		System.out.println();
		
		// If the resulting text is empty, the recognizer accepted the input
		if (resultText != "") {
			 // If the test case expected the test to pass then this is a failure
			if (expectedPass) {
				System.out.println("***Failure*** The Email <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be valid, so this is a failure!\n");
				System.out.println("Error message: " + resultText);
				numFailed++;
			}
			// If the test case expected the test to fail then this is a success
			else {			
				System.out.println("***Success*** The Email <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be invalid, so this is a pass!\n");
				System.out.println("Error message: " + resultText);
				numPassed++;
			}
		}
		
		// If the resulting text is empty, the recognizer accepted the input
		else {	
			// If the test case expected the test to pass then this is a success
			if (expectedPass) {	
				System.out.println("***Success*** The Email <" + inputText + 
						"> is valid, so this is a pass!");
				numPassed++;
			}
			// If the test case expected the test to fail then this is a failure
			else {
				System.out.println("***Failure*** The Email <" + inputText + 
						"> was judged as valid" + 
						"\nBut it was supposed to be invalid, so this is a failure!");
				numFailed++;
			}
		}
	}

private static void performUpdateNamesTestCase(int testCase, String inputText, boolean expectedPass) {
	
		/************** Display an individual test case header **************/
		System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
		System.out.println("Input: \"" + inputText + "\"");
		System.out.println("______________");
		System.out.println("\nFinite state machine execution trace:");
	
		/************** Call the recognizer to process the input **************/
	
		if(inputText.length() > 32) {
			TextLengthEvaluator.instance().showAlertDialogue();
		}
	}
}
