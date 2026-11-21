package applicationMain;

public class Logger {
	public enum Status {
		INFO("[INFO]:"),
		WARN("[WARNING]:"),
		ERROR("[ERROR]:");
		
		private String prefixString;
		
		private Status(String prefixString) {
			this.prefixString = prefixString;
		}
		
		public String getPrefix() {
			return this.prefixString;
		}
	}
	
	private static String RED = "\u001B[31m";
    private static String YELLOW = "\u001B[33m";
    private static String RESET = "\u001B[0m";
	
	private static Logger instance;
	private Logger() {}
	
	public static Logger getLogger() {
		return (instance == null) ? (instance = new Logger()) : (instance);
	}
	
	public void logInfo(String message) {
		System.out.println(Status.INFO.getPrefix() + message);
	}
	
	public void logWarning(String message) {
		System.out.println(YELLOW + Status.WARN.getPrefix() + message + RESET);
	}
	
	public void logError(String message) {
		System.out.println(RED + Status.ERROR.getPrefix() + message + RESET);
	}
}
