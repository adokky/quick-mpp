import org.gradle.api.JavaVersion
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion

internal fun JavaPluginExtension.configureJavaOptions(javaVersion: Int) {
    val version = JavaVersion.valueOf("VERSION_$javaVersion")
    targetCompatibility = version
    sourceCompatibility = version
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}