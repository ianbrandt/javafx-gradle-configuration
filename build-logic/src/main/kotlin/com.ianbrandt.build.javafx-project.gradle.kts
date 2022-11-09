import org.gradle.nativeplatform.MachineArchitecture.X86
import org.gradle.nativeplatform.MachineArchitecture.X86_64
import org.gradle.nativeplatform.OperatingSystemFamily.LINUX
import org.gradle.nativeplatform.OperatingSystemFamily.WINDOWS

plugins {
	`java-library`
	id("com.google.osdetector")
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
}

@CacheableRule
abstract class JavaFxRule : ComponentMetadataRule {

	// TODO: Add remaining JavaFX native variants.
	private val nativeVariants = mapOf(
		(LINUX to X86_64) to "linux",
		(WINDOWS to X86) to "win-x86",
		(WINDOWS to X86_64) to "win",
	)

	private val currentOs: String = osdetector.os
	private val currentArch: String = osdetector.arch

	private val buildNativeVariantClassifier: String =
		nativeVariants[(currentOs to currentArch)]
			?: throw GradleException(
				"No known JavaFX native runtime variant for build OS " +
						"'$currentOs' and architecture '$currentArch'"
			)

	override fun execute(context: ComponentMetadataContext) {

		// FIXME: Properly add all native variants.
		//  For now we'll just add the missing compile and runtime
		//  files for the build's native variant...
		listOf("compile", "runtime").forEach { base ->

			context.details.withVariant(base) {

				withFiles {

					with(context.details.id) {

						val nativeVariantClassifiedJar =
							"$name-$version-$buildNativeVariantClassifier.jar"

						addFile(nativeVariantClassifiedJar)
					}
				}
			}
		}

		// TODO: Also add Monocle variants.
	}
}
