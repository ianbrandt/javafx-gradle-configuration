import com.ianbrandt.buildlogic.conventions.javafx.JavaFXModules.getJavaFxModulePathInfo
import com.ianbrandt.buildlogic.tasks.nonmodular.NonModularJvmTasks.configureNonModularJvmTask

pluginManager.withPlugin("application") {

	tasks.named<JavaExec>("run").configure {

		val javaFxModulePathInfo =
			getJavaFxModulePathInfo(
				configurations["runtimeClasspath"],
				providers
			)

		configureNonModularJvmTask(javaFxModulePathInfo)
	}
}
