import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin

plugins {
    `kotlin-dsl`
    signing
    `maven-publish`
    id("com.vanniktech.maven.publish") version libs.versions.mavenPublish
}

group = "io.github.adokky"
version = "0.14"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

val javaVersion = libs.versions.java.orNull?.toIntOrNull() ?: 17

dependencies {
    val kotlinVersion = libs.versions.kotlin.orNull
        ?: plugins.findPlugin(KotlinBasePlugin::class)?.pluginVersion
        ?: "2.1.21"

    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation(
        "org.jetbrains.kotlin.plugin.serialization",
        "org.jetbrains.kotlin.plugin.serialization.gradle.plugin",
        kotlinVersion
    )
    implementation("org.jetbrains.kotlinx:kover-gradle-plugin:${libs.versions.kover.get()}")
    implementation(
        "org.jetbrains.kotlinx.binary-compatibility-validator",
        "org.jetbrains.kotlinx.binary-compatibility-validator.gradle.plugin",
        libs.versions.binaryCompatibilityValidator.get()
    )
    implementation(
        "com.vanniktech",
        "gradle-maven-publish-plugin",
        libs.versions.mavenPublish.get()
    )
    implementation(
        "org.jetbrains.dokka",
        "dokka-gradle-plugin",
        libs.versions.dokka.get()
    )
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

signing {
    useGpgCmd()
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

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