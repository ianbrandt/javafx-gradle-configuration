package com.ianbrandt.buildlogic.plugins.javafx

import com.ianbrandt.buildlogic.tasks.JvmTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.base.TestingExtension

@Suppress("unused")
open class JavaFXNonModularTestSuitePlugin : Plugin<Project> {

	override fun apply(project: Project) {

		@Suppress("UnstableApiUsage")
		project.pluginManager.withPlugin("jvm-test-suite") {

			val testingExtension =
				project.extensions.getByType<TestingExtension>()

			testingExtension.suites.withType<JvmTestSuite>().configureEach {
				targets.configureEach {
					testTask.configure {

						val runtimeClasspath: Configuration =
							project.configurations
								.getByName("${name}RuntimeClasspath")

						JvmTask.forTask(this)
							.configureNonModularJavaFXTask(runtimeClasspath)
					}
				}
			}
		}
	}
}
