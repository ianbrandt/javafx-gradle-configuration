# JavaFX Gradle Configuration

Use a
[`ComponentMetadataRule`](https://docs.gradle.org/current/userguide/component_metadata_rules.html)
for JavaFX dependency configuration. This would be an alternative to the current
[JavaFX Gradle Plugin](https://github.com/openjfx/javafx-gradle-plugin)
implementation.

## Rationale

There are a few issues with the JavaFX Gradle Plugin in its current
([0.1.0](https://github.com/openjfx/javafx-gradle-plugin/releases/tag/0.1.0))
state:

* Its component metadata rule is not particularly straightforward to use on its
own due to coupling with other classes from the plugin (e.g. the temporal
coupling to `JavaFXPlatform.detect(osDetector)` having been called before it's
invoked).
* It doesn't support declaring dependency configurations (e.g.
`api`, `implementation`, etc.) on a per-artifact basis, which is needed for
JavaFX library modules to convey proper transitive dependency information.
* It can't be applied to projects that also apply the Application plugin (e.g.
by way of a convention plugin) unless the `run` task also has JavaFX
dependencies (it tries to set the `--module-path` to an empty string in this
case).
* It doesn't handle non-modular `JavaExec` tasks besides the `run` task from
the Application plugin (e.g. the `bootRun` task from the Spring Boot Gradle
Plugin).
* It doesn't handle non-modular `Test` tasks.

## Source

See
[javafx-project.gradle.kts](build-logic/src/main/kotlin/com.ianbrandt.buildlogic.javafx-project.gradle.kts)
for the crux of the configuration.

See
[native-dependency-convention.gradle.kts](build-logic/src/main/kotlin/com.ianbrandt.buildlogic.native-dependency-convention.gradle.kts)
for use of Google's
[OS Detector Gradle Plugin](https://github.com/google/osdetector-gradle-plugin)
and
[attribute disambiguation rules](https://docs.gradle.org/current/userguide/variant_attributes.html#sec:abm-disambiguation-rules)
to select correct the JavaFX artifacts for the current OS and architecture.
This is broken out because components with native variants besides JavaFX may
want to use the same variant disambiguation approach.

See
[JavaFXNonModularApplicationPlugin.kt](build-logic/src/main/kotlin/com/ianbrandt/buildlogic/plugins/javafx/JavaFXNonModularApplicationPlugin.kt)
for handling of non-modular
applications. This is broken out so it can be individually applied as needed.

See
[JavaFXNonModularTestSuitePlugin.kt](build-logic/src/main/kotlin/com/ianbrandt/buildlogic/plugins/javafx/JavaFXNonModularTestSuitePlugin.kt)
for handling of non-modular test suites.
This is broken out so it can be individually applied as needed.

## Upstream Issues

The need for non-modular applications and test suite handling is currently driven
by several upstream issues with Kotlin and IntelliJ support for JPMS:

* https://youtrack.jetbrains.com/issue/KT-20740/Support-Xadd-exports-Xadd-reads-Xpatch-module-similar-to-javacs-add-exports-add-reads-patch-module
* https://youtrack.jetbrains.com/issue/KT-55389/Gradle-plugin-should-expose-an-extension-method-to-automatically-enable-JPMS-for-Kotlin-compiled-output
* https://youtrack.jetbrains.com/issue/KT-57937/IC-compileKotlin-seems-to-ignore-module-info-errors-randomly
* https://youtrack.jetbrains.com/issue/KT-60582/IC-does-not-handle-changes-to-module-info.java-properly
* https://youtrack.jetbrains.com/issue/KT-60583/Kotlin-compilation-with-module-info.java-h[…]recognize-changed-module-info.class-files-in-dependencies
* https://youtrack.jetbrains.com/issue/KT-63674/JVM-IC-Treat-module-info.java-package-info.java-as-Input-properties-for-Kotlin-compilation-Gradle-part
* https://youtrack.jetbrains.com/issue/KT-63675/JVM-IC-Treat-module-info.java-package-info.java-as-Input-properties-for-Kotlin-compilation-IC-part
* https://youtrack.jetbrains.com/issue/KTIJ-27483/Run-configurations-are-not-picking-app-configuration-from-build.gradle.kts-as-described-in-docs
* https://youtrack.jetbrains.com/issue/IDEA-154038/IDEA-doesnt-respect-Gradle-compiler-settings
