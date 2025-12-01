package com.ianbrandt.javafx.app

import com.ianbrandt.javafx.controls.ReflectionLabel
import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.controlsfx.control.Rating

fun main(args: Array<String>) {
	Application.launch(HelloWorld::class.java, *args)
}

class HelloWorld : Application() {

	override fun start(primaryStage: Stage) {
		primaryStage.title = "Hello, World!"
		val label = ReflectionLabel("Hello!")
		val rating = Rating()
		rating.rating = 3.0
		val root = VBox(8.0)
		// Add bottom margin under the label so its reflection isn't overlapped
		VBox.setMargin(label, Insets(0.0, 0.0, 32.0, 0.0))
		root.children.addAll(label, rating)
		val scene = Scene(root, 400.0, 200.0)
		primaryStage.scene = scene
		primaryStage.show()
	}
}
