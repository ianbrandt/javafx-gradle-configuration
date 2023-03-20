import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
	// Version catalog not available in precompiled script plugins:
	// https://github.com/gradle/gradle/issues/15383
	// alias(libs.plugins.dependency.analysis.gradle.plugin)
	id("com.autonomousapps.dependency-analysis")
}

val javaTargetVersion = JavaVersion.VERSION_17.toString()
val kotlinTargetVersion = "1.7"

kotlin {
	jvmToolchain {
		languageVersion.set(
			JavaLanguageVersion.of(javaTargetVersion)
		)
	}
}

dependencies {

	runtimeOnly(kotlin("reflect"))
}

tasks {

	withType<JavaCompile>().configureEach {
		sourceCompatibility = javaTargetVersion
		targetCompatibility = javaTargetVersion
	}

	withType<KotlinCompile>().configureEach {
		kotlinOptions {
			languageVersion = kotlinTargetVersion
			apiVersion = kotlinTargetVersion
			jvmTarget = javaTargetVersion
			freeCompilerArgs = listOf(
				"-Xjsr305=strict",
			)

			// https://github.com/gradle/gradle/issues/17271
			val compileJava: JavaCompile by tasks
			destinationDirectory.set(compileJava.destinationDirectory)
		}
	}
}
