# JavaFX Gradle Configuration

An attempt to use
[Gradle's built-in Java Platform Module System support](https://docs.gradle.org/current/userguide/java_library_plugin.html#sec:java_library_modular)
along with a [`ComponentMetadataRule`](https://docs.gradle.org/current/userguide/component_metadata_rules.html)
for JavaFX dependency configuration instead of the usual
[JavaFX Gradle Plugin](https://github.com/openjfx/javafx-gradle-plugin)
approach.

## Rationale

There are several issues with the JavaFX Gradle Plugin in its current state:

* It does not currently support resolving dependencies for platforms other than
the current OS
([except via a workaround](https://github.com/openjfx/javafx-gradle-plugin#4-cross-platform-projects-and-libraries)),
which is needed to build
[distributions](https://docs.gradle.org/current/userguide/distribution_plugin.html)
for platforms other than the build OS.
* It does not support declaring the Gradle dependency configurations (e.g.
"api", "implementation", etc.) on a per-project, per-artifact basis, which is
needed to create JavaFX library modules that convey proper transitive
dependency information.
* It arguably overreaches in its manipulation of the module path and classpath
(openjfx/javafx-gradle-plugin#133).
* It doesn't currently support the Kotlin Gradle Plugin 1.7+
(openjfx/javafx-gradle-plugin#138).
* A cacheable `ComponentMetadataRule` may prove to be a more performant way to
resolve JavaFX dependencies (TBD).

## Source

See the
[javafx-project.gradle.kts](build-logic/src/main/kotlin/com.ianbrandt.build.javafx-project.gradle.kts)
precompiled script plugin for the crux of the configuration.

## OS Detection

Google's
[OS Detector Plugin for Gradle](https://github.com/google/osdetector-gradle-plugin)
is used for determining the current OS and architecture.

## Upstream Issues

Further development on this is currently being limited by upstream issues.
My primary use case for this is a mixed Kotlin and Java Project that will
require incremental migration to JPMS, and these Kotlin Gradle Plugin and
IntelliJ IDEA issues are currently blocking that:

* https://youtrack.jetbrains.com/issue/KT-20740
* https://youtrack.jetbrains.com/issue/KT-55389
* https://youtrack.jetbrains.com/issue/IDEA-220886
* https://youtrack.jetbrains.com/issue/IDEA-304601
