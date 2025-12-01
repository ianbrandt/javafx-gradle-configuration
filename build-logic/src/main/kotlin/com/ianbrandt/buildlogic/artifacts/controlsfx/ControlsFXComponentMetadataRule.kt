package com.ianbrandt.buildlogic.artifacts.controlsfx

import org.gradle.api.artifacts.ComponentMetadataContext
import org.gradle.api.artifacts.ComponentMetadataRule

/**
 * Fixes the Gradle metadata for ControlsFX.
 *
 * ControlsFX versions prior to 11.1.0 published Gradle metadata that selected
 * JavaFX artifacts via an artifact selector using a fixed classifier ("linux").
 * That causes Gradle to potentially resolve JavaFX jars for Linux in addition
 * to any selected for the current platform.
 *
 * ControlsFX 11.1.0 and later don't publish Gradle metadata, and they also
 * don't declare any dependencies on JavaFX in their POM. However, they do
 * still depend on `org.openjfx:*` artifacts, resulting in Gradle having
 * incorrect transitive dependency information for its dependency management
 * and incremental build processing.
 *
 * This rule removes any transitive `org.openjfx:*` dependencies declared by
 * ControlsFX, and re‑adds its dependencies without versions or classifiers.
 * Version alignment and platform (OS/arch) selection are then handled by the
 * consumer’s constraints and component metadata rules for JavaFX.
 */
abstract class ControlsFXComponentMetadataRule : ComponentMetadataRule {

	override fun execute(context: ComponentMetadataContext) {

		context.details.allVariants {
			withDependencies {
				// Drop any JavaFX dependencies published by ControlsFX.
				removeIf { it.group == "org.openjfx" }

				// Re‑add the JavaFX dependencies ControlsFX needs without
				// versions or classifiers so consumer rules decide the variant.
				add("org.openjfx:javafx-base")
				add("org.openjfx:javafx-graphics")
				add("org.openjfx:javafx-controls")
				add("org.openjfx:javafx-media")
			}
		}
	}
}
