import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
}

group = "com.suushiemaniac"
version = "0.1-BETA-FOO"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.jetbrains.exposed:exposed:0.17.7")

    runtime("com.h2database:h2:1.4.200")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}