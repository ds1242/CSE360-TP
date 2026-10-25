package lessons;

import java.time.LocalDateTime;

/*******
 * <p> Title: LessonRecord Class </p>
 *
 * <p> Description: A LessonsLearned object as it exists once it has been stored in the
 * database. It adds the bookkeeping fields that the database and the Contributor pages need
 * but that the core lesson does not: the database id, the username of the contributor who
 * created it, the date it was created, which of its fields a curator has locked, and who last
 * changed it.</p>
 *
 * <p> Why a subclass instead of adding fields to LessonsLearned? LessonsLearned describes the
 * "core information" of a lesson (Story 1). Ownership, creation date, and locking describe how
 * the system manages a stored lesson (Stories 2, 3, and 5). Keeping those concerns in a subclass
 * leaves LessonsLearned unchanged and lets every existing use of it keep working, while any code
 * that needs the stored-lesson details can ask for a LessonRecord instead.</p>
 *
 * <p> Why lock each field instead of the whole lesson? Story 3 asks that a contributor can see
 * "the fields I can alter, and which are locked". A curator may want to freeze an approved title
 * while still letting the contributor correct the effort, so each core field has its own lock.
 * A lesson with any locked field cannot be deleted (Story 3 and Story 5).</p>
 *
 * @author Shane McPhillips
 *
 * @version 1.00	2026-10-07 Initial version for HW2
 * @version 1.01	2026-10-07 Per-field locks and last-modified-by for Stories 3, 4, and 5
 */
public class LessonRecord extends LessonsLearned {

	/*-*******************************************************************************************

	Field names used to lock a single field (see database.Database.setLessonFieldLock)

	 */

	/** Names the title field when locking or unlocking it */
	public static final String FIELD_TITLE = "title";
	/** Names the description field when locking or unlocking it */
	public static final String FIELD_DESCRIPTION = "description";
	/** Names the category field when locking or unlocking it */
	public static final String FIELD_CATEGORY = "category";
	/** Names the effort field when locking or unlocking it */
	public static final String FIELD_EFFORT = "effort";


	/*-*******************************************************************************************

	Attributes

	 */

	private int id;							// Database key, assigned by the database on insert
	private String creatorUsername;			// Owner: only this contributor may see or manage it
	private LocalDateTime createdAt;		// Shown in the "View My Lessons" list (Story 2)
	private boolean titleLocked;			// Each lock is set by a curator. A locked field
	private boolean descriptionLocked;		// cannot be edited (Story 4), and any lock blocks
	private boolean categoryLocked;			// deleting the lesson (Stories 3 and 5)
	private boolean effortLocked;
	private String lastModifiedBy;			// Who last changed the lesson; null if never changed


	/*-*******************************************************************************************

	Constructors

	 */

	/**********
	 * <p> Method: LessonRecord(...) </p>
	 *
	 * <p> Description: Builds a stored lesson from the values read back from the database (or
	 * just written to it). Every field is supplied at once because a LessonRecord always
	 * represents a row that already exists.</p>
	 *
	 * @param id				the database id of the lesson
	 * @param title				the lesson's title
	 * @param description		the lesson's description
	 * @param category			the lesson's category
	 * @param effort			the effort in hours
	 * @param creatorUsername	the username of the contributor who created the lesson
	 * @param createdAt			when the lesson was created
	 * @param titleLocked		true if a curator has locked the title
	 * @param descriptionLocked	true if a curator has locked the description
	 * @param categoryLocked	true if a curator has locked the category
	 * @param effortLocked		true if a curator has locked the effort
	 * @param lastModifiedBy	the username of whoever last changed the lesson, or null
	 */
	public LessonRecord(int id, String title, String description, String category, float effort,
			String creatorUsername, LocalDateTime createdAt, boolean titleLocked,
			boolean descriptionLocked, boolean categoryLocked, boolean effortLocked,
			String lastModifiedBy) {
		super(title, description, category, effort);
		this.id = id;
		this.creatorUsername = creatorUsername;
		this.createdAt = createdAt;
		this.titleLocked = titleLocked;
		this.descriptionLocked = descriptionLocked;
		this.categoryLocked = categoryLocked;
		this.effortLocked = effortLocked;
		this.lastModifiedBy = lastModifiedBy;
	}


	/*-*******************************************************************************************

	Getters and setters

	 */

	/** 
	 * <p> Method: getId()</p>
	 * @return the database id of this lesson */
	public int getId() { return id; }

	/**  
	 * <p> Method: getCreatorUsername()</p>
	 * @return the username of the contributor who created this lesson */
	public String getCreatorUsername() { return creatorUsername; }

	/**  
	 * <p> Method: getCreateAt()</p>
	 * @return the date and time this lesson was created */
	public LocalDateTime getCreatedAt() { return createdAt; }

	/**  
	 * <p> Method: isTitleLocked()</p>
	 * @return true if a curator has locked the title */
	public boolean isTitleLocked() { return titleLocked; }

	/**  
	 * <p> Method: isDescriptionLocked()</p>
	 * @return true if a curator has locked the description */
	public boolean isDescriptionLocked() { return descriptionLocked; }

	/**  
	 * <p> Method: isCategoryLocked()</p>
	 * @return true if a curator has locked the category */
	public boolean isCategoryLocked() { return categoryLocked; }

	/**  
	 * <p> Method: isEffortLocked()</p>
	 * @return true if a curator has locked the effort */
	public boolean isEffortLocked() { return effortLocked; }

	/**  
	 * <p> Method: getLastModifiedBy()</p>
	 * @return the username of whoever last changed this lesson, or null if never changed */
	public String getLastModifiedBy() { return lastModifiedBy; }

	/**********
	 * <p> Method: isLocked() </p>
	 *
	 * @return true if any field of this lesson is locked, which means it cannot be deleted
	 */
	public boolean isLocked() {
		return titleLocked || descriptionLocked || categoryLocked || effortLocked;
	}

	/**********
	 * <p> Method: isAlteredByAnother() </p>
	 *
	 * <p> Description: Story 5 blocks deleting a lesson that someone other than its owner has
	 * changed, so that work is not thrown away by the owner without the other person knowing.</p>
	 *
	 * @return true if the last change to this lesson was made by someone other than its owner
	 */
	public boolean isAlteredByAnother() {
		return lastModifiedBy != null && !lastModifiedBy.equals(creatorUsername);
	}


	/**********
	 * <p> Method: toString() </p>
	 *
	 * <p> Description: A short form used when printing lessons during testing.</p>
	 *
	 * @return the id, title, and category of this lesson
	 */
	@Override
	public String toString() {
		return "#" + id + " " + getTitle() + " [" + getCategory() + "]";
	}
}
