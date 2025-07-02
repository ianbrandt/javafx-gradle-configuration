import com.ianbrandt.buildlogic.conventions.javafx.JavaFXModules.getJavaFxModulePathInfo
import com.ianbrandt.buildlogic.tasks.nonmodular.NonModularJvmTasks.configureNonModularJvmTask

pluginManager.withPlugin("jvm-test-suite") {

	@Suppress("UnstableApiUsage")
	configure<TestingExtension> {
		suites.withType<JvmTestSuite>().configureEach {
			targets {
				all {
					testTask.configure {

						val javaFxModulePathInfo =
							getJavaFxModulePathInfo(
								configurations["runtimeClasspath"],
								providers
							)

						configureNonModularJvmTask(javaFxModulePathInfo)
					}
				}
			}
		}
	}
}
