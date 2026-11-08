package guiAITools;


public class ModelAITools {
	public static final int DESCRIPTION_MIN = 20;
    public static final int DESCRIPTION_MAX = 2000;
    
    public static String validateInput(String category, String description) {
        // 1. Check Category
        if (category == null || category.trim().isEmpty()) {
            return "Please select a tool category.";
        }
        
        // 2. Check Description Length
        if (description == null || description.trim().length() < DESCRIPTION_MIN) {
            return "Description must be at least " + DESCRIPTION_MIN + " characters.";
        }
        if (description.trim().length() > DESCRIPTION_MAX) {
            return "Description cannot exceed " + DESCRIPTION_MAX + " characters.";
        }

        return ""; // No errors
    }
}
