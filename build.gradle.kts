plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"

    id("com.github.ben-manes.versions") version "0.51.0"
}

group = "net.gregorbg"
version = "0.1-BETA-FOO"

repositories {
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation("org.jetbrains.exposed:exposed-core:0.49.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.49.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.49.0")
    implementation("com.mariten:kanatools:1.3.0")
    implementation("com.atilika.kuromoji:kuromoji-ipadic:0.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("io.ktor:ktor-client-core:2.2.1")
    implementation("io.ktor:ktor-client-java:2.2.1")
    implementation("io.ktor:ktor-client-content-negotiation:2.2.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.1")
    implementation("it.skrape:skrapeit:1.3.0-alpha.1")
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("io.github.pdvrieze.xmlutil:serialization-jvm:0.86.3")

    runtimeOnly("com.h2database:h2:2.2.224")
    runtimeOnly("org.xerial:sqlite-jdbc:3.45.3.0")
    runtimeOnly("com.fasterxml.woodstox:woodstox-core:6.6.2")
}
