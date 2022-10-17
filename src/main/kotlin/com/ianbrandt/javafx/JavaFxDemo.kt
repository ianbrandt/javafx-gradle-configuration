package com.ianbrandt.javafx

import javafx.application.Application
import javafx.stage.Stage

class HelloWorld : Application() {

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			launch(*args)
		}
	}

	override fun start(primaryStage: Stage) {
		primaryStage.title = "Hello World!"
		primaryStage.show()
	}
}
