plugins {
	id("java-platform")
}

group = "com.ianbrandt.platforms"

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
