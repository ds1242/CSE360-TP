package guiCurator;
 
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
import entityClasses.LessonsLearnedList;
import javafx.collections.FXCollections;
import entityClasses.LessonRecord;
 
/*******
 * <p> Title: ControllerCurator Class. </p>
 *
 * <p> Description: The JavaFX-based Curator page controller. It fills the lesson table and
 * the contributor list, and saves the per-contributor "show Avg and Sum" setting the moment
 * the curator changes it.</p>
 *
 * @author Andrew Kannas
 */
public class ControllerCurator {
 
    /**********
     * <p> Method: ControllerCurator() </p>
     *
     * <p> Description: Default constructor for the ControllerCurator class.
     * It is not directly used.</p>
     */
    public ControllerCurator() {
    }
 
    /**********
     * <p> Method: repaintTheWindow() </p>
     *
     * <p> Description: Loads all lessons and contributors, updates the lesson table
     * and count label, and displays the Curator page. If loading lessons fails,
     * the table is cleared and a database error is displayed.</p>
     */
    protected static void repaintTheWindow() {
 
        ViewCurator.label_UserDetails.setText("User: "
                + ViewCurator.theUser.getUserName());
        ViewCurator.label_Message.setText("");
 
        try {
            LessonsLearnedList allLessons = ModelCurator.loadAllLessons();
 
            ViewCurator.table_Lessons.setItems(
                    FXCollections.observableArrayList(allLessons.getLessons()));
 
            ViewCurator.label_LessonCount.setText(
                    "There are " + allLessons.size()
                    + (allLessons.size() == 1 ? " lesson." : " lessons."));
        }
        catch (SQLException e) {
            e.printStackTrace();
            ViewCurator.table_Lessons.setItems(FXCollections.observableArrayList());
            ViewCurator.label_LessonCount.setText("");
            ViewCurator.showError(ModelCurator.DATABASE_ERROR);
        }
 
        loadContributors();
 
        ViewCurator.theStage.setTitle("CSE 360 Foundations: Curator Lessons");
        ViewCurator.theStage.setScene(ViewCurator.theCuratorScene);
        ViewCurator.theStage.show();
    }
 
    /**********
     * <p> Method: loadContributors() </p>
     *
     * <p> Description: Reloads the contributor drop-down using the names stored
     * in the database. Preserves the previous selection if that contributor
     * is still available and refreshes the associated statistics checkbox.</p>
     */
    private static void loadContributors() {
        String previous = ViewCurator.combo_Contributor.getValue();
        List<String> names = new ArrayList<>();
        try {
            names = ModelCurator.loadContributorNames();
        }
        catch (SQLException e) {
            e.printStackTrace();
            ViewCurator.showError(ModelCurator.DATABASE_ERROR);
        }
 
        ViewCurator.combo_Contributor.setItems(FXCollections.observableArrayList(names));

        // Keep the current selection only if that contributor is still in the updated list.
        if (previous != null && names.contains(previous))
            ViewCurator.combo_Contributor.setValue(previous);
        else
            ViewCurator.combo_Contributor.setValue(null);
 
        refreshStatsCheckBox();
    }
 
    /**********
     * <p> Method: refreshStatsCheckBox() </p>
     *
     * <p> Description: Updates the statistics checkbox to reflect the selected
     * contributor's saved preference. Disables and clears the checkbox when no
     * contributor is selected. If the database setting cannot be retrieved,
     * the checkbox defaults to OFF.</p>
     */
    private static void refreshStatsCheckBox() {
        String name = ViewCurator.combo_Contributor.getValue();
 
        if (name == null) {
            ViewCurator.check_ShowStats.setSelected(false);
            ViewCurator.check_ShowStats.setDisable(true);
            return;
        }
 
        ViewCurator.check_ShowStats.setDisable(false);
        try {
            ViewCurator.check_ShowStats.setSelected(
                    ModelCurator.getContributorCanViewExperienceStats(name));
        }
        catch (SQLException e) {
            e.printStackTrace();

            // Default to OFF if the saved preference cannot be retrieved.
            ViewCurator.check_ShowStats.setSelected(false);
            ViewCurator.showError(ModelCurator.DATABASE_ERROR);
        }
    }
 
    /**********
     * <p> Method: performSelectContributor() </p>
     *
     * <p> Description: Handles the selection of a contributor by clearing
     * any previous message and displaying that contributor's saved statistics
     * visibility setting.</p>
     */
    protected static void performSelectContributor() {
        ViewCurator.label_Message.setText("");
        refreshStatsCheckBox();
    }
 
    /**********
     * <p> Method: performToggleStats() </p>
     *
     * <p> Description: Saves the selected contributor's statistics visibility
     * setting immediately after the checkbox is changed. Displays a success
     * message when the update succeeds or restores the previous checkbox state
     * if the database update fails.</p>
     */
    protected static void performToggleStats() {
        String name = ViewCurator.combo_Contributor.getValue();
        if (name == null) return;
 
        boolean enabled = ViewCurator.check_ShowStats.isSelected();
        try {
            ModelCurator.setContributorCanViewExperienceStats(name, enabled);
            ViewCurator.showSuccess("Avg and Sum are now "
                    + (enabled ? "visible to " : "hidden from ") + name + ".");
        }
        catch (SQLException e) {
            e.printStackTrace();

            // Restore the previous checkbox state because the database update failed.
            ViewCurator.check_ShowStats.setSelected(!enabled);
            ViewCurator.showError(ModelCurator.DATABASE_ERROR);
        }
    }
 
    /**********
     * <p> Method: performViewLesson() </p>
     *
     * <p> Description: Retrieves the lesson selected in the lesson table.
     * Displays an error message and stops if no lesson has been selected.</p>
     */
    protected static void performViewLesson() {
 
        LessonRecord selected =
                ViewCurator.table_Lessons.getSelectionModel().getSelectedItem();
 
        if (selected == null) {
            ViewCurator.showError("A lesson must be selected in order to view.");
            return;
        }
 
    }
 
    /**********
     * <p> Method: performUpdate() </p>
     *
     * <p> Description: Directs the currently logged-in curator to the
     * User Update page.</p>
     */
    protected static void performUpdate() {
        guiUserUpdate.ViewUserUpdate.displayUserUpdate(
                ViewCurator.theStage, ViewCurator.theUser);
    }
 
    /**********
     * <p> Method: performReturn() </p>
     *
     * <p> Description: Handles returning the curator to the Curator Home page.
     * Navigation functionality will be added here.</p>
     */
    protected static void performReturn() {
        // Curator Home navigation will be added here.
    }
 
    /**********
     * <p> Method: performLogout() </p>
     *
     * <p> Description: Logs the current user out and returns to the login page.</p>
     */
    protected static void performLogout() {
        guiUserLogin.ViewUserLogin.displayUserLogin(ViewCurator.theStage);
    }
 
    /**********
     * <p> Method: performQuit() </p>
     *
     * <p> Description: Terminates the application when the curator chooses to quit.</p>
     */
    protected static void performQuit() {
        System.exit(0);
    }
}