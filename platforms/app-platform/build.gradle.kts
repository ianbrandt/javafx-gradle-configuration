plugins {
	id("java-platform")
}

dependencies {
	constraints {
		api(libs.bundles.javafx)
		api(libs.bundles.kotlinx.coroutines.jvm)
		api(libs.jetbrains.annotations)
		api(libs.slf4j.simple)
	}
}
