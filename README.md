# JavaFX Gradle Configuration

A slightly different approach to that of the current
[0.1.0](https://github.com/openjfx/javafx-gradle-plugin/releases/tag/0.1.0)
version of the
[JavaFX Gradle Plugin](https://github.com/openjfx/javafx-gradle-plugin)
for handling JavaFX dependencies in a Gradle project.

## Rationale

There are a few issues with the JavaFX Gradle Plugin in its current state:

* Its approach to
  [declaring JavaFX modules](https://github.com/openjfx/javafx-gradle-plugin/tree/0.1.0?tab=readme-ov-file#2-specify-javafx-modules)
  at the project level, and then applying those to
  [all configured dependency scopes](https://github.com/openjfx/javafx-gradle-plugin/tree/0.1.0?tab=readme-ov-file#5-dependency-scope),
  doesn't support declaring each JavaFX dependency in the specific scope where
  it's actually used (e.g. `api`, `implementation`, `testImplementation`, etc.).
  This can result in Gradle having inaccurate information about dependency
  usage, which may impact
  [incremental compilation](https://docs.gradle.org/8.14.3/userguide/java_plugin.html#sec:incremental_compile).
  If tools to detect such issues are used (e.g. the
  [Dependency Analysis Gradle Plugin](https://github.com/autonomousapps/dependency-analysis-gradle-plugin)),
  exception configuration may be required.
    * One could work around this by not applying the JavaFX Gradle Plugin.
      Instead it could be added as a build script dependency, and its
      [
      `JavaFXComponenentMetadataRule`](https://github.com/openjfx/javafx-gradle-plugin/blob/0.1.0/src/main/java/org/openjfx/gradle/metadatarule/JavaFXComponentMetadataRule.java)
      could be used in isolation. That said, the rule does have a transitive
      dependency on the
      [OS Detector Gradle Plugin](https://github.com/google/osdetector-gradle-plugin),
      which one may not want.
* It can't be applied to projects that also apply the
  [Application Plugin](https://docs.gradle.org/8.14.3/userguide/application_plugin.html)
  (e.g. by way of a convention plugin) unless the `run` task also has JavaFX
  dependencies (it tries to set the `--module-path` to an empty string in this
  case).
* It doesn't handle non-modular `JavaExec` tasks besides the `run` task from
  the Application Plugin (e.g., the
  [Spring Boot Gradle Plugin's
  `bootRun` task](https://docs.spring.io/spring-boot/gradle-plugin/running.html)).
* It doesn't handle non-modular `Test` tasks.

## Approach

A
[
`ComponentMetadataRule`](https://docs.gradle.org/8.14.3/userguide/component_metadata_rules.html)
is still used (also named
[
`JavaFXComponentMetadataRule`](build-logic/src/main/kotlin/com/ianbrandt/buildlogic/artifacts/javafx/JavaFXComponentMetadataRule.kt)),
but it has no coupling to the OS Detector Gradle Plugin. See
[javafx-project.gradle.kts](build-logic/src/main/kotlin/com.ianbrandt.buildlogic.javafx-project.gradle.kts)
for an example of use.

Rather than
[setting attributes on all compile and runtime configurations](https://github.com/openjfx/javafx-gradle-plugin/blob/0.1.0/src/main/java/org/openjfx/gradle/JavaFXOptions.java#L101),
[attribute disambiguation rules](https://docs.gradle.org/8.14.3/userguide/variant_attributes.html#sec:abm-disambiguation-rules)
are registered to ensure the native variant for the current platform is
selected. This is broken out to
[native-dependency-convention.gradle.kts](build-logic/src/main/kotlin/com.ianbrandt.buildlogic.native-dependency-convention.gradle.kts),
as it may be generally applicable to projects that have native dependencies
besides JavaFX. The convention does still use the
[OS Detector Gradle Plugin](https://github.com/google/osdetector-gradle-plugin)
for detecting the current OS and architecture, but there is no explicit
coupling between it and
[
`CurrentOsAttributeDisambiguationRule`](build-logic/src/main/kotlin/com/ianbrandt/buildlogic/attributes/nativeplatform/CurrentOsAttributeDisambiguationRule.kt)
or
[
`CurrentArchitectureAttributeDisambiguationRule`](build-logic/src/main/kotlin/com/ianbrandt/buildlogic/attributes/nativeplatform/CurrentArchitectureAttributeDisambiguationRule.kt).
[
`OsDetector` extension functions](build-logic/src/main/kotlin/com/ianbrandt/buildlogic/plugins/osdetector/OsDetectorExtensions.kt)
are used to translate its detected operating system and architecture values to
corresponding values for Gradle's `OperatingSystemFamily` and
`MachineArchitecture` attributes. Although not currently demonstrated,
[artifact views](https://docs.gradle.org/8.14.3/userguide/artifact_views.html)
could be used to select native variants for platforms other than the build
platform, for example to build distributions or installers for other operating
systems and architectures.

Handling of JVM tasks (e.g. `JavaExec` and `Test`) in non-modular projects is
generalized as
[configuration convention functions](build-logic/src/main/kotlin/com/ianbrandt/buildlogic/tasks/nonmodular/NonModularJvmTasks.kt).
See
[javafx-non-modular-application.gradle.kts](build-logic/src/main/kotlin/com.ianbrandt.buildlogic.javafx-non-modular-application.gradle.kts)
and
[javafx-non-modular-test-suite.gradle.kts](build-logic/src/main/kotlin/com.ianbrandt.buildlogic.javafx-non-modular-test-suite.gradle.kts)
for usage examples. These are broken out so they can be individually applied as
needed. Rather than [filtering the
`classpath`](https://github.com/openjfx/javafx-gradle-plugin/blob/0.1.0/src/main/java/org/openjfx/gradle/JavaFXPlugin.java#L113-L114),
an
[artifact view](https://docs.gradle.org/8.14.3/userguide/artifact_views.html)
is used (see
[
`JavaFXModules.getJavaFxModulePathInfo(...)`](build-logic/src/main/kotlin/com/ianbrandt/buildlogic/conventions/javafx/JavaFXModules.kt)).

## Upstream Issues

The need for non-modular application and test suite handling is currently driven
in part by several upstream issues with Kotlin and IntelliJ support for JPMS:

* https://youtrack.jetbrains.com/issue/KT-20740/Support-Xadd-exports-Xadd-reads-Xpatch-module-similar-to-javacs-add-exports-add-reads-patch-module
* https://youtrack.jetbrains.com/issue/KT-55389/Gradle-plugin-should-expose-an-extension-method-to-automatically-enable-JPMS-for-Kotlin-compiled-output
* https://youtrack.jetbrains.com/issue/KT-57937/IC-compileKotlin-seems-to-ignore-module-info-errors-randomly
* https://youtrack.jetbrains.com/issue/KT-60582/IC-does-not-handle-changes-to-module-info.java-properly
* https://youtrack.jetbrains.com/issue/KT-60583/Kotlin-compilation-with-module-info.java-h[…]recognize-changed-module-info.class-files-in-dependencies
* https://youtrack.jetbrains.com/issue/KT-63674/JVM-IC-Treat-module-info.java-package-info.java-as-Input-properties-for-Kotlin-compilation-Gradle-part
* https://youtrack.jetbrains.com/issue/KT-63675/JVM-IC-Treat-module-info.java-package-info.java-as-Input-properties-for-Kotlin-compilation-IC-part
* https://youtrack.jetbrains.com/issue/KTIJ-27483/Run-configurations-are-not-picking-app-configuration-from-build.gradle.kts-as-described-in-docs
* https://youtrack.jetbrains.com/issue/IDEA-154038/IDEA-doesnt-respect-Gradle-compiler-settings
