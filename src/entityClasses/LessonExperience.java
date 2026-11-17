package entityClasses;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*******
 * <p> Title: LessonExperience Class </p>
 *
 * <p> Description: One experience entry for a lesson learned (Story 6). Each time someone makes
 * a new attempt to leverage a lesson, the contributor can record what was done, how it was done,
 * how long it took, and the effort each teammate put in.</p>
 *
 * <p> Why a separate class instead of a field of LessonsLearned? A lesson can collect any number
 * of experience entries over time, and each entry has its own list of teammates. Storing them as
 * their own objects (and their own database tables) means a new entry never has to rewrite the
 * lesson itself, and an entry can be edited or deleted without touching the others.</p>
 *
 * @author Shane McPhillips
 *
 * @version 1.00	2026-10-07 Initial version for HW2
 */
public class LessonExperience {

	// The date format used when an entry is listed on the Experience page. The time is left off
	// so the summary fits the narrow list without scrolling.
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");


	/*-*******************************************************************************************

	Attributes

	 */

	private int id;							// Database key, assigned by the database on insert
	private int lessonId;					// The lesson this entry belongs to
	private String whatWasDone;				// 20-2,000 characters
	private String howItWasDone;			// 20-2,000 characters
	private float durationHours;			// Greater than 0
	private LocalDateTime createdAt;		// When the entry was first recorded
	private List<TeammateEffort> teammates;	// One row per teammate who put in effort


	/*-*******************************************************************************************

	Constructors

	 */

	/**********
	 * <p> Method: LessonExperience(...) </p>
	 *
	 * <p> Description: Builds an experience entry from the values read back from the database
	 * (or just written to it).</p>
	 *
	 * @param id				the database id of the entry
	 * @param lessonId			the database id of the lesson it belongs to
	 * @param whatWasDone		what was done during the attempt
	 * @param howItWasDone		how it was done
	 * @param durationHours		how long it took, in hours
	 * @param createdAt			when the entry was first recorded
	 * @param teammates			the effort each teammate put in
	 */
	public LessonExperience(int id, int lessonId, String whatWasDone, String howItWasDone,
			float durationHours, LocalDateTime createdAt, List<TeammateEffort> teammates) {
		this.id = id;
		this.lessonId = lessonId;
		this.whatWasDone = whatWasDone;
		this.howItWasDone = howItWasDone;
		this.durationHours = durationHours;
		this.createdAt = createdAt;
		this.teammates = new ArrayList<>(teammates);
	}


	/*-*******************************************************************************************

	Getters

	 */

	/**  
	 * <p> Method: getId()</p>
	 * @return the database id of this entry */
	public int getId() { return id; }

	/**  
	 * <p> Method: getLessonId()</p>
	 * @return the database id of the lesson this entry belongs to */
	public int getLessonId() { return lessonId; }

	/**  
	 * <p> Method: getWhatWasDone()</p>
	 * @return what was done during the attempt */
	public String getWhatWasDone() { return whatWasDone; }

	/**  
	 * <p> Method: getHowItWasDone()</p>
	 * @return how it was done */
	public String getHowItWasDone() { return howItWasDone; }

	/**  
	 * <p> Method: getDurationHours()</p>
	 * @return how long it took, in hours */
	public float getDurationHours() { return durationHours; }

	/**  
	 * <p> Method: getCreatedAt()</p>
	 * @return when the entry was first recorded */
	public LocalDateTime getCreatedAt() { return createdAt; }

	/**  
	 * <p> Method: getTeammates()</p>
	 * @return a read-only view of the effort each teammate put in */
	public List<TeammateEffort> getTeammates() { return Collections.unmodifiableList(teammates); }


	/**********
	 * <p> Method: toString() </p>
	 *
	 * <p> Description: The text shown for this entry in the list on the Experience page: the
	 * date, the duration, and the start of what was done.</p>
	 *
	 * @return a one-line summary of this entry
	 */
	@Override
	public String toString() {
		String date = createdAt == null ? "" : createdAt.format(DATE_FORMAT);
		String what = whatWasDone.length() > 14 ? whatWasDone.substring(0, 14) + "..." : whatWasDone;
		return date + "  " + durationHours + " hrs  " + what;
	}
}
