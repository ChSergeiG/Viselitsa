import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    mavenCentral()
    jcenter()
}

plugins {
    id("io.ratpack.ratpack-java") version "1.5.4"
    application
    kotlin("jvm") version "1.3.72"
}

group = "ru.chsergeig.bot.viselitsa"
version = "1.0.10"

dependencies {
    implementation(group = "net.dv8tion", name = "JDA", version = "4.1.1_154")
    implementation(group = "com.jagrosh", name = "jda-utilities", version = "3.0.3")
    implementation(group = "com.mashape.unirest", name = "unirest-java", version = "1.4.9")
    implementation(group = "org.apache.httpcomponents", name = "httpclient", version = "4.1.1")
    implementation(group = "commons-lang", name = "commons-lang", version = "2.6")
    implementation(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.11.0")
    implementation(kotlin("stdlib-jdk8"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
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
