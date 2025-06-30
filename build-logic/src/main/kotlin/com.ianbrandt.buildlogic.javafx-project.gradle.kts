import com.ianbrandt.buildlogic.artifacts.javafx.JavaFXComponentMetadataRule
import com.ianbrandt.buildlogic.attributes.nativeplatform.CurrentArchitectureAttributeDisambiguationRule
import com.ianbrandt.buildlogic.attributes.nativeplatform.CurrentOsAttributeDisambiguationRule
import org.gradle.nativeplatform.MachineArchitecture.ARCHITECTURE_ATTRIBUTE
import org.gradle.nativeplatform.OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE

plugins {
	id("com.google.osdetector")
}

dependencies {
	attributesSchema {
		attribute(OPERATING_SYSTEM_ATTRIBUTE) {
			disambiguationRules.add(
				CurrentOsAttributeDisambiguationRule::class.java
			) {
				params(
					objects.named<OperatingSystemFamily>(osdetector.os)
				)
			}
		}
		attribute(ARCHITECTURE_ATTRIBUTE) {
			disambiguationRules.add(
				CurrentArchitectureAttributeDisambiguationRule::class.java
			) {
				params(
					objects.named<MachineArchitecture>(osdetector.arch)
				)
			}
		}
	}
	components {
		withModule<JavaFXComponentMetadataRule>("org.openjfx:javafx-base")
		withModule<JavaFXComponentMetadataRule>("org.openjfx:javafx-controls")
		withModule<JavaFXComponentMetadataRule>("org.openjfx:javafx-fxml")
		withModule<JavaFXComponentMetadataRule>("org.openjfx:javafx-graphics")
		withModule<JavaFXComponentMetadataRule>("org.openjfx:javafx-swing")
		withModule<JavaFXComponentMetadataRule>("org.openjfx:javafx-web")
	}
}
