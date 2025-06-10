import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions

internal fun KotlinJvmCompilerOptions.configureJvmDefaults(javaVersion: Int) {
    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(javaVersion.toString()))
}