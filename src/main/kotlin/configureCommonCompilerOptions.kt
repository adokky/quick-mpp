import org.gradle.kotlin.dsl.assign
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions

internal fun KotlinCommonCompilerOptions.configureCommonDefaults() {
    extraWarnings = true
    progressiveMode = true

    freeCompilerArgs.addAll(
        "-Xexplicit-backing-fields",
        "-Xexpect-actual-classes"
    )
}