import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("org.jlleitschuh.gradle.ktlint") version "9.4.0"
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.spring") version "1.4.10"
}

group = "com.fizwidget"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_12

val developmentOnly: Configuration by configurations.creating

configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.graphql-java:graphql-java-spring-boot-starter-webmvc:1.0")
    implementation("com.graphql-java:graphql-java-extended-scalars:1.0")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:0.7.3")

    runtimeOnly("org.postgresql:postgresql")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "12"
        languageVersion = "1.4"
    }
}

tasks.register<Exec>("dockerRun") {
    group = "application"
    description = "Starts the main application in Docker."
    dependsOn(dockerBuild)
    commandLine("docker-compose", "up")
}

val dockerStop = tasks.register<Exec>("dockerStop") {
    group = "application"
    description = "Stops the main application in Docker."
    commandLine("docker-compose", "stop")
}

val dockerBuild = tasks.register<Exec>("dockerBuild") {
    group = "build"
    description = "Build a docker image containing the application."
    dependsOn(tasks.build)
    commandLine("docker-compose", "build")
}

tasks.register<Exec>("dockerPush") {
    group = "deploy"
    description = "Push the docker image containing the application to the remote registry."
    dependsOn(dockerBuild)
    commandLine("docker-compose", "push")
}
