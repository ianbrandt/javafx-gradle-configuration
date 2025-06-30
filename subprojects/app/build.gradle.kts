plugins {
	application
	id("com.ianbrandt.buildlogic.javafx-project")
	id("com.ianbrandt.buildlogic.javafx-non-modular-application-plugin")
	id("com.ianbrandt.buildlogic.kotlin-project")
	id("com.ianbrandt.buildlogic.test.unit-test-suite")
}

application {
	mainClass = "com.ianbrandt.javafx.app.HelloWorldKt"
}

dependencies {

	api(platform("com.ianbrandt.platforms:app-platform"))

	implementation(libs.javafx.base)
	implementation(libs.javafx.graphics)

	implementation(projects.subprojects.controls)
}
