/**
 * <p> Module: FoundationsF26 </p>
 * 
 * <p> Description: provides project module insight into binding libraries. </p>
 */
module FoundationsF26 {
	requires javafx.controls;
	requires java.sql;
    requires javafx.graphics;
	requires org.junit.jupiter.api;
	requires java.net.http;

	opens applicationMain to javafx.graphics, javafx.fxml;
}
