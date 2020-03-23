import com.github.jengelman.gradle.plugins.shadow.transformers.AppendingTransformer
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
    application
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

application {
    mainClassName = "com.github.onlinestore.orderservice.MainKt"
}

group = "com.github.onlinestore.orderservice"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    /////////////////////
    /// Versions
    /////////////////////
    val scalaVersion = "2.13"
    val scalaTestVersion = "3.0.8"
    val akkaVersion = "2.5.27"

    /////////////////////
    /// Main dependencies
    /////////////////////
    implementation(kotlin("stdlib-jdk8"))

    //Akka actor
    implementation("com.typesafe.akka:akka-actor-typed_$scalaVersion:$akkaVersion")

    //Akka persistence
    implementation("com.typesafe.akka:akka-persistence-typed_$scalaVersion:$akkaVersion")

    //Akka cluster sharding
    implementation("com.typesafe.akka:akka-cluster-sharding-typed_$scalaVersion:$akkaVersion")

    //Akka serialization
//    implementation("com.typesafe.akka:akka-serialization-jackson_$scalaVersion:$akkaVersion")

    //LevelDB
    implementation("org.fusesource.leveldbjni:leveldbjni-all:1.8")

    //Logback
    implementation("ch.qos.logback:logback-classic:1.2.3")

    //Cassandra persistence plugin
    implementation("com.typesafe.akka:akka-persistence-cassandra_$scalaVersion:0.101")

    //Jackson
    implementation("com.fasterxml.jackson.core:jackson-databind:2.10.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.10.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.1")

    //SLF4JSimple
    implementation("org.slf4j:slf4j-simple:1.7.25")

    //Javalin
    implementation("io.javalin:javalin:3.7.0")

    //Koin
    compile("org.koin:koin-core:2.1.3")
    
    testCompile("com.typesafe.akka:akka-actor-testkit-typed_$scalaVersion:$akkaVersion")
    testCompile("junit:junit:4.12")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks {
    shadowJar {
        archiveFileName.set("order-service.jar")
        destinationDirectory.set(file("./target"))

        val newTransformer = AppendingTransformer()
        newTransformer.resource = "reference.conf"
        transformers.add(newTransformer)
    }
}
