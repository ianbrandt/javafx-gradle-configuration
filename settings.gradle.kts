pluginManagement {
	@Suppress("UnstableApiUsage")
	includeBuild("build-logic")
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
