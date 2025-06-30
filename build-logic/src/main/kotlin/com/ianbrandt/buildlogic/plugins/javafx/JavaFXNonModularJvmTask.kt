package com.ianbrandt.buildlogic.plugins.javafx

import com.ianbrandt.buildlogic.plugins.javafx.JavaFXArtifacts.JAVAFX_GROUP_ID
import com.ianbrandt.buildlogic.plugins.javafx.JavaFXArtifacts.deriveModuleNamesFromModulePath
import com.ianbrandt.buildlogic.tasks.JvmTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.component.ModuleComponentIdentifier
import org.gradle.api.file.FileCollection

internal fun JvmTask.configureNonModularJavaFXTask(
	runtimeClasspath: Configuration
) {
	val javaFxModulePath: FileCollection =
		runtimeClasspath.incoming.artifactView {
			componentFilter { componentIdentifier ->
				componentIdentifier is ModuleComponentIdentifier
					&& componentIdentifier.group == JAVAFX_GROUP_ID
			}
		}.files

	doFirst {

		if (!javaFxModulePath.isEmpty) {

			classpath = classpath.minus(javaFxModulePath)

			val moduleNames: List<String> =
				deriveModuleNamesFromModulePath(
					javaFxModulePath
				)

			jvmArgumentProviders.add {
				listOf(
					"--module-path",
					javaFxModulePath.asPath,
					"--add-modules",
					moduleNames.joinToString(",")
				)
			}
		}
	}
}
