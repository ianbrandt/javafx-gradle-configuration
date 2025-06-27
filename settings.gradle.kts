pluginManagement {
	repositories {
		mavenCentral()
		gradlePluginPortal()
	}

	includeBuild("build-logic")
}

plugins {
	id("com.gradle.develocity") version "3.18"
}

dependencyResolutionManagement {
	@Suppress("UnstableApiUsage")
	repositories {
		mavenCentral()
	}
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

develocity {
	buildScan {
		publishing.onlyIf { !System.getenv("CI").isNullOrEmpty() }
		termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
		termsOfUseAgree.set("yes")
	}
}

rootProject.name = "javafx-gradle-configuration"

gradle.beforeProject {
	// Set group and version properties for all projects
	group = "com.ianbrandt"
	version = "1.0-SNAPSHOT"
}

include("subprojects:automatic-module")
include("subprojects:demo")
include("subprojects:named-module")
include("subprojects:unnamed-module")
