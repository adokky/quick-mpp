import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin

plugins {
    `kotlin-dsl`
    signing
    `maven-publish`
    idea
    alias(libs.plugins.mavenPublish)
}

group = "io.github.adokky"
version = "0.22"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.bundles.core)
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get().toInt()))
    }
}

signing {
    useGpgCmd()
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    configure(
        KotlinJvm(
            javadocJar = JavadocJar.Empty(),
            sourcesJar = false,
        )
    )

    coordinates(
        groupId = group.toString(),
        artifactId = rootProject.name,
        version = version.toString()
    )

    pom {
        name = "Quick MPP"
        description = "adokky convention plugins"
        inceptionYear = "2025"
        url = "https://github.com/adokky/quick-mpp"
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
            url = "https://github.com/adokky/quick-mpp/"
            connection = "scm:git:git://github.com/adokky/quick-mpp.git"
            developerConnection = "scm:git:ssh://git@github.com/adokky/quick-mpp.git"
        }
    }
}

// Fix Gradle warning about signing tasks using publishing
// task outputs without explicit dependencies:
// https://github.com/gradle/gradle/issues/26091
tasks.withType<PublishToMavenRepository> {
    dependsOn(tasks.withType<Sign>())
}