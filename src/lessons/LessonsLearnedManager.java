package lessons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/*******
 * <p> Title: LessonsLearnedManager Class </p>
 *
 * <p> Description: Manages a list of lessons learned. Each LessonsLearnedManager object holds a
 * list of LessonRecords. The list may hold every lesson in the system or any subset of them (for
 * example, one contributor's lessons, or the result of a search). It may be empty, hold one
 * lesson, or hold any number of lessons; there is no fixed upper limit.</p>
 *
 * <p> Storing and loading lessons is done by database.Database (see createLesson,
 * getLessonsByCreator, getAllLessons, getLesson, updateLesson, and deleteLesson), which returns
 * its results as a LessonsLearnedManager. Keeping all of the SQL in Database means the
 * application has a single connection to the H2 database and one place to look for every query,
 * the same as for users and invitation codes.</p>
 *
 * @author Shane McPhillips
 *
 * @version 1.00	2026-10-07 Initial version for HW2
 * @version 1.01	2026-10-07 Database operations moved into database.Database
 */
public class LessonsLearnedManager {

	/*-*******************************************************************************************

	Attributes

	 */

	private final List<LessonRecord> lessons = new ArrayList<>();	// This manager's lessons


	/*-*******************************************************************************************

	Constructors

	 */

	/**********
	 * <p> Method: LessonsLearnedManager() </p>
	 *
	 * <p> Description: Creates an empty list of lessons. Use the lesson methods in
	 * database.Database to get a list filled from the database.</p>
	 */
	public LessonsLearnedManager() {
	}


	/*-*******************************************************************************************

	List operations

	 */

	/**********
	 * <p> Method: addLesson(LessonRecord) </p>
	 *
	 * <p> Description: Adds a lesson to this list. Null is ignored so the list never holds an
	 * entry that would break a GUI table or a loop.</p>
	 *
	 * @param lesson	the lesson to add
	 */
	public void addLesson(LessonRecord lesson) {
		if (lesson != null) lessons.add(lesson);
	}

	/**********
	 * <p> Method: getLessons() </p>
	 *
	 * <p> Description: Returns the lessons in this list. The returned list is read-only so code
	 * outside this class cannot change the list contents without going through it.</p>
	 *
	 * @return a read-only view of the lessons
	 */
	public List<LessonRecord> getLessons() {
		return Collections.unmodifiableList(lessons);
	}

	/**********
	 * <p> Method: getLesson(int) </p>
	 *
	 * @param index	the position of the lesson in this list (0 is the first)
	 * @return the lesson at that position, or null if the index is out of range
	 */
	public LessonRecord getLesson(int index) {
		if (index < 0 || index >= lessons.size()) return null;
		return lessons.get(index);
	}

	/** @return the number of lessons in this list */
	public int size() { return lessons.size(); }

	/** @return true if this list holds no lessons */
	public boolean isEmpty() { return lessons.isEmpty(); }

	/**********
	 * <p> Method: getSubset(Predicate) </p>
	 *
	 * <p> Description: Returns a new manager holding only the lessons that match the filter.
	 * This is how a subset (for example, the results of a search) is produced from a larger
	 * list. The original list is not changed.</p>
	 *
	 * @param filter	the test each lesson must pass to be included
	 * @return a new manager holding the matching lessons (possibly empty)
	 */
	public LessonsLearnedManager getSubset(Predicate<LessonRecord> filter) {
		LessonsLearnedManager subset = new LessonsLearnedManager();
		for (LessonRecord lesson : lessons)
			if (filter.test(lesson)) subset.addLesson(lesson);
		return subset;
	}
}
