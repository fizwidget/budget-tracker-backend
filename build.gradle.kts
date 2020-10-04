import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.run.BootRun

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

val developmentOnly by configurations.creating

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

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    developmentOnly("org.springframework.boot:spring-boot-devtools")
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

val buildDockerImage = tasks.register<Exec>("buildDockerImage") {
    dependsOn(tasks.build)

    group = "build"
    description = "Build a docker image containing the application."

    commandLine("docker", "build", "--tag", "budget-tracker", ".")
}

tasks.register("pushDockerImage") {
    dependsOn(buildDockerImage)

    group = "deploy"
    description = "Push the docker image containing the application to the remote registry."

    doLast {
        exec {
            commandLine("docker", "tag", "budget-tracker", "fizwidget/budget-tracker")
        }
        exec {
            commandLine("docker", "push", "fizwidget/budget-tracker")
        }
    }
}

val startPostgres = tasks.register<Exec>("startPostgres") {
    group = "application"
    description = "Starts Postgres in a Docker container."

    // Starts the "postgres" service defined in docker-compose.yml
    commandLine("docker-compose", "up", "-d", "postgres")
}

tasks.register<Exec>("stopPostgres") {
    group = "application"
    description = "Stops the Postgres Docker container."

    // Stops the "postgres" container started by the "postgres" service (see docker-compose.yml)
    commandLine("docker", "stop", "postgres")
}

tasks.register<Exec>("startInDocker") {
    dependsOn(buildDockerImage)

    group = "application"
    description = "Starts the main application in Docker."

    commandLine("docker-compose", "up", "springbootapp")
}

tasks.register<Exec>("stopInDocker") {
    group = "application"
    description = "Stops the main application in Docker."

    commandLine("docker-compose", "down", "springbootapp")
}

tasks.withType<BootRun> {
    dependsOn(startPostgres)
}
