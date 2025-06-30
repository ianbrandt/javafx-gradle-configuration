package com.ianbrandt.buildlogic.attributes.nativeplatform

import org.gradle.api.attributes.AttributeDisambiguationRule
import org.gradle.api.attributes.MultipleCandidatesDetails
import org.gradle.nativeplatform.OperatingSystemFamily
import javax.inject.Inject

/**
 * A rule to disambiguate between multiple candidates of the
 * [OperatingSystemFamily] attribute in Gradle. It ensures the variant matching
 * the current operating system is chosen (if there is such a candidate).
 *
 * @param currentOsAttribute the `OperatingSystemFamily` attribute corresponding
 * to the current operating system.
 */
open class CurrentOsAttributeDisambiguationRule @Inject constructor(
	private val currentOsAttribute: OperatingSystemFamily,
) : AttributeDisambiguationRule<OperatingSystemFamily> {

	override fun execute(
		details: MultipleCandidatesDetails<OperatingSystemFamily>
	) {
		if (details.candidateValues.contains(currentOsAttribute)) {
			details.closestMatch(currentOsAttribute)
		}
	}
}
