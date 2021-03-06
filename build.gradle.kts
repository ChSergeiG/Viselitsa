import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    mavenCentral()
    jcenter()
}

plugins {
    jacoco
    id("com.github.kt3k.coveralls") version "2.8.4"
    application
    kotlin("jvm") version "1.3.72"
}

group = "ru.chsergeig.bot.viselitsa"
version = "1.0.21"

jacoco {
    toolVersion = "0.8.4"
}

coveralls {
    sourceDirs = listOf("src/main/kotlin")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(group = "net.dv8tion", name = "JDA", version = "4.2.0_204")
    implementation(group = "com.jagrosh", name = "jda-utilities", version = "3.0.4")
    implementation(group = "com.mashape.unirest", name = "unirest-java", version = "1.4.9")
    implementation(group = "org.apache.httpcomponents", name = "httpclient", version = "4.1.1")
    implementation(group = "commons-lang", name = "commons-lang", version = "2.6")
    implementation(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.11.0")
    implementation(group = "com.fasterxml.jackson.dataformat", name = "jackson-dataformat-xml", version = "2.11.0")

    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "5.6.2")
    testImplementation(group = "org.mockito", name = "mockito-core", version = "2.7.22")
    testImplementation(group = "org.mockito", name = "mockito-junit-jupiter", version = "3.3.3")
    testImplementation(group = "io.github.artsok", name = "rerunner-jupiter", version = "2.1.6")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

configure<JavaApplication> {
    mainClassName = "ru.chsergeig.bot.viselitsa.MainKt"
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.jacocoTestReport {
    dependsOn("test")
    reports {
        xml.isEnabled = true
        html.isEnabled = true
    }
}

tasks.coveralls {
    dependsOn("jacocoTestReport")
}
