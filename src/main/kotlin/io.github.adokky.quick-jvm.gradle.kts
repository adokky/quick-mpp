plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

private val javaVersion = getJavaVersion()

configureProjectDefaults(javaVersion)

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
    compilerOptions {
        configureCommonDefaults()
        configureJvmDefaults(javaVersion)
    }
}

dependencies {
    api(kotlin("stdlib-common"))
    api(kotlin("stdlib-jdk7"))
    api(kotlin("stdlib-jdk8"))

    testImplementation(kotlin("test"))
}

sourceSets.main {
    kotlin.setSrcDirs(listOf("src"))
    java.setSrcDirs(listOf("src"))
    resources.setSrcDirs(listOf("res"))
}

sourceSets.test {
    kotlin.setSrcDirs(listOf("test"))
    java.setSrcDirs(listOf("test"))
    resources.setSrcDirs(listOf("test-res"))
}