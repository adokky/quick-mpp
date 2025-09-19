import com.vanniktech.maven.publish.AndroidMultiVariantLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.MavenPublishBaseExtension

plugins {
    signing
    `maven-publish`
    id("com.vanniktech.maven.publish")
}

signing {
    useGpgCmd()
}

fun MavenPublishBaseExtension.configurePublishing(
    publishSources: Boolean,
    dokka: Boolean
) {
    if (dokka) project.apply(plugin = "org.jetbrains.dokka")
    
    val isMpp = project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")
    val publishPlatformSources = !isMpp || publishSources

    when {
        isMpp -> configure(
            KotlinMultiplatform(
                javadocJar = JavadocJar.Dokka(taskName = if (dokka) "dokkaHtml" else "dokkaJavadoc"),
                sourcesJar = true
            )
        )
        project.plugins.hasPlugin("org.jetbrains.kotlin.jvm") -> configure(
            KotlinJvm(
                javadocJar = if (dokka) JavadocJar.Dokka("dokkaHtml") else JavadocJar.Javadoc(),
                sourcesJar = publishPlatformSources,
            )
        )
        project.plugins.hasPlugin("com.android.library") -> configure(
            AndroidMultiVariantLibrary(
                sourcesJar = publishPlatformSources
            )
        )
    }
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    configurePublishing(
        publishSources = (project.properties["publish.platformSources"] as? String)?.toBooleanStrictOrNull() ?: false,
        dokka = true
    )

    pom {
        name.setIfEmpty(project.name)
        val githuibRepoName = project.properties["github.repositoryName"] as? String
            ?: project.rootProject.name
        url.setIfEmpty("https://github.com/adokky/$githuibRepoName")
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "adokky"
                name = "Alexander Dokuchaev"
                url = "https://dokky.github.io"
            }
        }
        scm {
            url.setIfEmpty("https://github.com/adokky/$githuibRepoName/")
            connection.setIfEmpty("scm:git:git://github.com/adokky/$githuibRepoName.git")
            developerConnection.setIfEmpty("scm:git:ssh://git@github.com/adokky/$githuibRepoName.git")
        }
    }
}

fun Property<String>.setIfEmpty(value: String) {
    if (isPresent && get().isNotBlank()) return
    set(value)
}

// Fix Gradle warning about signing tasks using publishing
// task outputs without explicit dependencies:
// https://github.com/gradle/gradle/issues/26091
tasks.withType<PublishToMavenRepository> {
    dependsOn(tasks.withType<Sign>())
}