import com.ianbrandt.buildlogic.conventions.registerCustomJvmTestSuite

val testSuiteName = "integrationTest"
val testSuiteTestSuffix = "IT"

registerCustomJvmTestSuite(testSuiteName, testSuiteTestSuffix)
