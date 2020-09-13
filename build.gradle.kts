import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlin_version = "1.4.0"

plugins {
    kotlin("jvm") version "1.4.0"
    kotlin("plugin.serialization") version "1.4.0"
    application
}
group = "me.bill"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    testImplementation(kotlin("test-junit"))
    //implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0") // JVM dependency
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC") // JVM dependency
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-properties:1.0.0-RC")

}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
application {
    mainClassName = "MainKt"
}