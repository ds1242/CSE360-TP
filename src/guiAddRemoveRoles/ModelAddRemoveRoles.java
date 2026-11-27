package guiAddRemoveRoles;

import database.Database;
import entityClasses.User.Role;

/*******
 * <p> Title: ModelAddRemoveRoles Class. </p>
 * 
 * <p> Description: The AddRemoveRoles Page Model.  This class is not used as there is no
 * data manipulated by this MVC beyond accepting role information and saving it in the
 * database.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-15 Initial version
 *  
 */

public class ModelAddRemoveRoles {
	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	protected static boolean hasUserAccountDetails(String username) {
		return theDatabase.getUserAccountDetails(username);
	}
	
	protected static boolean hasAdminRole() {
		return theDatabase.getCurrentAdminRole();
	}
	
	protected static boolean hasContributorRole() {
		return theDatabase.getCurrentContributorRole();
	}
	
	protected static boolean hasCuratorRole() {
		return theDatabase.getCurrentCuratorRole();
	}
	
	protected static boolean[] hasRoles() {
		return theDatabase.getCurrentRoles();
	}
	
	protected static boolean hasUpdateUser(String username, Role role, boolean value) {
		return theDatabase.updateUserRole(username, role, value);
	}

}
