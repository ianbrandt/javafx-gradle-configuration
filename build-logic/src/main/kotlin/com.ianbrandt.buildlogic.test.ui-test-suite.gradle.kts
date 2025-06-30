import com.ianbrandt.buildlogic.conventions.registerCustomJvmTestSuite

val testSuiteName = "uiTest"
val testSuiteTestSuffix = "UIT"

registerCustomJvmTestSuite(testSuiteName, testSuiteTestSuffix)

@Suppress("UnstableApiUsage")
configure<TestingExtension> {
	suites {
		named<JvmTestSuite>(testSuiteName) {
			dependencies {
				// Version catalog type-safe accessors not available in precompiled
				// script plugins: https://github.com/gradle/gradle/issues/15383
				val versionCatalog = versionCatalogs.named("libs")

				implementation(
					versionCatalog.findLibrary("testfx-core").get()
				)
				implementation(
					versionCatalog.findLibrary("testfx-junit5").get()
				)
				implementation(
					versionCatalog.findLibrary("testfx-monocle").get()
				)
			}
			targets {
				all {
					testTask.configure {
						val headless: String? by project
						if ((headless ?: "true") == "true") {
							systemProperty("prism.order", "sw")
							systemProperty("prism.text", "t2k")
							systemProperty("testfx.headless", "true")
							systemProperty("testfx.robot", "glass")
						}
						jvmArgs(
							"--add-exports",
							"javafx.base/com.sun.javafx.logging=org.testfx.monocle",
							"--add-exports",
							"javafx.graphics/com.sun.glass.ui=org.testfx.monocle",
							"--add-exports",
							"javafx.graphics/com.sun.javafx.application=org.testfx",
							"--add-exports",
							"javafx.graphics/com.sun.javafx.util=org.testfx.monocle",
							"--add-opens",
							"javafx.graphics/com.sun.glass.ui=org.testfx",
							"--add-opens",
							"javafx.graphics/com.sun.javafx.application=org.testfx",
						)
					}
				}
			}
		}
	}
}
