plugins {
	id("java-platform")
}

javaPlatform {
	allowDependencies()
}

dependencies {

	api(platform(libs.junit.bom))

	constraints {
		api(libs.assertj)
		api(libs.bundles.mockk.jvm)
	}
}
