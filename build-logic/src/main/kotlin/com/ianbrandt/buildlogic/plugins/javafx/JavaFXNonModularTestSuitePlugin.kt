package com.ianbrandt.buildlogic.plugins.javafx

import com.ianbrandt.buildlogic.plugins.javafx.JavaFXModules.getJavaFxModulePathInfo
import com.ianbrandt.buildlogic.tasks.nonmodular.NonModularJvmTasks.configureNonModularJvmTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.base.TestingExtension

/**
 * A plugin for projects that use JavaFX from any non-modular Gradle JVM Test
 * Suite plugin test suites. Moves any JavaFX dependencies found on the runtime
 * classpath of the `Test` task of such suites to the module path as roots.
 * Does nothing if the task is already modular.
 */
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

						val javaFxModulePathInfo =
							getJavaFxModulePathInfo(
								runtimeClasspath,
								project.providers
							)

						configureNonModularJvmTask(javaFxModulePathInfo)
					}
				}
			}
		}
	}
}
