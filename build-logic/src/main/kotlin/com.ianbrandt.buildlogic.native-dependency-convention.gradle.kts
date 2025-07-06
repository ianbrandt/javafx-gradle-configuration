import com.ianbrandt.buildlogic.attributes.nativeplatform.CurrentArchitectureAttributeDisambiguationRule
import com.ianbrandt.buildlogic.attributes.nativeplatform.CurrentOsAttributeDisambiguationRule
import com.ianbrandt.buildlogic.plugins.osdetector.machineArchitecture
import com.ianbrandt.buildlogic.plugins.osdetector.operatingSystemFamily
import org.gradle.nativeplatform.MachineArchitecture.ARCHITECTURE_ATTRIBUTE
import org.gradle.nativeplatform.OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE

plugins {
	id("com.google.osdetector")
}

dependencies {
	// Add attribute disambiguation rules that select native variants for the
	// current operating system and architecture when not otherwise requested.
	// These could be superseded by artifact views of a given configuration,
	// e.g. to build distributions or installers for other platforms.
	attributesSchema {
		attribute(OPERATING_SYSTEM_ATTRIBUTE) {
			disambiguationRules.add(
				CurrentOsAttributeDisambiguationRule::class.java
			) {
				params(
					objects.named<OperatingSystemFamily>(
						osdetector.operatingSystemFamily
					)
				)
			}
		}
		attribute(ARCHITECTURE_ATTRIBUTE) {
			disambiguationRules.add(
				CurrentArchitectureAttributeDisambiguationRule::class.java
			) {
				params(
					objects.named<MachineArchitecture>(
						osdetector.machineArchitecture
					)
				)
			}
		}
	}
}
