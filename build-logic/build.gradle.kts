plugins {
	// Not using `id("kotlin-dsl")` per:
	// https://github.com/gradle/gradle/issues/23884
	`kotlin-dsl`
}

kotlin {
	jvmToolchain {
		languageVersion.set(
			JavaLanguageVersion.of(
				JavaVersion.VERSION_11.toString()
			)
		)
	}
}

dependencies {

	implementation(platform("com.ianbrandt.platforms:plugins-platform"))

	implementation(libs.dependency.analysis.gradle.plugin.dependency)
	implementation(libs.kotlin.gradle.plugin.dependency)
	implementation(libs.os.detector.gradle.plugin.dependency)
}
