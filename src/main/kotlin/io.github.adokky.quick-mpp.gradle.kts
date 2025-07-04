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

    linuxX64()
    linuxArm64()
    mingwX64()

    if ((project.properties["kotlin.native.enableAndroidTargets"] as? String)?.toBoolean() == true) {
        androidNativeArm32()
        androidNativeArm64()
        androidNativeX64()
    }

    if ((project.properties["kotlin.native.enableAppleTargets"] as? String)?.toBoolean() != false) {
        iosArm64()
        iosSimulatorArm64()
        iosX64()
        macosArm64()
        macosX64()
        tvosArm64()
        tvosSimulatorArm64()
        tvosX64()
        watchosArm32()
        watchosArm64()
        watchosDeviceArm64()
        watchosSimulatorArm64()
        watchosX64()
    }

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

        val nonJvmMain = register("nonJvmMain") {
            dependsOn(commonMain.get())
        }
        val jsCommonMain = register("jsCommonMain") {
            dependsOn(nonJvmMain.get())
        }

        listOf("jsMain", "wasmJsMain").forEach { name ->
            named(name) {
                dependsOn(jsCommonMain.get())
            }
        }
        named("nativeMain") {
            dependsOn(nonJvmMain.get())
        }
    }
}

// no need to build and publish intermediate JsWasm sourceset
tasks.whenTaskAdded {
    if (name == "compilejsCommonMainKotlinMetadata") {
        enabled = false
    }
}

apiValidation {
    apiDumpDirectory = "abi"
}