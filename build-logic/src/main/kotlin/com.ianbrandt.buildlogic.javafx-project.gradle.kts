import com.ianbrandt.buildlogic.artifacts.controlsfx.ControlsFXComponentMetadataRule
import com.ianbrandt.buildlogic.artifacts.javafx.JavaFXComponentMetadataRule

plugins {
	// Ensure the native JavaFX variant for the current operating system and
	// architecture is selected by default.
	id("com.ianbrandt.buildlogic.native-dependency-convention")
}

dependencies {
	// Add Gradle metadata for the native variants of all the JavaFX
	// dependencies.
	components {
		withModule<JavaFXComponentMetadataRule>("org.openjfx:javafx-base")
		withModule<JavaFXComponentMetadataRule>("org.openjfx:javafx-controls")
		withModule<JavaFXComponentMetadataRule>("org.openjfx:javafx-fxml")
		withModule<JavaFXComponentMetadataRule>("org.openjfx:javafx-graphics")
		withModule<JavaFXComponentMetadataRule>("org.openjfx:javafx-media")
		withModule<JavaFXComponentMetadataRule>("org.openjfx:javafx-swing")
		withModule<JavaFXComponentMetadataRule>("org.openjfx:javafx-web")

		// Fix ControlsFX metadata.
		withModule<ControlsFXComponentMetadataRule>("org.controlsfx:controlsfx")
	}
}
