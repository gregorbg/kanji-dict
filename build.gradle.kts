import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.serialization") version "1.3.72"

    id("com.github.ben-manes.versions") version "0.28.0"
    id("com.github.edeandrea.xjc-generation") version "1.4"
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

    implementation("org.jetbrains.exposed:exposed-core:0.24.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.24.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.24.1")
    implementation("com.mariten:kanatools:1.3.0")
    implementation("com.atilika.kuromoji:kuromoji-ipadic:0.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
    implementation("io.ktor:ktor-client-core:1.3.2")
    implementation("io.ktor:ktor-client-apache:1.3.2")
    implementation("io.ktor:ktor-client-serialization-jvm:1.3.2")
    implementation("it.skrape:skrapeit-core:1.0.0-alpha6")
    implementation("org.jsoup:jsoup:1.13.1")
    implementation("net.devrieze:xmlutil-serialization-jvm:0.20.0.1")

    val jaxb = "javax.xml.bind:jaxb-api:2.3.1"
    implementation(jaxb)
    implementation("com.sun.xml.bind:jaxb-core:2.3.0")
    implementation("com.sun.xml.bind:jaxb-impl:2.3.1")
    "xjc"(jaxb)
    "xjc"("org.glassfish.jaxb:jaxb-runtime:2.3.3")
    "xjc"("org.glassfish.jaxb:jaxb-xjc:2.3.3")
    "xjc"("org.jvnet.jaxb2_commons:jaxb2-basics-annotate:1.1.0")

    runtimeOnly("com.h2database:h2:1.4.200")
    runtimeOnly("org.xerial:sqlite-jdbc:3.31.1")
}

sourceSets {
    main {
        java {
            srcDir("$buildDir/generated-sources/main/xjc")
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

xjcGeneration {
    schemas {
        create("wadoku") {
            schemaFile = "wadoku/entry_1.6.2.xsd"
            additionalXjcCommandLineArgs = mapOf("-Xannotate" to "")
        }
    }
}
