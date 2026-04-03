plugins {
    kotlin("jvm") version "2.3.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.github.IgloLang:StoryScript:622a4e0c2a")
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}