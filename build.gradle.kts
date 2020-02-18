import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.61"
    id("com.github.ben-manes.versions") version "0.27.0"
}

group = "com.suushiemaniac"
version = "0.1-BETA-FOO"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.jetbrains.exposed:exposed-core:0.21.1")
    implementation("com.mariten:kanatools:1.3.0")

    runtimeOnly("com.h2database:h2:1.4.200")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}