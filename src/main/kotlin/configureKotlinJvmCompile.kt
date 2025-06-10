import org.gradle.api.Project
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

internal fun Project.configureJvmCompilationTask(javaVersion: Int) {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
        jvmTargetValidationMode.set(org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode.ERROR)

        kotlinJavaToolchain.toolchain.use(
            project.extensions.getByType<JavaToolchainService>()
                .launcherFor { languageVersion.set(JavaLanguageVersion.of(javaVersion)) }
        )
    }
}