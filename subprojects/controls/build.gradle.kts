plugins {
	id("com.ianbrandt.buildlogic.javafx-project")
	id("com.ianbrandt.buildlogic.javafx-non-modular-test-suite")
	id("com.ianbrandt.buildlogic.kotlin-project")
	id("com.ianbrandt.buildlogic.test.ui-test-suite")
}

dependencies {

	api(libs.javafx.controls)

	implementation(libs.javafx.base)
	implementation(libs.javafx.graphics)
}
