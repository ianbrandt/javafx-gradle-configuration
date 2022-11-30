import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL

// Without these suppressions version catalog usage here and in other build
// files is marked red by IntelliJ:
// https://youtrack.jetbrains.com/issue/KTIJ-19369.
@Suppress(
	"DSL_SCOPE_VIOLATION",
	"MISSING_DEPENDENCY_CLASS",
	"UNRESOLVED_REFERENCE_WRONG_RECEIVER",
	"FUNCTION_CALL_EXPECTED"
)
plugins {
	alias(libs.plugins.dependency.analysis.gradle.plugin)
	// Kotlin plugin declaration needed here for the Dependency Analysis Plugin,
	// but with `apply false` since the root project itself isn't a Kotlin
	// project:
	// https://github.com/autonomousapps/dependency-analysis-android-gradle-plugin/wiki/FAQ#typenotpresentexception-type-orgjetbrainskotlingradledslkotlinprojectextension-in-kotlin-jvm-library
	alias(libs.plugins.kotlin.gradle.plugin) apply false
	alias(libs.plugins.versions.gradle.plugin)
}

allprojects {
	group = "com.ianbrandt"
	version = "1.0-SNAPSHOT"
}

tasks {

	named<Wrapper>("wrapper").configure {
		gradleVersion = "7.6"
		distributionType = ALL
	}
}
