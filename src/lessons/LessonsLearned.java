package lessons;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> Title: LessonsLearned Class</p>
 * <p> Description: A data wrapper class used to hold detailed information about each saved and loaded lesson.</p>
 */
public class LessonsLearned {

	private String title;
	private String description;
	private String category;
	private float durationHours;
	private Map<String, String> experiences;
	
	/**
	 * <p> Method: LessonsLearned(String, String, String, float)</p>
	 * <p> Description: The constructor for a Lesson learned. A wrapper for the data in the class.</p>
	 * @param title
	 * @param description
	 * @param category
	 * @param effort
	 */
	public LessonsLearned(String title, String description, String category, float effort) {
		this.title = title;
		this.description = description;
		this.category = category;
		this.durationHours = effort;
		this.experiences = new HashMap<String, String>();
	}

	/**
	 * <p> Method: getTitle() </p>
	 * @return the title of the Lesson
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * <p> Method: setTitle(String) </p>
	 * <p> Description: Takes in a String that sets the lessons title to the given input.
	 * @param title - the string input to replace the lessons current title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * <p> Method: getDescription()</p>
	 * @return the descriptive text of the lesson
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * <p> Method: setDescription(String)</p>
	 * <p> Description: This function sets the description value of the lesson. 
	 * When displayed, this text will read as the description of the lesson.</p>
	 * @param description - the descriptive text that will replace the current lessons text.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * <p> Method: getCategory()</p>
	 * @return the category string type of what the lesson's idea is centered around.
	 */
	public String getCategory() {
		return category;
	}
	
	/**
	 * <p> Method: setCategory(String)</p>
	 * <p> Description: This function takes in a String value for the value of a category. This value directly 
	 * corresponds to the enum values of CATEGORY as a string literal.</p>
	 * @param category - the category enum value literal to replace the current value of the category type saved.
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
	 * <p> Method: getDurationHours()</p>
	 * @return the number of hours that a lesson is said to take.
	 */
	public float getDurationHours() {
		return durationHours;
	}
	
	/**
	 * <p> Method: setDurationHours(float) </p>
	 * <p> Description: This function takes in a float input to replace / save the time (in hours) 
	 * that the lesson has taken to learn. </p>
	 * @param durationHours - The time value input, in hours, for the effort spent on a lesson.
	 */
	public void setDurationHours(float durationHours) {
		this.durationHours = durationHours;
	}
	
	/**
	 * <p> Method: getExperiences() </p>
	 * @return the map dataset holding lesson experiences per every person who has shared an experience about this lesson.
	 */
	public Map<String, String> getExperiences() {
		return experiences;
	}
	
	/**
	 * <p> Method: setEexperiences(Map<String, String>) </p>
	 * <p> Description: (NOT IN USE) This function takes in a Map dataset and is used to save the current 
	 * lesson members experiences.</p>
	 * @param experiences - A dataset pair that holds a persons name and their descriptive experience regarding the lesson.
	 */
	public void setExperiences(Map<String, String> experiences) {
		this.experiences = experiences;
	}
}
