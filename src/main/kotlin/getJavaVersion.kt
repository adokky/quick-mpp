import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import kotlin.jvm.optionals.getOrNull

internal fun Project.getJavaVersion(default: Int = 17): Int {
    val versionFromLibs = libs?.findVersion("java")
        ?.getOrNull()
        ?.strictVersion
        ?.takeIf { it.isNotEmpty() }
    return (versionFromLibs ?: properties["kotlin.jvmTarget"].toString())
        .toIntOrNull() ?: default
}

internal val Project.libs: VersionCatalog? get() =
    project.extensions.getByType<VersionCatalogsExtension>().find("libs").getOrNull()