import com.vanniktech.maven.publish.AndroidMultiVariantLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost

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
    when {
        project.plugins.hasPlugin("org.jetbrains.kotlin.jvm") -> configure(
            KotlinJvm(
                javadocJar = if (dokka) JavadocJar.Dokka("dokkaHtml") else JavadocJar.Javadoc(),
                sourcesJar = publishSources,
            )
        )
        project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") -> configure(
            KotlinMultiplatform(
                javadocJar = JavadocJar.Dokka(taskName = if (dokka) "dokkaHtml" else "dokkaJavadoc"),
                sourcesJar = publishSources
            )
        )
        project.plugins.hasPlugin("com.android.library") -> configure(
            AndroidMultiVariantLibrary(
                // whether to publish a sources jar
                sourcesJar = publishSources
            )
        )
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    configurePublishing(
        publishSources = (project.properties["publish.sources"] as? String)?.toBooleanStrictOrNull() ?: true,
        dokka = true
    )

    afterEvaluate {
        coordinates(
            groupId = project.group.toString(),
            artifactId = project.rootProject.name,
            version = project.version.toString()
        )
    }

    pom {
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
    }

    afterEvaluate {
        pom {
            val githuibRepoName = project.properties["github.repositoryName"] as? String ?: project.rootProject.name
            if (!url.isPresent) url = "https://github.com/adokky/$githuibRepoName"
            scm {
                if (!url.isPresent) url = "https://github.com/adokky/$githuibRepoName/"
                if (!connection.isPresent) connection = "scm:git:git://github.com/adokky/$githuibRepoName.git"
                if (!developerConnection.isPresent) developerConnection = "scm:git:ssh://git@github.com/adokky/$githuibRepoName.git"
            }
        }
    }
}

// Fix Gradle warning about signing tasks using publishing
// task outputs without explicit dependencies:
// https://github.com/gradle/gradle/issues/26091
tasks.withType<PublishToMavenRepository> {
    dependsOn(tasks.withType<Sign>())
}