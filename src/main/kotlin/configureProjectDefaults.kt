import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure

internal fun Project.configureProjectDefaults(javaVersion: Int) {
    configure<JavaPluginExtension> {
        configureJavaOptions(javaVersion)
    }
    configureTestsCommon(javaVersion)
    configureJvmCompilationTask(javaVersion)
}