package com.ianbrandt.buildlogic.tasks.nonmodular

import org.gradle.api.Task
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.testing.Test
import org.gradle.process.JavaForkOptions

/**
 * Provides utilities to configure JVM tasks in non-modular Java projects by
 * adjusting their classpath and module path settings.
 */
object NonModularJvmTasks {

	fun JavaExec.configureNonModularJvmTask(
		modulePathInfo: ModulePathInfo,
	) = this.configureNonModularJvmTask(
		modulePathInfo,
		{ moduleArtifacts: FileCollection ->
			this.classpath = this.classpath.minus(moduleArtifacts)
		}
	)

	fun Test.configureNonModularJvmTask(
		modulePathInfo: ModulePathInfo,
	) = this.configureNonModularJvmTask(
		modulePathInfo,
		{ moduleArtifacts: FileCollection ->
			this.classpath = this.classpath.minus(moduleArtifacts)
		}
	)

	/**
	 * Configures a non-modular JVM task to support the module path when
	 * working with modular dependencies in a non-modular project. This function
	 * adjusts the classpath and sets the module path arguments for the JVM if
	 * necessary.
	 *
	 * @param modulePathInfo Information about the module path, including the
	 * collection of artifacts to be moved from the classpath to the module path
	 * and the provider for the set of module names to be added as roots.
	 * @param configureClasspath A function to configure the task's classpath
	 * by removing the given `moduleArtifacts` collection from it. Required to
	 * handle different Gradle `Task` subtypes generically.
	 */
	fun <T> T.configureNonModularJvmTask(
		modulePathInfo: ModulePathInfo,
		configureClasspath: T.(FileCollection) -> Unit,
	) where T : Task, T : JavaForkOptions = with(modulePathInfo) {

		doFirst {

			// If the task is already modular, the module path configuration is
			// handled by Gradle. If the given file collection is empty, nothing
			// needs doing.
			if (isModular() || moduleArtifacts.isEmpty) return@doFirst

			configureClasspath(moduleArtifacts)

			jvmArgumentProviders.add {
				buildList {
					// Set the module path for the task.
					add("--module-path")
					add(moduleArtifacts.asPath)

					// Since the task isn't modularized, add the given
					// modules as roots.
					val moduleNames: Set<String> = moduleNamesProvider.get()
					if (moduleNames.isNotEmpty()) {
						add("--add-modules")
						add(moduleNames.joinToString(","))
					}
				}
			}
		}
	}

	/**
	 * Determines whether the current task is modular based on the presence
	 * of module path arguments in the JVM arguments (`--module-path` or `-p`).
	 *
	 * @return `true` if the task is modular (i.e., it includes module path
	 * arguments in the JVM arguments); `false` otherwise.
	 */
	private fun JavaForkOptions.isModular(): Boolean =
		jvmArgs?.any { it == "--module-path" || it == "-p" } ?: false
}
