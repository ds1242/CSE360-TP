package guiCurator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.Database;
import entityClasses.LessonsLearnedList;
import entityClasses.User;
import entityClasses.LessonRecord;

/*******
 * <p> Title: ModelCurator Class. </p>
 *
 * <p> Description: Provides the data management functionality for the Curator page.
 * Retrieves lessons and contributor information from the database and manages
 * the visibility of experience statistics (Avg and Sum) for individual
 * contributors. Visibility settings are stored separately for each contributor
 * and default to disabled when no setting exists.</p>
 */
public class ModelCurator {

    /**
     * Error message displayed when a database operation cannot be completed.
     */
    public static final String DATABASE_ERROR = "Unable to connect to database at this time...";

    private static Database database;

    /**********
     * <p> Method: ModelCurator() </p>
     *
     * <p> Description: Constructs a ModelCurator instance. The default constructor
     * is not required for the static database operations provided by this class.</p>
     */
    public ModelCurator() {
    }

    /**********
     * <p> Method: setDatabase(Database db) </p>
     *
     * <p> Description: Sets the database instance used by the ModelCurator class.
     * Allows a separate database to be provided for testing purposes, such as an
     * in-memory H2 database. If no database is assigned, the application's default
     * database is used.</p>
     *
     * @param db the database instance to use
     */
    public static void setDatabase(Database db) {
        database = db;
    }

    /**********
     * <p> Method: db() </p>
     *
     * <p> Description: Retrieves the currently assigned database instance. Uses the
     * application's default database if no alternate database has been assigned.
     * Throws an exception if no database has been initialized.</p>
     *
     * @return the active database instance
     * @throws SQLException if no database has been initialized
     */
    private static Database db() throws SQLException {
        // Use the test database when assigned; otherwise use the application's database.
        Database d = (database != null) ? database : applicationMain.FoundationsMain.database;
        if (d == null) {
            throw new SQLException("Database has not been initialized.");
        }
        return d;
    }

    /**********
     * <p> Method: loadAllLessons() </p>
     *
     * <p> Description: Retrieves all lessons stored in the database for display
     * on the Curator page.</p>
     *
     * @return a list containing all stored lessons
     * @throws SQLException if the database cannot be accessed
     */
    public static LessonsLearnedList loadAllLessons() throws SQLException {
        return db().getAllLessons();
    }

    /**********
     * <p> Method: loadContributorNames() </p>
     *
     * <p> Description: Retrieves the usernames of all users assigned the Contributor
     * role. Only users identified as contributors are included in the returned list.</p>
     *
     * @return a list of contributor usernames, or an empty list if none exist
     * @throws SQLException if the database cannot be accessed
     */
    public static List<String> loadContributorNames() throws SQLException {
        List<String> names = new ArrayList<>();
        for (User u : db().getAllUsers()) {

            // Only users with the Contributor role are included in the list.
            if (u.getContributorRole())
                names.add(u.getUserName());
        }
        return names;
    }

    /**********
     * <p> Method: getContributorCanViewExperienceStats(String userName) </p>
     *
     * <p> Description: Retrieves the experience statistics visibility setting
     * for a specified contributor. Determines whether the contributor is permitted
     * to view the Avg and Sum statistics.</p>
     *
     * @param userName the username of the contributor
     * @return true if the contributor is allowed to view experience statistics;
     *         false otherwise
     * @throws SQLException if the database cannot be accessed
     */
    public static boolean getContributorCanViewExperienceStats(String userName) throws SQLException {
        return db().getContributorCanViewExperienceStats(userName);
    }

    /**********
     * <p> Method: setContributorCanViewExperienceStats(String userName, boolean enabled) </p>
     *
     * <p> Description: Updates the experience statistics visibility setting for
     * an individual contributor. Allows the curator to enable or disable access
     * to Avg and Sum statistics. The setting applies only to the specified
     * contributor and does not affect other contributors.</p>
     *
     * @param userName the username of the contributor whose visibility setting will
     *        be updated
     * @param enabled true to allow the contributor to view Avg and Sum statistics;
     *        false to hide them
     * @throws SQLException if the database setting cannot be updated
     */
    public static void setContributorCanViewExperienceStats(String userName, boolean enabled) throws SQLException {
        db().setContributorCanViewExperienceStats(userName, enabled);
    }

    /**********
     * <p> Method: getLesson(int lessonId) </p>
     *
     * <p> Description: Retrieves an individual lesson from the database using its
     * unique lesson identifier.</p>
     *
     * @param lessonId the unique database identifier of the lesson
     * @return the requested lesson record, or null if no matching lesson exists
     * @throws SQLException if the database cannot be accessed
     */
    public static LessonRecord getLesson(int lessonId) throws SQLException {
        return db().getLesson(lessonId);
    }
}