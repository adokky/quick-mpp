![Maven Central Version](https://img.shields.io/maven-central/v/io.github.adokky/quick-mpp)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

A set of convention plugins mostly used in `io.github.adokky` libraries.

## Common

* enabled all `kotlin` experimental features
* enabled extra compiler warnings
* enabled `progressiveMode`
* disabled all test reports. All exceptions are printed.
* `kotlin-test` configured by default
* JVM toolchain configured for Java 17
* additional source sets for convenience
  * `nonJvmMain` - base source set every other source set except `commonMain` and `jvmMain`
  * `jsCommonMain` - base source set for `jsMain` and `wasmJsMain`

## `quick-mpp`

### Apply the plugin

```kotlin
plugins {
  id("io.github.adokky.quick-mpp") version "0.18"
}
```

Applied plugins:
* [kover](https://github.com/Kotlin/kotlinx-kover)
* [binary-compatibility-validator](https://github.com/Kotlin/binary-compatibility-validator)

### Directory structure

```text
.
├── build.gradle.kts
├── abi
│   └── hello-world.api
├── commonMain
│   └── HelloWorld.kt
├── commonTest
│   └── HelloWorldTest.kt
├── jvmMain
│   └── HelloWorld.jvm.kt
├── jvmTest
│   └── HelloWorldTest.jvm.kt
├── jsMain
│   └── HelloWorld.js.kt
├── jsTest
│   └── HelloWorldTest.js.kt
...
```

## JS

* `karma` configured for `Chrome` and `Firefox`

## `quick-jvm`

### Directory structure

```text
.
├── build.gradle.kts
├── src
│   ├── HelloWorld.kt
└── test
    └── HelloWorldTest.kt
```

## `quick-publish`

Automatically applies and preconfigures `com.vanniktech.maven.publish` plugin.
The only required step before publishing is to set module `group`, `version`, `description` and `inceptionYear`:

```kotlin
plugins {
  id("io.github.adokky.quick-publish")
}

group = "com.example"
version = "0.1"

mavenPublishing {
    pom {
        description = "Fast and powerful implementation of JSON format for kotlinx.serialization"
        inceptionYear = "2025"
    }
}
```

In case of multi-module project it is better to initialize `group` in root project configuration:

```kotlin
group = "com.example"

subprojects {
    group = rootProject.group
}
```

## Multi-project builds

### Specify plugin version in root project *only*

Root `build.gradle.kts`:

```kotlin
plugins {
  id("io.github.adokky.quick-mpp") version "0.18" apply false
}
```

Any subproject `build.gradle.kts`:

```kotlin
plugins {
  id("io.github.adokky.quick-mpp")
}
```

### Override Kotlin version

Root `build.gradle.kts`:

```kotlin
plugins {
  id("multiplatform") version "2.2.20" apply false
}
```

This same method can be used to override the version of `kover` and `binary-compatibility-validator`.