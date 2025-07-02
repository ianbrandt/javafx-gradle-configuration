package com.ianbrandt.buildlogic.conventions.test

import com.autonomousapps.DependencyAnalysisSubExtension
import com.ianbrandt.buildlogic.test.Compilations.TEST_FIXTURES_COMPILATION_NAME
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.jvm.JvmTestSuite
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.gradle.testing.base.TestingExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation.Companion.MAIN_COMPILATION_NAME

object CustomJvmTestSuiteConvention {

	/**
	 * A convention function for configuring custom JVM Test Suite Plugin suites.
	 */
	fun Project.registerCustomJvmTestSuite(
		testSuiteName: String,
		testSuiteTestSuffix: String,
	) {
		pluginManager.apply("com.ianbrandt.buildlogic.test.test-suite-convention")

		@Suppress("UnstableApiUsage")
		configure<TestingExtension> {
			suites {
				val test by getting(JvmTestSuite::class)
				register<JvmTestSuite>(testSuiteName) {
					targets.all {
						testTask.configure {
							filter {
								includeTestsMatching("*$testSuiteTestSuffix")
								// Support JUnit @Nested tests
								includeTestsMatching("*$testSuiteTestSuffix$*")
							}
							shouldRunAfter(test)
						}
					}
				}
			}
		}

		tasks.named<Task>("check").configure {
			val testTask = tasks.named<Task>(testSuiteName)
			dependsOn(testTask)
		}

		pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
			configure<KotlinJvmProjectExtension> {
				target {
					// Workaround for https://youtrack.jetbrains.com/issue/KTIJ-23114.
					compilations.getByName(testSuiteName)
						.associateWith(
							compilations.getByName(
								MAIN_COMPILATION_NAME
							)
						)
					compilations.findByName(TEST_FIXTURES_COMPILATION_NAME)
						?.let {
							compilations.getByName(testSuiteName)
								.associateWith(it)
						}
				}
			}
		}

		pluginManager.withPlugin("com.autonomousapps.dependency-analysis") {
			configure<DependencyAnalysisSubExtension> {
				abi {
					exclusions {
						excludeSourceSets(testSuiteName)
					}
				}
				issues {
					// Ignore test source set to work around
					// https://github.com/autonomousapps/dependency-analysis-gradle-plugin/issues/1239.
					ignoreSourceSet(testSuiteName)
				}
			}
		}
	}
}
