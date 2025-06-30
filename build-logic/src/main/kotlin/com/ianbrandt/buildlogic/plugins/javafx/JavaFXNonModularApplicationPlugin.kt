package com.ianbrandt.buildlogic.plugins.javafx

import com.ianbrandt.buildlogic.tasks.JvmTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.named

@Suppress("unused")
open class JavaFXNonModularApplicationPlugin : Plugin<Project> {

	override fun apply(project: Project) {

		project.pluginManager.withPlugin("application") {

			project.tasks.named<JavaExec>("run") {

				val runtimeClasspath: Configuration =
					project.configurations.getByName("runtimeClasspath")

				JvmTask.forTask(this)
					.configureNonModularJavaFXTask(runtimeClasspath)
			}
		}
	}
}
