plugins {
	id("java-platform")
}

group = "com.ianbrandt.platforms"

dependencies {
	constraints {
		api(libs.dependency.analysis.gradle.plugin.dependency)
		api(libs.kotlin.gradle.plugin.dependency)
		api(libs.os.detector.gradle.plugin.dependency)
	}
}
