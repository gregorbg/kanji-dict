plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"

    id("com.github.ben-manes.versions") version "0.44.0"
}

group = "net.gregorbg"
version = "0.1-BETA-FOO"

repositories {
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation("com.mariten:kanatools:1.3.0")
    implementation("com.atilika.kuromoji:kuromoji-ipadic:0.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("io.ktor:ktor-client-core:2.2.1")
    implementation("io.ktor:ktor-client-java:2.2.1")
    implementation("io.ktor:ktor-client-content-negotiation:2.2.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.1")
    implementation("it.skrape:skrapeit:1.3.0-alpha.1")
    implementation("org.jsoup:jsoup:1.15.3")
    implementation("io.github.pdvrieze.xmlutil:serialization-jvm:0.84.3")

    runtimeOnly("com.h2database:h2:2.1.214")
    runtimeOnly("org.xerial:sqlite-jdbc:3.40.0.0")
    //runtimeOnly("com.fasterxml.woodstox:woodstox-core:6.2.6")
}
