A set of convention plugins mostly used in `io.github.adokky` libraries.

## Common

* enabled all `kotlin` experimental features
* enabled extra compiler warnings
* enabled `progressiveMode`
* disabled all test reports. All exceptions are printed.
* `kotlin-test` configured by default
* JVM toolchain configured for Java 17

## `quick-mpp`

### Directory structure

```text
.
├── build.gradle.kts
├── apiDump
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

## Applied plugins

* [kover](https://github.com/Kotlin/kotlinx-kover)
* [binary-compatibility-validator](https://github.com/Kotlin/binary-compatibility-validator)

## JS

* `karma` configured for `Chrome` and `Firefox`

## `quick-jvm`

```text
.
├── build.gradle.kts
├── src
│   ├── HelloWorld.kt
└── test
    └── HelloWorldTest.kt
```