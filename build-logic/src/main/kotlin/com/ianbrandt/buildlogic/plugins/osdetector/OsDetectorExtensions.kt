package com.ianbrandt.buildlogic.plugins.osdetector

import com.google.gradle.osdetector.OsDetector
import org.gradle.api.GradleException
import org.gradle.nativeplatform.MachineArchitecture
import org.gradle.nativeplatform.MachineArchitecture.ARM64
import org.gradle.nativeplatform.MachineArchitecture.X86
import org.gradle.nativeplatform.MachineArchitecture.X86_64
import org.gradle.nativeplatform.OperatingSystemFamily
import org.gradle.nativeplatform.OperatingSystemFamily.LINUX
import org.gradle.nativeplatform.OperatingSystemFamily.MACOS
import org.gradle.nativeplatform.OperatingSystemFamily.WINDOWS

/**
 * Converts the [OsDetector] detected operating system into a standard
 * [OperatingSystemFamily] attribute value string.
 *
 * @return A string representing the operating system attribute value.
 * @throws GradleException if the detected operating system is unrecognized.
 */
val OsDetector.operatingSystemFamily: String
	get() = when (os) {
		"linux" -> LINUX
		"osx" -> MACOS
		"windows" -> WINDOWS
		else ->
			throw GradleException(
				"Unknown operating system: '$os'!"
			)
	}

/**
 * Converts the [OsDetector] detected architecture into a standard
 * [MachineArchitecture] attribute value string.
 *
 * @return A string representing the architecture attribute value.
 * @throws GradleException if the detected architecture is unrecognized.
 */
@Suppress("UnstableApiUsage")
val OsDetector.machineArchitecture: String
	get() = when (arch) {
		"x86_32" -> X86
		"x86_64" -> X86_64
		"aarch_64" -> ARM64
		else ->
			throw GradleException(
				"Unknown architecture: '$arch'!"
			)
	}
