package lessons;

import java.util.HashMap;
import java.util.Map;

public class LessonsLearned {

	private String title;
	private String description;
	private String category;
	private float durationHours;
	private Map<String, String> experiences;
	
	public LessonsLearned(String title, String description, String category, float effort) {
		this.title = title;
		this.description = description;
		this.category = category;
		this.durationHours = effort;
		this.experiences = new HashMap<String, String>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public float getDurationHours() {
		return durationHours;
	}

	public void setDurationHours(float durationHours) {
		this.durationHours = durationHours;
	}

	public Map<String, String> getExperiences() {
		return experiences;
	}

	public void setExperiences(Map<String, String> experiences) {
		this.experiences = experiences;
	}
}
