package entityClasses;

import java.time.LocalDateTime;

/*******
 * <p> Title: Lesson Class </p>
 *
 * <p> Description: This LessonLearned class represents a lesson entity in the system.  It contains the lesson's
 *  details such as lessonID, userName, lessonText, createdAt, and updatedAt. </p>
 *
 * <p> Copyright: David Shaw © 2026 </p>
 *
 * @author David Shaw
 *
 *
 */

public class Lesson {

	private Long id;
	private String username;
	private String lessonTitle;
	private String lessonText;
	private	LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	
	/**
	 * <p> Method: Lesson() </p>
	 * 
	 * <p> Description: This default constructor is not used in this system </p>
	 */
	public Lesson() {};
	
	/**
	 * <p> Method: Lesson(String username, String lessonTitle, String lessonText)</p>
	 * 
	 * <p> Description: This constructor takes in the username and the actual lesson text to establish a lesson learned</p>
	 * @param userName specifies the account username for the lesson
	 * @param lessonTitle specifies the title of the lesson
	 * @param lessonText specifies the text for this lesson
	 */
	public Lesson(String username, String lessonTitle, String lessonText) {
		this.username = username;
		this.lessonText = lessonText;
		this.lessonTitle = lessonTitle;
	}
	
	/**
	 * <p> Method: Lesson(Long id, String username, String lessonTitle, String lessonText, LocalDateTime createdAt, LocalDateTime updatedAt)</p>
	 * 
	 * <p> Description: This constructor takes in all six params for a lesson to work with the database</p>
	 * 
	 * @param id specifies the id, comes from the db
	 * @param userName specifies the account username for the lesson
	 * @param lessonTitle specifies the title of the lesson
	 * @param lessonText specifies the text for this lesson
	 * @param createdAt specifies the date time this lesson was created
	 * @param updatedAt specifies the date time the lesson was last updated
	 */
	public Lesson(Long id, String username, String lessonTitle, String lessonText, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.username = username;
		this.lessonTitle = lessonTitle;
		this.lessonText = lessonText;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	/**
	 * <p> Method: setId(Long lessonId)</p>
	 * 
	 * <p> Description: this method updates a lesson id</p>
	 * @param text specifies the lesson id
	 */
	public void setId(Long lessonId) {
		this.id = lessonId;
	}
	
	/**
	 * <p> Method: setUserName(String username)</p>
	 * 
	 * <p> Description: this method updates a lesson username</p>
	 * @param username sets the username of the lesson
	 */
	public void setUserName(String username) {
		this.username = username;
	}
	/**
	 * <p> Method: setLessonText(String lessonText)</p>
	 * 
	 * <p> Description: this method updates a lesson text</p>
	 * @param text specifies the new account text
	 */
	public void setLessonText(String text) {
		this.lessonText = text;
	}
	
	/**
	 * <p> Method: setLessonTitle(String title)</p>
	 * 
	 * <p> Description: this method updates a lesson title and updates the modified at date time</p>
	 * @param text specifies the new account text
	 */
	public void setLessonTitle(String title) {
		this.lessonTitle = title;
	}
	
	/**
	 * <p> Method: setCreatedAt(LocalDateTime createdAt)</p>
	 * 
	 * <p> Description: this method updates the createdAt value</p>
	 * @param createdAt sets the createdAt value
	 */
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	/**
	 * <p> Method: setUpdateAt(LocalDateTime updatedAt)</p>
	 * 
	 * <p> Description: this method updates the updatedAt value</p>
	 * @param updatedAt sets the updatedAt value
	 */
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	/**
	 * <p> Method: getId()</p>
	 * 
	 * <p> Description: this method returns the lesson id</p>
	 * 
	 */
	public Long getId() {
		return this.id;
	}
	
	/**
	 * <p> Method: getLessonTitle()</p>
	 * 
	 * <p> Description: this method returns the lesson title</p>
	 * @return title
	 * 
	 */
	public String getLessonTitle() {
		return this.lessonTitle;
	}
	
	/**
	 * <p> Method: getLessonText()</p>
	 * 
	 * <p> Description: this method returns the lesson text</p>
	 * 
	 */
	public String getLessonText() {
		return this.lessonText;
	}
	
	/**
	 * <p> Method: getLessonUsername()</p>
	 * 
	 * <p> Description: this method returns the lesson username</p>
	 * 
	 */
	public String getLessonUsername() {
		return this.username;
	}
	
	/**
	 * <p> Method: getCreatedAt()</p>
	 * 
	 * <p> Description: this method returns the lesson createdAt date time</p>
	 * 
	 */
	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}
	
	/**
	 * <p> Method: getUpdatedAt()</p>
	 * 
	 * <p> Description: this method returns the lesson updatedAt date time</p>
	 * 
	 */
	public LocalDateTime getUpdatedAt() {
		return this.updatedAt;
	}
	
}
