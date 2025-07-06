package com.ianbrandt.buildlogic.artifacts.javafx

import org.gradle.api.artifacts.ComponentMetadataContext
import org.gradle.api.artifacts.ComponentMetadataRule
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.named
import org.gradle.nativeplatform.MachineArchitecture
import org.gradle.nativeplatform.MachineArchitecture.ARM64
import org.gradle.nativeplatform.MachineArchitecture.X86
import org.gradle.nativeplatform.MachineArchitecture.X86_64
import org.gradle.nativeplatform.OperatingSystemFamily
import org.gradle.nativeplatform.OperatingSystemFamily.LINUX
import org.gradle.nativeplatform.OperatingSystemFamily.MACOS
import org.gradle.nativeplatform.OperatingSystemFamily.WINDOWS
import javax.inject.Inject

/**
 * A [ComponentMetadataRule] for adding JavaFX component metadata for
 * platform-specific variants based on the operating system and architecture of
 * the target platform. Also aligns JavaFX dependency versions by creating a
 * `org.openjfx:javafx-bom` virtual platform.
 *
 * Attribute disambiguation rules can be used to select the variant for testing
 * and running on the current platform, and artifact views can be used to
 * reselect variants for building distributions or installers for other
 * platforms.
 */
abstract class JavaFXComponentMetadataRule : ComponentMetadataRule {

	companion object {

		@Suppress("UnstableApiUsage")
		private val PLATFORM_TO_CLASSIFIER_MAP = mapOf(
			Platform(LINUX, X86_64) to "linux",
			Platform(LINUX, ARM64) to "linux-aarch64",
			Platform(MACOS, X86_64) to "mac",
			Platform(MACOS, ARM64) to "mac-aarch64",
			Platform(WINDOWS, X86) to "win-x86",
			Platform(WINDOWS, X86_64) to "win",
		)
	}

	@get:Inject
	abstract val objects: ObjectFactory

	override fun execute(context: ComponentMetadataContext) {

		context.details.apply {

			// Align JavaFX dependencies to the same version with a virtual BOM.
			belongsTo("org.openjfx:javafx-virtual-bom:${id.version}", true)

			// Add native compile and runtime variants for all supported
			// platforms.
			PLATFORM_TO_CLASSIFIER_MAP.forEach { platformToClassifierEntry ->

				val os = platformToClassifierEntry.key.os
				val arch = platformToClassifierEntry.key.arch
				val classifier = platformToClassifierEntry.value

				listOf("compile", "runtime").forEach { baseVariant ->

					// Only add the variants if the Gradle-inferred "compile"
					// and "runtime" base variants exist. Presumably, if
					// official Gradle metadata for JavaFX was ever published,
					// these base variants would not exist, and the published
					// metadata would be used instead.
					maybeAddVariant(
						"$baseVariant-$classifier",
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
							// Remove the empty non-classified JAR.
							removeAllFiles()
							// Add only the classified JAR.
							addFile("${id.name}-${id.version}-$classifier.jar")
						}
					}
				}
			}
		}
	}

	private data class Platform(
		val os: String,
		val arch: String,
	)
}
