dependencyResolutionManagement {
	versionCatalogs {
		create("libs") {
			from(files("../gradle/libs.versions.toml"))
		}
	}
}

rootProject.name = "platforms"

gradle.beforeProject {
	group = "com.ianbrandt.platforms"
	version = "1.0.0-SNAPSHOT"
}

include("app-platform")
include("test-platform")
include("plugins-platform")
