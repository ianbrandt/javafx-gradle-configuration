plugins {
	id("jvm-test-suite")
}

@Suppress("UnstableApiUsage")
testing {
	suites {
		configureEach {
			if (this is JvmTestSuite) {
				// Version catalog type-safe accessors not available in
				// precompiled script plugins:
				// https://github.com/gradle/gradle/issues/15383
				val versionCatalog = versionCatalogs.named("libs")

				val junitVersion =
					versionCatalog.findVersion("junit").get().preferredVersion

				useJUnitJupiter(junitVersion)

				dependencies {
					implementation(
						platform("com.ianbrandt.platforms:test-platform")
					)

					implementation(
						versionCatalog.findLibrary("assertj").get()
					)
					implementation(
						versionCatalog.findLibrary("junit-api").get()
					)
				}
			}
		}
	}
}
