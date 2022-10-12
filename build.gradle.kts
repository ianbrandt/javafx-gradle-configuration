import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL
import org.gradle.nativeplatform.MachineArchitecture.X86
import org.gradle.nativeplatform.MachineArchitecture.X86_64
import org.gradle.nativeplatform.OperatingSystemFamily.LINUX
import org.gradle.nativeplatform.OperatingSystemFamily.WINDOWS
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.7.20"
}

group = "com.ianbrandt"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	components {
		withModule<JavaFxRule>("org.openjfx:javafx-base")
		withModule<JavaFxRule>("org.openjfx:javafx-controls")
		withModule<JavaFxRule>("org.openjfx:javafx-fxml")
		withModule<JavaFxRule>("org.openjfx:javafx-graphics")
		withModule<JavaFxRule>("org.openjfx:javafx-swing")
		withModule<JavaFxRule>("org.openjfx:javafx-web")
	}

	val javaFxVersion = "19"

	api("org.openjfx:javafx-base:$javaFxVersion")
	api("org.openjfx:javafx-controls:$javaFxVersion")
	api("org.openjfx:javafx-fxml:$javaFxVersion")
	api("org.openjfx:javafx-graphics:$javaFxVersion")
	api("org.openjfx:javafx-swing:$javaFxVersion")
	api("org.openjfx:javafx-web:$javaFxVersion")
}

@CacheableRule
abstract class JavaFxRule : ComponentMetadataRule {

	// TODO: Add remaining JavaFX native variants.
	private val nativeVariants = mapOf(
		(LINUX to X86_64) to "linux",
		(WINDOWS to X86) to "win-x86",
		(WINDOWS to X86_64) to "win",
	)

	// FIXME: Derive from current OS and architecture.
	private val buildNativeVariantClassifier =
		nativeVariants[(WINDOWS to X86_64)]

	override fun execute(context: ComponentMetadataContext) {
		listOf("compile", "runtime").forEach { base ->
			// FIXME: Properly add all native variants.
			//  For now we'll just add the missing compile and runtime
			//  dependencies for the build's native variant...
			context.details.withVariant(base) {
				withDependencies {
					with(context.details.id) {
						val nativeVariantDependency =
							"$group:$name:$version:$buildNativeVariantClassifier"
						println("*** Adding $nativeVariantDependency to $base")
						add(nativeVariantDependency)
					}
				}
			}
		}
	}
}

tasks {

	withType<Test>().configureEach {
		useJUnitPlatform()
	}

	withType<KotlinCompile>().configureEach {
		kotlinOptions {
			jvmTarget = JavaVersion.VERSION_17.toString()
		}
	}

	named<Wrapper>("wrapper").configure {
		gradleVersion = "7.5.1"
		distributionType = ALL
	}
}
