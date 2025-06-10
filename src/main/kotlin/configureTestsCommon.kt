import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

internal fun Project.configureTestsCommon(javaVersion: Int) {
    tasks.withType<Test> {
        useJUnitPlatform()

        testLogging {
            showExceptions = true
            showStackTraces = true
            showStandardStreams = true

            exceptionFormat = TestExceptionFormat.FULL

            events(TestLogEvent.FAILED, TestLogEvent.STANDARD_ERROR)

            debug {
                exceptionFormat = TestExceptionFormat.FULL
                events(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.STANDARD_ERROR)
            }

            info {
                events = debug.events
                exceptionFormat = debug.exceptionFormat
            }
        }

        reports.all { required = false }

        val javaToolchains = project.extensions.getByType<JavaToolchainService>()
        javaLauncher = javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(javaVersion))
        }
    }
}