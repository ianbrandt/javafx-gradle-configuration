plugins {
	application
	id("com.ianbrandt.buildlogic.javafx-project")
	id("com.ianbrandt.buildlogic.javafx-non-modular-application")
	id("com.ianbrandt.buildlogic.kotlin-project")
}

application {
	mainClass = "com.ianbrandt.javafx.app.HelloWorldKt"
}

dependencies {

	implementation(libs.javafx.base)
	implementation(libs.javafx.graphics)

	implementation(projects.subprojects.controls)
}
