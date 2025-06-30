package com.ianbrandt.javafx.app

import com.ianbrandt.javafx.controls.ReflectionLabel
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage

fun main(args: Array<String>) {
	Application.launch(HelloWorld::class.java, *args)
}

class HelloWorld : Application() {

	override fun start(primaryStage: Stage) {
		primaryStage.title = "Hello, World!"
		val label = ReflectionLabel("Hello!")
		val root = StackPane()
		root.children.add(label)
		val scene = Scene(root, 400.0, 200.0)
		primaryStage.scene = scene
		primaryStage.show()
	}
}
