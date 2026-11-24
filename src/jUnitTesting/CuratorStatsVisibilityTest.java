
package jUnitTesting;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import database.Database;
import guiCurator.*;

/**
 * Tests the visibility settings for curator experience statistics.
 * <p>
 * Method: CuratorStatsVisibilityTest </p>
 * <p>
 * Description: Verifies that experience statistics visibility is managed
 * independently for each contributor and defaults to disabled when no
 * visibility setting has been stored. </p>
 */
class CuratorStatsVisibilityTest {

    private static Database db;

    /**
     * Method: setUp
     * <p>
     * Description: Initializes the in-memory H2 database, establishes a
     * database connection, and assigns the database to ModelCurator for
     * testing purposes. </p>
     *
     * @throws SQLException if the database connection cannot be established
     */
    @BeforeAll
    static void setUp() throws SQLException {
        db = new Database("jdbc:h2:mem:curatorTest");
        db.connectToDatabase();
        ControllerCurator.setDatabase(db);
    }

    /**
     * Method: tearDown
     * <p>
     * Description: Closes the database connection after all unit tests
     * have completed. </p>
     */
    @AfterAll
    static void tearDown() {
        db.closeConnection();
    }

    /**
     * Method: avgSumVisibilityIsPerContributorAndDefaultsOff
     * <p>
     * Description: Tests that contributor experience statistics visibility
     * defaults to false and that enabling visibility for one contributor
     * does not change the visibility setting of another contributor. </p>
     *
     * @throws SQLException if a database operation fails during testing
     */
    @Test
    void avgSumVisibilityIsPerContributorAndDefaultsOff() throws SQLException {
        
        assertFalse(ControllerCurator.getContributorCanViewExperienceStats("alice"));

        
        ControllerCurator.setContributorCanViewExperienceStats("alice", true);

        assertTrue(ControllerCurator.getContributorCanViewExperienceStats("alice"));
        assertFalse(ControllerCurator.getContributorCanViewExperienceStats("bob"));
    }
}