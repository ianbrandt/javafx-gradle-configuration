plugins {
	application
	id("com.ianbrandt.buildlogic.javafx-project")
	id("com.ianbrandt.buildlogic.kotlin-project")
	id("com.ianbrandt.buildlogic.test.unit-test-suite")
}

application {
	mainModule.set("com.ianbrandt.javafx.demo")
	mainClass.set("com.ianbrandt.javafx.HelloWorld")
}

dependencies {

	api(platform("com.ianbrandt.platforms:app-platform"))

	implementation(libs.javafx.graphics)
}
