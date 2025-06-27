plugins {
	id("com.ianbrandt.buildlogic.kotlin-project")
	id("jvm-test-suite")
}

val versionCatalog = versionCatalogs.named("libs")

@Suppress("UnstableApiUsage")
testing {
	suites {
		configureEach {
			if (this is JvmTestSuite) {
				// Version catalog type-safe accessors not available in
				// precompiled script plugins:
				// https://github.com/gradle/gradle/issues/15383
				val junitVersion = versionCatalog.findVersion("junit")
					.get().preferredVersion

				useJUnitJupiter(junitVersion)
			}
		}
	}
}

dependencies {

	// Version catalog type-safe accessors not available in precompiled script
	// plugins: https://github.com/gradle/gradle/issues/15383

	testImplementation(platform("com.ianbrandt.platforms:test-platform"))

	testImplementation(versionCatalog.findLibrary("assertj").get())
	testImplementation(versionCatalog.findLibrary("junit-api").get())
}
