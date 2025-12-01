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

	implementation(libs.controlsfx)
	implementation(libs.javafx.base)
	implementation(libs.javafx.graphics)

	implementation(projects.subprojects.controls)
}

// A task to print the runtime classpath to verify the correct native variants
// of JavaFX are being used.
tasks.register("printRuntimeClasspath") {

	group = "debug"

	val runtimeClasspath: FileCollection = configurations.runtimeClasspath.get()

	doLast {
		println(runtimeClasspath.joinToString("\n"))
	}
}
