import org.gradle.kotlin.dsl.assign
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions

internal fun KotlinCommonCompilerOptions.configureCommonDefaults() {
    extraWarnings.set(true) // only available in K2

    progressiveMode = true

    freeCompilerArgs.addAll(
        "-Xallow-kotlin-package",
        "-Xcontext-parameters",
        "-Xexpect-actual-classes"
    )
}