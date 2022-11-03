plugins {
	id("java-platform")
}

group = "com.ianbrandt.platforms"

dependencies {
	constraints {
		api(libs.bundles.javafx)
		api(libs.bundles.kotlinx.coroutines.jvm)
		api(libs.jetbrains.annotations)
		api(libs.slf4j.simple)
	}
}
