plugins {
	`java-library`
	id("com.google.osdetector")
}

val currentOs: String = osdetector.os
val currentArch: String = osdetector.arch

dependencies {
	components {
		withModule<JavaFxRule>("org.openjfx:javafx-base") {
			params(currentOs, currentArch)
		}
		withModule<JavaFxRule>("org.openjfx:javafx-controls") {
			params(currentOs, currentArch)
		}
		withModule<JavaFxRule>("org.openjfx:javafx-fxml") {
			params(currentOs, currentArch)
		}
		withModule<JavaFxRule>("org.openjfx:javafx-graphics") {
			params(currentOs, currentArch)
		}
		withModule<JavaFxRule>("org.openjfx:javafx-swing") {
			params(currentOs, currentArch)
		}
		withModule<JavaFxRule>("org.openjfx:javafx-web") {
			params(currentOs, currentArch)
		}
	}
}

@CacheableRule
abstract class JavaFxRule @Inject constructor(
	currentOs: String,
	currentArch: String,
) : ComponentMetadataRule {

	// TODO: Add remaining JavaFX native variants.
	private val nativeVariants = mapOf(
		("linux" to "x86_64") to "linux",
		("windows" to "x86") to "win-x86",
		("windows" to "x86_64") to "win",
	)

	private val buildNativeVariantClassifier: String =
		nativeVariants[currentOs to currentArch]
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
