plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.1.0"
    application //Source: https://docs.gradle.org/current/userguide/application_plugin.html#sec:application_usage
}

javafx {
    version = "21"
    modules("javafx.controls", "javafx.fxml")
}

group = "com.team"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("ch.obermuhlner:big-math:2.3.2")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "com.team.Main"
}

