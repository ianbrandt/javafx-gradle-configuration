import com.ianbrandt.buildlogic.conventions.CustomJvmTestSuiteConvention.registerCustomJvmTestSuite

val testSuiteName = "integrationTest"
val testSuiteTestSuffix = "IT"

registerCustomJvmTestSuite(testSuiteName, testSuiteTestSuffix)
