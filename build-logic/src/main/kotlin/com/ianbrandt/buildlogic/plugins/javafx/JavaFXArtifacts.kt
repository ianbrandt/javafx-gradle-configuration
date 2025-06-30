package com.ianbrandt.buildlogic.plugins.javafx

import org.gradle.api.GradleException
import org.gradle.api.file.FileCollection
import java.io.File

/**
 * Supporting code related to JavaFX artifacts.
 */
internal object JavaFXArtifacts {

	/**
	 * The group ID for published JavaFX artifacts.
	 */
	const val JAVAFX_GROUP_ID = "org.openjfx"

	/**
	 * Get the set of JavaFX module names that correspond to any JavaFX
	 * artifacts found on the given path.
	 */
	fun deriveModuleNamesFromModulePath(
		javaFxModulePath: FileCollection
	): Set<String> =
		javaFxModulePath.map { moduleFile: File ->
			when {
				moduleFile.path.contains("javafx-base") -> "javafx.base"
				moduleFile.path.contains("javafx-controls") -> "javafx.controls"
				moduleFile.path.contains("javafx-fxml") -> "javafx.fxml"
				moduleFile.path.contains("javafx-graphics") -> "javafx.graphics"
				moduleFile.path.contains("javafx-media") -> "javafx.media"
				moduleFile.path.contains("javafx-swing") -> "javafx.swing"
				moduleFile.path.contains("javafx-web") -> "javafx.web"
				else -> {
					throw GradleException("Unknown JavaFX module: $moduleFile")
				}
			}
		}.toSet()
}
