package com.ianbrandt.buildlogic.plugins.javafx

import com.ianbrandt.buildlogic.plugins.javafx.JavaFXNonModularPlugins.configureNonModularJavaFXTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.named

/**
 * A plugin for non-modular JavaFX projects that use the Gradle Application
 * plugin. Moves any JavaFX dependencies found on the runtime classpath of the
 * `run` task to the module path as roots. Does nothing if the task is already
 * modular.
 */
@Suppress("unused")
open class JavaFXNonModularApplicationPlugin : Plugin<Project> {

	override fun apply(project: Project) {

		project.pluginManager.withPlugin("application") {

			project.tasks.named<JavaExec>("run").configure {

				val runtimeClasspath: Configuration =
					project.configurations.getByName("runtimeClasspath")

				configureNonModularJavaFXTask(this, runtimeClasspath)
			}
		}
	}
}
