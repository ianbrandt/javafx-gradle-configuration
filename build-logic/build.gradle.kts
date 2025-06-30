plugins {
	// Not using `id("kotlin-dsl")` per:
	// https://github.com/gradle/gradle/issues/23884
	`kotlin-dsl`
}

group = "com.ianbrandt.buildlogic"
version = "1.0.0-SNAPSHOT"

dependencies {

	implementation(platform("com.ianbrandt.platforms:plugins-platform"))

	implementation(libs.dependencyAnalysis.gradlePluginDependency)
	implementation(libs.jvmDependencyConflictResolution.gradlePluginDependency)
	implementation(libs.kotlin.gradlePluginDependency)
	implementation(libs.osDetector.gradlePluginDependency)
}

gradlePlugin {
	plugins {
		register("javaFxNonModularApplicationPlugin") {
			id = "com.ianbrandt.buildlogic.javafx-non-modular-application"
			implementationClass =
				"com.ianbrandt.buildlogic.plugins.javafx.JavaFXNonModularApplicationPlugin"
		}
		register("javaFxNonModularTestSuitePlugin") {
			id = "com.ianbrandt.buildlogic.javafx-non-modular-test-suite"
			implementationClass =
				"com.ianbrandt.buildlogic.plugins.javafx.JavaFXNonModularTestSuitePlugin"
		}
	}
}
