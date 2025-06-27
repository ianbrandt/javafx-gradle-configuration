plugins {
	id("com.ianbrandt.buildlogic.javafx-project")
	id("com.ianbrandt.buildlogic.kotlin-project")
	id("com.ianbrandt.buildlogic.test.unit-test-suite")
}

dependencies {

	api(platform("com.ianbrandt.platforms:app-platform"))
}
