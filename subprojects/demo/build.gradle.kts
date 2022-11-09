plugins {
	application
	id("com.ianbrandt.build.javafx-project")
	id("com.ianbrandt.build.kotlin-project")
	id("com.ianbrandt.build.test.unit-test-suite")
}

application {
	mainModule.set("com.ianbrandt.javafx.demo")
	mainClass.set("com.ianbrandt.javafx.HelloWorld")
}

dependencies {

	api(platform("com.ianbrandt.platforms:app-platform"))

	api(libs.javafx.graphics)
}
