plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"

    id("com.github.ben-manes.versions") version "0.51.0"
    id("antlr")
}

group = "net.gregorbg"
version = "0.1-BETA-FOO"

repositories {
    mavenCentral()
}

dependencies {
    antlr("org.antlr:antlr4:4.13.1")

    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation("org.jetbrains.exposed:exposed-core:0.51.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.51.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.51.1")
    implementation("com.mariten:kanatools:1.3.0")
    implementation("com.atilika.kuromoji:kuromoji-ipadic:0.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.0")
    implementation("io.ktor:ktor-client-core:2.3.11")
    implementation("io.ktor:ktor-client-java:2.3.11")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.11")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.11")
    implementation("it.skrape:skrapeit:1.3.0-alpha.2")
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("io.github.pdvrieze.xmlutil:serialization-jvm:0.90.1")

    runtimeOnly("com.h2database:h2:2.2.224")
    runtimeOnly("org.xerial:sqlite-jdbc:3.46.0.0")
    runtimeOnly("com.fasterxml.woodstox:woodstox-core:6.6.2")
}

tasks.generateGrammarSource {
    arguments = arguments + listOf("-no-listener", "-visitor", "-package", "net.gregorbg.lang.japanese.kanji.model.kanjivg.grammar")
}

sourceSets.main {
    java {
        srcDir(tasks.generateGrammarSource)
    }
}
