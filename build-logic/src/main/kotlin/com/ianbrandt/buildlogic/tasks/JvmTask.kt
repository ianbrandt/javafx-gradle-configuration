package com.ianbrandt.buildlogic.tasks

import org.gradle.api.Action
import org.gradle.api.Task
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.testing.Test
import org.gradle.process.JavaForkOptions

internal interface JvmTask : JavaForkOptions {

	companion object {

		fun forTask(javaExec: JavaExec): JvmTask =
			JavaExecJvmTask(javaExec)

		fun forTask(test: Test): JvmTask =
			TestJvmTask(test)
	}

	var classpath: FileCollection

	fun doFirst(action: Action<in Task>): Task
}

@JvmInline
private value class JavaExecJvmTask(
	private val javaExec: JavaExec
) : JvmTask, JavaForkOptions by javaExec {

	override var classpath: FileCollection
		get() = javaExec.classpath
		set(value) {
			javaExec.classpath = value
		}

	override fun doFirst(action: Action<in Task>) =
		javaExec.doFirst(action)
}

@JvmInline
private value class TestJvmTask(
	private val test: Test
) : JvmTask, JavaForkOptions by test {

	override var classpath: FileCollection
		get() = test.classpath
		set(value) {
			test.classpath = value
		}

	override fun doFirst(action: Action<in Task>) =
		test.doFirst(action)
}
