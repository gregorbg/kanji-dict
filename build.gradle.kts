plugins {
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"

    id("com.github.ben-manes.versions") version "0.39.0"
}

group = "com.suushiemaniac"
version = "0.1-BETA-FOO"

repositories {
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation("org.jetbrains.exposed:exposed-core:0.36.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.36.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.36.1")
    implementation("com.mariten:kanatools:1.3.0")
    implementation("com.atilika.kuromoji:kuromoji-ipadic:0.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    implementation("io.ktor:ktor-client-core:1.6.4")
    implementation("io.ktor:ktor-client-apache:1.6.4")
    implementation("io.ktor:ktor-client-serialization-jvm:1.6.4")
    implementation("it.skrape:skrapeit:1.1.6")
    implementation("org.jsoup:jsoup:1.14.3")
    implementation("io.github.pdvrieze.xmlutil:serialization-jvm:0.83.0")

    runtimeOnly("com.h2database:h2:1.4.200")
    runtimeOnly("org.xerial:sqlite-jdbc:3.36.0.3")
    //runtimeOnly("com.fasterxml.woodstox:woodstox-core:6.2.6")
}
