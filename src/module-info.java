/**
 * Core module for the Foundations Fall 2026 CSE3560 Project.
 * This module defines the dependencies and package exports for the 
 * application.
 */
module FoundationsF26 {
	requires javafx.controls;
	requires java.sql;
    requires javafx.graphics;

	opens applicationMain to javafx.graphics, javafx.fxml;
}
