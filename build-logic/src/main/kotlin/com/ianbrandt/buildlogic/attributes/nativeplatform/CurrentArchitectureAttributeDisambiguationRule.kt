package com.ianbrandt.buildlogic.attributes.nativeplatform

import org.gradle.api.attributes.AttributeDisambiguationRule
import org.gradle.api.attributes.MultipleCandidatesDetails
import org.gradle.nativeplatform.MachineArchitecture
import javax.inject.Inject

/**
 * A rule to disambiguate between multiple candidates of the
 * [MachineArchitecture] attribute in Gradle. It ensures the variant matching
 * the current architecture is chosen (if there is such a candidate).
 *
 * @param currentArchitectureAttribute the `MachineArchitecture` attribute
 * corresponding to the current architecture.
 */
open class CurrentArchitectureAttributeDisambiguationRule @Inject constructor(
	private val currentArchitectureAttribute: MachineArchitecture,
) : AttributeDisambiguationRule<MachineArchitecture> {

	override fun execute(
		details: MultipleCandidatesDetails<MachineArchitecture>
	) {
		if (details.candidateValues.contains(currentArchitectureAttribute)) {
			details.closestMatch(currentArchitectureAttribute)
		}
	}
}
