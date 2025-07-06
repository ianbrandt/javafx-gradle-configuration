package com.ianbrandt.buildlogic.conventions.javafx

import com.ianbrandt.buildlogic.tasks.nonmodular.ModulePathInfo
import org.gradle.api.GradleException
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.component.ModuleComponentIdentifier
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import java.io.File

/**
 * Supporting code related to JavaFX modules.
 */
internal object JavaFXModules {

	/**
	 * The group ID for published JavaFX artifacts.
	 */
	const val JAVAFX_GROUP_ID = "org.openjfx"

	private val ARTIFACT_NAME_TO_MODULE_NAME_MAP: Map<String, String> =
		mapOf(
			"javafx-base" to "javafx.base",
			"javafx-controls" to "javafx.controls",
			"javafx-fxml" to "javafx.fxml",
			"javafx-graphics" to "javafx.graphics",
			"javafx-media" to "javafx.media",
			"javafx-swing" to "javafx.swing",
			"javafx-web" to "javafx.web",
		)

	private val JAVAFX_ARTIFACT_NAME_PATTERN =
		Regex("""^(javafx-[^-]+)-.+\.jar$""")

	/**
	 * Extracts information about JavaFX modules and their corresponding file
	 * paths from the provided runtime classpath. This information includes the
	 * set of JavaFX module artifacts and their corresponding module names as a
	 * lazy computation wrapped in a provider.
	 *
	 * @param runtimeClasspath the configuration representing the runtime
	 * classpath which may contain JavaFX module dependencies
	 * @param providerFactory  the factory used to create lazy providers for
	 * computing JavaFX module names
	 * @return a [ModulePathInfo] object containing the JavaFX module artifacts
	 * as a file collection and a provider for the set of JavaFX module names
	 */
	fun getJavaFxModulePathInfo(
		runtimeClasspath: Configuration,
		providerFactory: ProviderFactory,
	): ModulePathInfo {

		// A lazy `FileCollection` view of any JavaFX dependencies found on the
		// classpath.
		val javaFxModuleArtifacts: FileCollection =
			runtimeClasspath.incoming.artifactView {
				componentFilter { componentIdentifier ->
					componentIdentifier is ModuleComponentIdentifier
						&& componentIdentifier.group in
						setOf(JAVAFX_GROUP_ID)
				}
			}.files

		// A lazy `Provider` of the module names for any JavaFX modules found
		// on the classpath.
		val javaFxModuleNames: Provider<Set<String>> =
			providerFactory.provider {
				deriveJavaFxModuleNamesFromModuleArtifacts(javaFxModuleArtifacts)
			}

		return ModulePathInfo(javaFxModuleArtifacts, javaFxModuleNames)
	}

	/**
	 * Derives the names of JavaFX modules from the provided collection of
	 * JavaFX module artifacts.
	 *
	 * @param javaFxModuleArtifacts the collection of files representing JavaFX
	 * module artifacts
	 * @return a set of strings where each string is the name of a JavaFX module
	 * derived from the artifacts
	 * @throws GradleException if an unknown JavaFX module is encountered
	 */
	private fun deriveJavaFxModuleNamesFromModuleArtifacts(
		javaFxModuleArtifacts: FileCollection
	): Set<String> =
		javaFxModuleArtifacts.map { moduleFile: File ->

			val artifactName =
				JAVAFX_ARTIFACT_NAME_PATTERN.matchEntire(moduleFile.name)
					?.groupValues?.get(1)
					?: throw GradleException(
						"Unable to extract JavaFX artifact name from " +
							"'${moduleFile.path}'"
					)

			ARTIFACT_NAME_TO_MODULE_NAME_MAP[artifactName]
				?: throw GradleException(
					"Unknown JavaFX artifact name: '$artifactName'"
				)
		}.toSet()
}
