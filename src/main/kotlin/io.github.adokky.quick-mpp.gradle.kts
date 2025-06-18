import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JsModuleKind
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

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

fun KotlinJsTargetDsl.configureJsTarget() {
    // outputModuleName = project.path.removePrefix(":").replace(":", "-")
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
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        sourceMap = true
        moduleKind = JsModuleKind.MODULE_UMD
    }
}

kotlin {
    jvmToolchain(javaVersion)

    @Suppress("OPT_IN_USAGE")
    applyDefaultHierarchyTemplate()

    compilerOptions.configureCommonDefaults()

    jvm {
        compilerOptions.configureJvmDefaults(javaVersion)
    }

    js {
        configureJsTarget()
    }

    @Suppress("OPT_IN_USAGE")
    wasmJs {
        configureJsTarget()
    }

    // According to https://kotlinlang.org/docs/native-target-support.html
    // Tier 1
    macosX64()
    macosArm64()
    iosSimulatorArm64()
    iosX64()

    // Tier 2
    linuxX64()
    linuxArm64()
    watchosSimulatorArm64()
    watchosX64()
    watchosArm32()
    watchosArm64()
    tvosSimulatorArm64()
    tvosX64()
    tvosArm64()
    iosArm64()

    // Tier 3
    mingwX64()
    watchosDeviceArm64()
    androidNativeArm32()
    androidNativeArm64()
    androidNativeX86()
    androidNativeX64()

    sourceSets {
        configureEach {
            val isMain = name.endsWith("Main")
            val isTest = name.endsWith("Test")

            if (isMain || isTest) {
                kotlin.setSrcDirs(listOf(name))
                resources.setSrcDirs(listOf("${name}Res"))

                if (isTest) dependencies {
                    implementation(kotlin("test"))
                }
            }
        }

        val jsCommonMain = register("jsCommonMain") {
            dependsOn(commonMain.get())
        }
        named("jsMain") {
            dependsOn(jsCommonMain.get())
        }
        named("wasmJsMain") {
            dependsOn(jsCommonMain.get())
        }
    }
}

// This task should be disabled because of no need to build and publish intermediate JsWasm sourceset
tasks.whenTaskAdded {
    if (name == "compilejsCommonMainKotlinMetadata") {
        enabled = false
    }
}

apiValidation {
    apiDumpDirectory = "apiDump"
}