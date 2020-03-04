import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.70"
    kotlin("plugin.serialization") version "1.3.70"

    id("com.github.ben-manes.versions") version "0.27.0"
}

group = "com.suushiemaniac"
version = "0.1-BETA-FOO"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation(kotlin("stdlib-jdk8"))

    implementation("org.jetbrains.exposed:exposed-core:0.21.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.21.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.21.1")
    implementation("com.mariten:kanatools:1.3.0")
    implementation("com.atilika.kuromoji:kuromoji-ipadic:0.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")

    runtimeOnly("com.h2database:h2:1.4.200")
    runtimeOnly("org.xerial:sqlite-jdbc:3.30.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}