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

	@get:Inject
	abstract val objects: ObjectFactory

	private val platformToClassifierMap = mapOf(
		Platform("linux", "x86_64") to "linux",
		Platform("linux", "aarch_64") to "linux-aarch64",
		Platform("osx", "x86_64") to "mac",
		Platform("osx", "aarch_64") to "mac-aarch64",
		Platform("windows", "x86") to "win-x86",
		Platform("windows", "x86_64") to "win",
	)

	private val buildPlatformClassifier: String =
		platformToClassifierMap[Platform(currentOs, currentArch)]
			?: throw GradleException(
				"No supported JavaFX platform known for build OS " +
						"'$currentOs' and architecture '$currentArch'"
			)

	override fun execute(context: ComponentMetadataContext) {

		val componentDetails = context.details
		val componentName = componentDetails.id.name
		val componentVersion = componentDetails.id.version

		listOf("compile", "runtime").forEach { baseVariant ->

			componentDetails.withVariant(baseVariant) {

				withFiles {

					val nativePlatformClassifiedJar =
						"$componentName-$componentVersion-$buildPlatformClassifier.jar"

					addFile(nativePlatformClassifiedJar)
				}
			}
		}

		platformToClassifierMap.forEach { platformToClassifierEntry ->

			val os = platformToClassifierEntry.key.os
			val arch = platformToClassifierEntry.key.arch
			val classifier = platformToClassifierEntry.value

			listOf("compile", "runtime").forEach { baseVariant ->

				componentDetails.addVariant(
					"$classifier-$baseVariant",
					baseVariant
				) {
					attributes {
						attributes.attribute(
							OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE,
							objects.named(os)
						)
						attributes.attribute(
							MachineArchitecture.ARCHITECTURE_ATTRIBUTE,
							objects.named(arch)
						)
					}
					withFiles {
						addFile("${componentName}-${componentVersion}.jar")
						addFile("${componentName}-${componentVersion}-$classifier.jar")
					}
				}
			}
		}

		// TODO: Also add Monocle variants?
	}

	private data class Platform(
		val os: String,
		val arch: String,
	)
}
