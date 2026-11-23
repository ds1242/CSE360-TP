/**
 * <p> Module: FoundationsF26 </p>
 * 
 * <p> Description: provides project module insight into binding libraries. </p>
 */
module FoundationsF26 {
    requires javafx.controls;
    requires java.sql;
    requires javafx.graphics;
<<<<<<< HEAD
	requires org.junit.jupiter.api;
	requires java.net.http;

	opens applicationMain to javafx.graphics, javafx.fxml; 
    opens database to org.junit.platform.commons;
=======
    requires org.junit.jupiter.api;
    
    opens database to org.junit.platform.commons;
    opens applicationMain to javafx.graphics, javafx.fxml;
>>>>>>> cc7abf7 (changes)
}
