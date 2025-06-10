plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlinx.kover")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
}

repositories {
    mavenCentral()
}

private val javaVersion = getJavaVersion()

configureProjectDefaults(javaVersion)

tasks.withType<org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask> {
    args += "--ignore-scripts"
}

tasks.withType<org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest> {
    useJUnit()
}

kotlin {
    jvmToolchain(javaVersion)

    compilerOptions.configureCommonDefaults()

    jvm {
        compilerOptions.configureJvmDefaults(javaVersion)
    }

    js(IR) {
//        outputModuleName = project.path.removePrefix(":").replace(":", "-")
        browser {
            testTask {
                testLogging.showStandardStreams = true
                useKarma {
                    useChromeHeadless()
                    useFirefox()
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        named("commonMain") {
            kotlin.setSrcDirs(listOf("commonMain"))
            resources.setSrcDirs(listOf("commonMainRes"))
        }
        named("commonTest") {
            dependencies {
                implementation(kotlin("test"))
            }
            kotlin.setSrcDirs(listOf("commonTest"))
            resources.setSrcDirs(listOf("commonTestRes"))
        }

        named("jvmMain") {
            kotlin.setSrcDirs(listOf("jvmMain"))
            resources.setSrcDirs(listOf("jvmMainRes"))
        }
        named("jvmTest") {
            kotlin.setSrcDirs(listOf("jvmTest"))
            resources.setSrcDirs(listOf("jvmTestRes"))
            dependencies {
                implementation(kotlin("test"))
            }
        }

        named("jsMain") {
            kotlin.setSrcDirs(listOf("jsMain"))
            resources.setSrcDirs(listOf("jsMainRes"))
        }
        named("jsTest") {
            kotlin.setSrcDirs(listOf("jsTest"))
            resources.setSrcDirs(listOf("jsTestRes"))
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

apiValidation {
    apiDumpDirectory = "apiDump"
}