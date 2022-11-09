plugins {
	idea
	`java-library`
	`jvm-test-suite`
	kotlin("jvm")
	id("com.ianbrandt.build.test.unit-test-suite")
}

val integrationTestSuiteName = "integrationTest"

@Suppress("UnstableApiUsage")
testing {
	suites {

		register<JvmTestSuite>(integrationTestSuiteName) {

			dependencies {
				implementation(project)
			}

			sources {
				java {
					setSrcDirs(listOf("src/it/java"))
				}
				kotlin {
					setSrcDirs(listOf("src/it/kotlin"))
				}
			}

			targets {
				all {
					testTask.configure {
						filter {
							includeTestsMatching("*IT")
						}
						val test by getting(JvmTestSuite::class)
						shouldRunAfter(test)
					}
				}
			}
		}
	}
}

dependencies {

	// Version catalog not available in precompiled script plugins:
	// https://github.com/gradle/gradle/issues/15383

	"integrationTestImplementation"(
		platform("com.ianbrandt.platforms:test-platform")
	)

	//"integrationTestImplementation"(libs.assertj.core)
	"integrationTestImplementation"("org.assertj:assertj-core")
}

tasks {

	named<Task>("check").configure {
		val integrationTest by existing
		dependsOn(integrationTest)
	}
}

idea {
	module {
		testSourceDirs.addAll(
			kotlin.sourceSets[integrationTestSuiteName].kotlin.srcDirs
		)
		testResourceDirs.addAll(
			kotlin.sourceSets[integrationTestSuiteName].resources.srcDirs
		)
	}
}
