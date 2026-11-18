package guiAITools;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import applicationMain.FoundationsMain;
import entityClasses.LessonRecord;
import entityClasses.LessonsLearnedList;


/**
 * Title: ModelAITools
 * Class with several helper methods for validating user input and sending it to the Groq chat API.
 * <p>
 * Currently supports the "Write" category, which proofreads and organizes a
 * user written lesson learned. </p>
 */
public class ModelAITools {
	/** Minimum description length. */
	public static final int DESCRIPTION_MIN = 20;

	/** Maximum description length. */
	public static final int DESCRIPTION_MAX = 2000;

	private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";
	private static final String GROQ_MODEL = "openai/gpt-oss-120b";
	

	private static final String WRITE_SYSTEM_PROMPT = "You are a friendly writer helping a user document a high-quality lesson "
			+ "learned from a real project experience. Your goal is to format and organize entries provided by users that are "
			+ "specific and honest enough to serve as training data for a future AI model. Users must provide a mostly complete "
			+ "thought and you are to only proofread, fix punctuation, or reorganize for clarity. Again the user must provide "
			+ "a mostly complete lesson. If they ask you to write it you must respond with: 'What have you tried? Steps you took? "
			+ "and what will you try next?' Return only the newly written lesson. Do not add any advice, preamble, or extra "
			+ "punctuation with the exception of the required response if they ask you to write a lesson outright.";
	
	
	private static final String COACH_PROMPT = "You are a helpful writing coach reviewing a 'lesson learned' post. "
			+ "Provide 3 specific actionable items of feedback. Focus on clarity and what would make it valuable to"
			+ "readers. Start each bullet point with a +.  Do not add any other advice, preamble, or extra punctuation. ";
	
	private static final String SEARCH_PROMPT = "A user is attempting to search for a 'lesson learned' post using plain "
			+ "language.  A list of lesson titles and their";

	/**
	 * Method: validateInput(String category, String description)
	 * <p>Description: Validates the category and description entered by the user.
	 *
	 * Returns an empty string when the input is valid.
	 * </p>
	 *
	 * @param category    the selected tool category
	 * @param description the user's description text
	 * @return an error message, or an empty string if valid
	 */
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

	/**
	 * Method: askAI(String category, String description)
	 * <p>Description: 
	 * Sends the description to the AI using the prompt for the given category.
	 * 
	 * Only the "Write" category is supported.</p>
	 *
	 * @param category    the tool category, must be "Write"
	 * @param description the user's description text
	 * @return the AI's response text
	 * @throws Exception if the category is unsupported or the request fails
	 */
	public static String askAI(String category, String description) throws Exception {
		if (!category.equals("Write")) {
			throw new RuntimeException("Unsupported or null category: " + category);
		}

		List<ChatMessage> history = new ArrayList<>();
		history.add(new ChatMessage("user", description));

		String body = buildRequestBody(history, WRITE_SYSTEM_PROMPT);
		String response = postToGroq(body);
		return extractAssistantMessage(response);
	}
	
	/**
	 * Method: brainstormLesson(String category, String body)
	 * <p>Description:
	 * Sends the user's draft lesson to the AI using the writing coach prompt,
	 * which returns three actionable feedback items.
	 *
	 * Only the "Brainstorm" category is supported.</p>
	 *
	 * @param category the tool category, must be "Brainstorm"
	 * @param body     the user's draft lesson text
	 * @return the AI's feedback text
	 * @throws Exception if the category is unsupported or the request fails
	 */
	public static String brainstormLesson(String category, String body) throws Exception {
		if(!category.equals("Brainstorm")) {
			throw new RuntimeException("Unsupported or null category: " + category);
		}
		List<ChatMessage> msgs = new ArrayList<>();
		msgs.add(new ChatMessage("user", body));
		String requestBody = buildRequestBody(msgs, COACH_PROMPT);
		String response = postToGroq(requestBody);
		return extractAssistantMessage(response);
	}
	
	/**
	 * Method: searchLessons(String category, String body)
	 * <p>Description:
	 * Loads the saved lessons learned, appends them to the user's plain language
	 * search request, and sends the combined text to the AI so it can identify
	 * the best matches.
	 *
	 * Only the "Search" category is supported.</p>
	 *
	 * @param category the tool category, must be "Search"
	 * @param body     the user's plain language search text
	 * @return the AI's response text
	 * @throws Exception if the category is unsupported or the request fails
	 */
	public static String searchLessons(String category, String body) throws Exception {
		if(!category.equals("Search")) {
			throw new RuntimeException("Unsupported or null category: " + category);
		}
		List<LessonRecord> lessons = new ArrayList<>();
		try {
			LessonsLearnedList lessonList = new LessonsLearnedList();
			lessons = lessonList.getLessons();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		StringBuilder stringBuilder = new StringBuilder();
	    stringBuilder.append("User search: ").append(body).append("\n\n");
	    stringBuilder.append("Available lessons:\n");
	    for (LessonRecord lesson : lessons) {
	    	stringBuilder.append("- ").append(lesson.getTitle())
//	          .append(": ").append(lesson.getDescription())
	          .append("\n");
	    }
		
		List<ChatMessage> history = new ArrayList<>();
		history.add(new ChatMessage("user", body));

		String requestBody = buildRequestBody(history, SEARCH_PROMPT);
		String response = postToGroq(requestBody);
		return extractAssistantMessage(response);
	}


	/**
	 * Method: postToGroq(String body)
	 * <p>
	 * Description: Posts a JSON request body to the Groq API.
	 * </p>
	 * @param body the JSON request body
	 * @return the raw JSON response
	 * @throws Exception if the request fails or the status code is not 200
	 */
	public static String postToGroq(String body) throws Exception {
		HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();
		HttpRequest req = HttpRequest.newBuilder().uri(URI.create(GROQ_URL)).timeout(Duration.ofSeconds(60))
				.header("Content-Type", "application/json").header("Authorization", "Bearer " + FoundationsMain.llmApiKey)
				.POST(HttpRequest.BodyPublishers.ofString(body)).build();
		HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
		if (resp.statusCode() != 200) {
			throw new RuntimeException("Groq API error " + resp.statusCode() + ": " + truncate(resp.body(), 300));
		}
		return resp.body();
	}

	/**
	 * Method: truncate(String s, int n)
	 * <p>
	 * Description: Shortens a string to a maximum length.
	 * </p>
	 * @param s the string to shorten
	 * @param n the maximum length before "..." is added
	 * @return the shortened string, or an empty string if null
	 */
	private static String truncate(String s, int n) {
		if (s == null)
			return "";
		return s.length() <= n ? s : s.substring(0, n) + "...";
	}

	/**
	 * Method: buildRequestBody(List<ChatMessage> history, String systemPrompt)
	 * 
	 * <p>Description:
	 * Builds the JSON request body with the system prompt and message history.
	 * </p>
	 *
	 * @param history      the list of chat messages
	 * @param systemPrompt the system prompt text
	 * @return the JSON request body
	 */
	private static String buildRequestBody(List<ChatMessage> history, String systemPrompt) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"model\":\"").append(GROQ_MODEL).append("\",");
		sb.append("\"temperature\":0.7,");
		sb.append("\"max_tokens\":1024,");
		sb.append("\"messages\":[");
		
		// System prompt first
		sb.append("{\"role\":\"system\",\"content\":\"").append(escapeJson(systemPrompt)).append("\"}");
		
		// Then conversation history
		for (ChatMessage m : history) {
			String role = m.role.equals("model") ? "assistant" : m.role;
			sb.append(",{\"role\":\"").append(role).append("\",\"content\":\"").append(escapeJson(m.text)).append("\"}");
		}
		
		sb.append("]");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Method: extractAssistantMessage(String json)
	 * <p>
	 * Description: Pulls the assistant's message text out of the JSON response.
	 * 
	 * Finds the first "content" value after "message" and decodes its escapes.
	 * </p>
	 * @param json the raw JSON response
	 * @return the message text, or an empty string if not found
	 */
	public static String extractAssistantMessage(String json) {
		int msgIdx = json.indexOf("\"message\"");
		int searchFrom = msgIdx >= 0 ? msgIdx : 0;
		int contentIdx = json.indexOf("\"content\"", searchFrom);
		if (contentIdx < 0)
			return "";
		int colon = json.indexOf(':', contentIdx);
		if (colon < 0)
			return "";
		int start = json.indexOf('"', colon + 1);
		if (start < 0)
			return "";
		StringBuilder out = new StringBuilder();
		boolean escape = false;
		for (int i = start + 1; i < json.length(); i++) {
			char c = json.charAt(i);
			if (escape) {
				switch (c) {
				case 'n':
					out.append('\n');
					break;
				case 't':
					out.append('\t');
					break;
				case 'r':
					out.append('\r');
					break;
				case '"':
					out.append('"');
					break;
				case '\\':
					out.append('\\');
					break;
				case '/':
					out.append('/');
					break;
				case 'u':
					if (i + 4 < json.length()) {
						try {
							int code = Integer.parseInt(json.substring(i + 1, i + 5), 16);
							out.append((char) code);
							i += 4;
						} catch (NumberFormatException e) {
							out.append(c);
						}
					}
					break;
				default:
					out.append(c);
				}
				escape = false;
			} else if (c == '\\') {
				escape = true;
			} else if (c == '"') {
				return out.toString();
			} else {
				out.append(c);
			}
		}
		return out.toString();
	}

	/**
	 * Method: escapeJson(String s)
	 * <p>Description: Escapes a string so it can be placed inside a JSON string value.</p>
	 *
	 * @param s the text to escape
	 * @return the escaped text, or an empty string if null
	 */
	private static String escapeJson(String s) {
		if (s == null)
			return "";
		StringBuilder sb = new StringBuilder(s.length() + 16);
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			default:
				if (c < 0x20)
					sb.append(String.format("\\u%04x", (int) c));
				else
					sb.append(c);
			}
		}
		return sb.toString();
	}
	
	public static void getKey() {
		System.out.println("Key = " + FoundationsMain.llmApiKey);
	}

	/**
	 * Title: ChatMessage
	 * <p>Description: A single chat message with a role and text.</p>
	 */
	public static class ChatMessage {
		/** The sender role, such as "user", "system" or "model". */
		public String role; 

		/** The message text. */
		public String text;

		/**
		 * Creates a chat message.
		 *
		 * @param role the sender role
		 * @param text the message text
		 */
		public ChatMessage(String role, String text) {
			this.role = role;
			this.text = text;
		}
	}
}