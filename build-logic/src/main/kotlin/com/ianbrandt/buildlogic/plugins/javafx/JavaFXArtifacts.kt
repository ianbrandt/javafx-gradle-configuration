package com.ianbrandt.buildlogic.plugins.javafx

import org.gradle.api.GradleException
import org.gradle.api.file.FileCollection
import java.io.File

internal object JavaFXArtifacts {

	const val JAVAFX_GROUP_ID = "org.openjfx"

	fun deriveModuleNamesFromModulePath(
		javaFxModulePath: FileCollection
	): List<String> =
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
		}
}
