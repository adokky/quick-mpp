import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

internal fun Project.getJavaVersion(default: Int = 17): Int =
    properties["kotlin.jvmTarget"].toString().toIntOrNull() ?: default

internal val Project.libs: VersionCatalog get() =
    project.extensions.getByType<VersionCatalogsExtension>().named("libs")