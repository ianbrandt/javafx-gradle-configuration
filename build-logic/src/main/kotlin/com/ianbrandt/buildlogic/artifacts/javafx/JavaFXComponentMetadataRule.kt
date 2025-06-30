package com.ianbrandt.buildlogic.artifacts.javafx

import org.gradle.api.artifacts.ComponentMetadataContext
import org.gradle.api.artifacts.ComponentMetadataRule
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.named
import org.gradle.nativeplatform.MachineArchitecture
import org.gradle.nativeplatform.OperatingSystemFamily
import javax.inject.Inject

abstract class JavaFXComponentMetadataRule : ComponentMetadataRule {

	companion object {

		private val PLATFORM_TO_CLASSIFIER_MAP = mapOf(
			Platform("linux", "x86_64") to "linux",
			Platform("linux", "aarch_64") to "linux-aarch64",
			Platform("osx", "x86_64") to "mac",
			Platform("osx", "aarch_64") to "mac-aarch64",
			Platform("windows", "x86") to "win-x86",
			Platform("windows", "x86_64") to "win",
		)
	}

	@get:Inject
	abstract val objects: ObjectFactory

	override fun execute(context: ComponentMetadataContext) {

		val componentDetails = context.details
		val componentName = componentDetails.id.name
		val componentVersion = componentDetails.id.version

		PLATFORM_TO_CLASSIFIER_MAP.forEach { platformToClassifierEntry ->

			val os = platformToClassifierEntry.key.os
			val arch = platformToClassifierEntry.key.arch
			val classifier = platformToClassifierEntry.value

			// TODO: Also add Monocle variants?
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
						removeAllFiles()
						addFile("${componentName}-${componentVersion}-$classifier.jar")
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
