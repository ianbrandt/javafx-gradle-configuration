plugins {
	`jvm-test-suite`
	kotlin("jvm")
	id("com.ianbrandt.build.test.unit-test-suite")
}

val integrationTestSuiteName = "integrationTest"

@Suppress("UnstableApiUsage")
testing {
	suites {

		val test by getting(JvmTestSuite::class)

		register<JvmTestSuite>(integrationTestSuiteName) {

			dependencies {
				implementation(project())
			}

			sources {
				val sourcesRootDir = "src/it"
				java {
					setSrcDirs(listOf("$sourcesRootDir/java"))
				}
				kotlin {
					setSrcDirs(listOf("$sourcesRootDir/kotlin"))
				}
				resources {
					setSrcDirs(listOf("$sourcesRootDir/resources"))
				}
			}

			targets {
				all {
					testTask.configure {
						filter {
							val testSuffix = "IT"
							includeTestsMatching("*$testSuffix")
							// Support JUnit @Nested tests
							includeTestsMatching("*$testSuffix$*")
						}
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
