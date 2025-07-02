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
			when {
				moduleFile.path.contains("javafx-base") -> "javafx.base"
				moduleFile.path.contains("javafx-controls") -> "javafx.controls"
				moduleFile.path.contains("javafx-fxml") -> "javafx.fxml"
				moduleFile.path.contains("javafx-graphics") -> "javafx.graphics"
				moduleFile.path.contains("javafx-media") -> "javafx.media"
				moduleFile.path.contains("javafx-swing") -> "javafx.swing"
				moduleFile.path.contains("javafx-web") -> "javafx.web"
				else -> {
					throw GradleException("Unknown JavaFX module: $moduleFile")
				}
			}
		}.toSet()
}
