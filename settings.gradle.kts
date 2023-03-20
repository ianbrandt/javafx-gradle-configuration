pluginManagement {
	@Suppress("UnstableApiUsage")
	includeBuild("build-logic")
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}

dependencyResolutionManagement {
	@Suppress("UnstableApiUsage")
	repositories {
		mavenCentral()
	}
}

rootProject.name = "javafx-gradle-configuration"

include("subprojects:automatic-module")
include("subprojects:demo")
include("subprojects:named-module")
include("subprojects:unnamed-module")
