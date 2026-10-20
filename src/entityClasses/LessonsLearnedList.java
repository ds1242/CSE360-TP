package entityClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/*******
 * <p> Title: LessonsLearnedList Class </p>
 *
 * <p> Description: This LessonsLearnedList class holds a group of lessons learned </p>
 *
 * @author Edis Jakupovic
 *
 */
public class LessonsLearnedList {
	private List<LessonsLearned> lessons;

	public LessonsLearnedList() {
		lessons = new ArrayList<LessonsLearned>();
	}

	public LessonsLearnedList(List<LessonsLearned> lessons) {
		this.lessons = new ArrayList<LessonsLearned>();
		if (lessons != null) this.lessons.addAll(lessons);
	}

	public void add(LessonsLearned lesson) {
		if (lesson != null) lessons.add(lesson);
	}

	public boolean remove(LessonsLearned lesson) {
		return lessons.remove(lesson);
	}

	public LessonsLearned get(int index) {
		if (index < 0 || index >= lessons.size()) return null;
		return lessons.get(index);
	}

	public int size() { return lessons.size(); }

	public boolean isEmpty() { return lessons.isEmpty(); }

	public void clear() { lessons.clear(); }

	public List<LessonsLearned> toList() {
		return new ArrayList<LessonsLearned>(lessons);
	}

	public LessonsLearnedList subset(Predicate<LessonsLearned> test) {
		LessonsLearnedList result = new LessonsLearnedList();
		if (test == null) return result;
		for (LessonsLearned lesson : lessons)
			if (test.test(lesson)) result.add(lesson);
		return result;
	}

	public LessonsLearnedList search(String keyword) {
		if (keyword == null || keyword.trim().length() == 0)
			return new LessonsLearnedList(lessons);

		final String target = keyword.trim().toLowerCase();
		return subset(lesson -> contains(lesson.getTitle(), target)
				|| contains(lesson.getCoreInformation(), target)
				|| contains(lesson.getExperience(), target));
	}

	private static boolean contains(String field, String target) {
		return field != null && field.toLowerCase().contains(target);
	}
}
