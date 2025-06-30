package com.ianbrandt.javafx.controls

import javafx.scene.control.Label
import javafx.scene.effect.Reflection
import javafx.scene.text.Font

class FancyLabel(text: String) : Label(text) {

	init {
		font = Font.font(32.0)

		val reflection = Reflection()
		reflection.fraction = 0.7
		effect = reflection
	}
}
