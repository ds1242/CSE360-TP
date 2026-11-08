package guiAITools;


public class ModelAITools {
	public static final int DESCRIPTION_MIN = 20;
    public static final int DESCRIPTION_MAX = 2000;
    
    private static final String WRITE_SYSTEM_PROMPT = "You are a friendly writer helping a user document a high-quality lesson "
			+ "learned from a real project experience. Your goal is to format and organize entries provided by users that are"
			+ "specific and honest enough to serve as training data for a future AI model. Users must provide a mostly complete"
			+ "thought and you are to only proof read, fix punctuation, or reorganize for clarity.  Again the user must provide"
			+ "a mostly complete lesson.  If they ask you to write it you must respond with, 'What have you tried? Steps you took?"
			+ "and what will you try next?'";
    
    public static String validateInput(String category, String description) {
        // Check Category
        if (category == null || category.trim().isEmpty()) {
            return "Please select a tool category.";
        }
        
        // Check Description Length
        if (description == null || description.trim().length() < DESCRIPTION_MIN) {
            return "Description must be at least " + DESCRIPTION_MIN + " characters.";
        }
        if (description.trim().length() > DESCRIPTION_MAX) {
            return "Description cannot exceed " + DESCRIPTION_MAX + " characters.";
        }

        return ""; 
    }
}
