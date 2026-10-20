package entityClasses;

/*******
 * <p> Title: LessonsLearned Class </p>
 *
 * <p> Description: This LessonsLearned class represents a single lesson learned entity in the
 *  system.</p>
 *
 * @author Edis Jakupovic
 *
 * @version 1.00		2026-10-07 Initial implementation for HW2
 *
 */

public class LessonsLearned {

	/*
	 * These are the private attributes for this entity object
	 */
	private String contributorUserName;
	private String coreInformation;
	private String experience;
	private String title;

	/*****
	 * <p> Method: LessonsLearned() </p>
	 *
	 * <p> Description: This default constructor produces an empty lesson, which is the starting
	 *  state of a lesson being composed before any of its fields have been entered. </p>
	 */
	public LessonsLearned() {

	}

	/*****
	 * <p> Method: LessonsLearned(String contributorUserName, String coreInformation,
	 * 		String experience, String title) </p>
	 *
	 * <p> Description: This constructor is used to establish lesson learned entity objects.  The
	 *  database layer uses it when reading a lesson back, so no validation is performed here;
	 *  the input validation and the helpful error messages belong to the GUI model that gathers
	 *  the fields, which keeps stored lessons constructible without re-checking data the system
	 *  itself wrote. </p>
	 *
	 * @param contributorUserName specifies the userName of the contributor who added this lesson
	 *
	 * @param coreInformation specifies the body of the lesson: what happened, what was learned,
	 * 		and what a future team should do differently
	 *
	 * @param experience specifies the time and effort information gathered from attempts to
	 * 		leverage this lesson, which is empty until the first attempt has been reported
	 *
	 * @param title specifies the short name used when this lesson appears in a list
	 *
	 */
	public LessonsLearned(String contributorUserName, String coreInformation, String experience,
			String title) {
		this.contributorUserName = contributorUserName;
		this.coreInformation = coreInformation;
		this.experience = experience;
		this.title = title;
	}

	/*****
	 * <p> Method: String getContributorUserName() </p>
	 *
	 * <p> Description: This getter returns the ContributorUserName. </p>
	 *
	 * @return a String of the ContributorUserName
	 */
	public String getContributorUserName() { return contributorUserName; }

	/*****
	 * <p> Method: String getCoreInformation() </p>
	 *
	 * <p> Description: This getter returns the CoreInformation. </p>
	 *
	 * @return a String of the CoreInformation
	 */
	public String getCoreInformation() { return coreInformation; }

	/*****
	 * <p> Method: String getExperience() </p>
	 *
	 * <p> Description: This getter returns the Experience. </p>
	 *
	 * @return a String of the Experience, which is empty for a lesson that has not yet been
	 * 		leveraged
	 */
	public String getExperience() { return experience; }

	/*****
	 * <p> Method: String getTitle() </p>
	 *
	 * <p> Description: This getter returns the Title. </p>
	 *
	 * @return a String of the Title
	 */
	public String getTitle() { return title; }

	public void setContributorUserName(String s) { contributorUserName = s; }
	public void setCoreInformation(String s) { coreInformation = s; }
	public void setExperience(String s) { experience = s; }
	public void setTitle(String s) { title = s; }
}

