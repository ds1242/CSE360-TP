package entityClasses;

/*******
 * <p> Title: TeammateEffort Class </p>
 * <p> Description: One row of the "description of effort" in an experience entry (Story 6): the
 * name of a teammate and what that teammate contributed. An experience entry may hold several
 * of these, one for each member of the team who worked on the attempt.</p>
 *
 * @author Shane McPhillips
 *
 * @version 1.00	2026-10-07 Initial version for HW2
 */
public class TeammateEffort {

	/*-*******************************************************************************************

	Attributes

	 */

	private String teammateName;		// 2-50 characters (see LessonsEvaluator)
	private String effortEntry;			// 20-2,000 characters (see LessonsEvaluator)


	/*-*******************************************************************************************

	Constructors

	 */

	/**********
	 * <p> Method: TeammateEffort(String, String) </p>
	 * <p> Description: Constructor of the class. </p>
	 * @param teammateName	the name of the teammate
	 * @param effortEntry	a description of the effort this teammate put in.
	 */
	public TeammateEffort(String teammateName, String effortEntry) {
		this.teammateName = teammateName;
		this.effortEntry = effortEntry;
	}


	/*-*******************************************************************************************

	Getters

	 */

	/** 
	 * <p> Method: getTeammateName() </p>
	 * <p> Description: Returns the name of the current teammate. Check for null on return variable.</p>
	 * @return the name of the teammate */
	public String getTeammateName() { return teammateName; }

	/** 
	 * <p> Method: getEffortEntry() </p>
	 * <p> Description: Returns the effort description supplied by the teammate. Check for null.</p>
	 * @return the description of the effort this teammate put in */
	public String getEffortEntry() { return effortEntry; }


	/**********
	 * <p> Method: toString() </p>
	 * <p> Description: The text shown for this row is in the teammate list on the Experience page. </p>
	 * @return the teammate's name followed by the effort entry
	 */
	@Override
	public String toString() {
		return teammateName + ": " + effortEntry;
	}
}
