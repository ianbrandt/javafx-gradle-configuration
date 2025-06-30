package com.ianbrandt.buildlogic.plugins.javafx

import com.ianbrandt.buildlogic.plugins.javafx.JavaFXArtifacts.JAVAFX_GROUP_ID
import com.ianbrandt.buildlogic.plugins.javafx.JavaFXArtifacts.deriveModuleNamesFromModulePath
import org.gradle.api.GradleException
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.component.ModuleComponentIdentifier
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.testing.Test
import org.gradle.process.JavaForkOptions

/**
 * Common code for the non-modular JavaFX plugins.
 */
internal object JavaFXNonModularPlugins {

	/**
	 * Move any JavaFX dependencies from the given runtime classpath to the
	 * module path for the given task. Does nothing if the task is already
	 * modular.
	 */
	fun <T> configureNonModularJavaFXTask(
		task: T,
		runtimeClasspath: Configuration,
	) where T : Task, T : JavaForkOptions = with(task) {

		// A lazy `FileCollection` view of any JavaFX dependencies on the given
		// runtime classpath.
		val javaFxModulePath: FileCollection =
			runtimeClasspath.incoming.artifactView {
				componentFilter { componentIdentifier ->
					componentIdentifier is ModuleComponentIdentifier
						&& componentIdentifier.group == JAVAFX_GROUP_ID
				}
			}.files

		doFirst {

			// If the task is already modular, the module path configuration is
			// handled by Gradle. If there are otherwise no JavaFX dependencies
			// on the classpath, nothing needs doing.
			if (isModular() || javaFxModulePath.isEmpty) return@doFirst

			// No common interface for the `classpath` property between the
			// `JavaExec` and `Test` task types, and no union types in
			// Kotlin (yet), so pattern match to get and set the
			// corresponding classpath minus the JavaFX dependencies.
			when (this) {
				is JavaExec -> classpath = classpath.minus(javaFxModulePath)
				is Test -> classpath = classpath.minus(javaFxModulePath)
				else -> throw GradleException(
					"Unsupported task type ${this::class.qualifiedName}"
				)
			}

			val moduleNames: Set<String> =
				deriveModuleNamesFromModulePath(javaFxModulePath)

			jvmArgumentProviders.add {
				listOf(
					// Set the module path for the task.
					"--module-path", javaFxModulePath.asPath,
					// Since the task isn't modularized, add all the
					// JavaFX modules as roots.
					"--add-modules", moduleNames.joinToString(",")
				)
			}
		}
	}

	/**
	 * Determines whether a task is modular based on the presence or absence of
	 * the module path JVM argument.
	 */
	fun JavaForkOptions.isModular(): Boolean =
		jvmArgs?.any { it == "--module-path" || it == "-p" } == true
}
