plugins {
	`kotlin-dsl`
}

dependencies {

	implementation(platform("com.ianbrandt.platforms:plugins-platform"))

	implementation(libs.dependency.analysis.gradle.plugin.dependency)
	implementation(libs.kotlin.gradle.plugin.dependency)
}
