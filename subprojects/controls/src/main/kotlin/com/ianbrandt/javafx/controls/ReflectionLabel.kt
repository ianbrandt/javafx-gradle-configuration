package com.ianbrandt.javafx.controls

import javafx.scene.control.Label
import javafx.scene.effect.Reflection
import javafx.scene.text.Font

/**
 * A custom JavaFX control that uses 'javafx.controls' in its public ABI
 * (i.e. as an `api` dependency in Gradle), and 'javafx.graphics' only in its
 * implementation (i.e. as an `implementation` dependency in Gradle).
 */
class ReflectionLabel(text: String) : Label(text) {

	init {
		val reflection = Reflection()
		reflection.fraction = 0.7
		effect = reflection
		font = Font.font(32.0)
	}
}
