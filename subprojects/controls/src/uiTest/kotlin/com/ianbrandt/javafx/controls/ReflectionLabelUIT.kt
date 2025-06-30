package com.ianbrandt.javafx.controls

import javafx.scene.Scene
import javafx.stage.Stage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxRobot
import org.testfx.assertions.api.Assertions.assertThat
import org.testfx.framework.junit5.ApplicationExtension
import org.testfx.framework.junit5.Start

@ExtendWith(ApplicationExtension::class)
internal class ReflectionLabelUIT {

	companion object {
		const val TEST_LABEL_ID = "testLabel"
		const val TEST_LABEL_TEXT = "Testing!"
	}

	@Start
	@Suppress("unused")
	fun start(stage: Stage) {
		val reflectionLabel = ReflectionLabel(TEST_LABEL_TEXT)
		reflectionLabel.id = TEST_LABEL_ID
		stage.scene = Scene(reflectionLabel, 300.0, 300.0)
		stage.show()
	}

	@Test
	fun `test ReflectionLabel`(robot: FxRobot) {
		val reflectionLabel =
			robot.lookup("#$TEST_LABEL_ID").queryAs(ReflectionLabel::class.java)

		assertThat(reflectionLabel).hasText(TEST_LABEL_TEXT)
	}
}
