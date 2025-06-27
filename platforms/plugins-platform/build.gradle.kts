plugins {
	id("java-platform")
}

dependencies {
	constraints {
		api(libs.dependencyAnalysis.gradlePluginDependency)
		api(libs.kotlin.gradlePluginDependency)
		api(libs.osDetector.gradlePluginDependency)
	}
}
