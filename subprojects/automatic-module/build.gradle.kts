plugins {
	id("com.ianbrandt.build.javafx-project")
	id("com.ianbrandt.build.kotlin-project")
	id("com.ianbrandt.build.test.unit-test-suite")
}

dependencies {

	api(platform("com.ianbrandt.platforms:app-platform"))

	api(libs.bundles.javafx)
}
