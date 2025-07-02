package com.ianbrandt.buildlogic.tasks.nonmodular

import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Provider

/**
 * Represents information about the module path for use in configuring JVM tasks
 * in non-modular Java projects.
 *
 * @property moduleArtifacts A collection of files that should be moved from the
 * classpath to the module path.
 * @property moduleNamesProvider A provider for a set of module names that
 * should be added as module roots on the module path.
 */
data class ModulePathInfo(
	val moduleArtifacts: FileCollection,
	val moduleNamesProvider: Provider<Set<String>>
)
