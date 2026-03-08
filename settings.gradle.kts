rootProject.name = "scala-std-data-hash-mhash"

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
    plugins {
        id("org.jetbrains.kotlin.jvm") version "2.3.10"
        id("org.jetbrains.kotlin.js") version "2.3.10"
        id("org.jetbrains.kotlin.multiplatform") version "2.3.10"
        id("org.jetbrains.kotlin.plugin.serialization") version "2.3.10"
        id("org.jetbrains.kotlin.plugin.compose") version "2.3.10"
        id("com.android.application") version "8.13.2"
        id("com.android.library") version "8.13.2"
        id("com.android.kotlin.multiplatform.library") version "8.13.2"
        id("org.jetbrains.compose") version "1.9.1"
        id("com.gradleup.shadow") version "8.3.0"
        id("org.jetbrains.dokka") version "2.0.0"
        id("org.jetbrains.kotlinx.kover") version "0.9.3"
        id("org.jetbrains.intellij") version "1.17.2"
        id("io.papermc.paperweight.userdev") version "1.7.2"
        id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    }
}

include(":scala-std-data-hashing")
project(":scala-std-data-hashing").projectDir = file("../scala-std-data-hashing")
include(":scala-std-algebra")
project(":scala-std-algebra").projectDir = file("../scala-std-algebra")
include(":scala-std-data-reprshow")
project(":scala-std-data-reprshow").projectDir = file("../scala-std-data-reprshow")
include(":scala-std-data-need")
project(":scala-std-data-need").projectDir = file("../scala-std-data-need")
include(":scala-std-evidence")
project(":scala-std-evidence").projectDir = file("../scala-std-evidence")
include(":scala-std-cord")
project(":scala-std-cord").projectDir = file("../scala-std-cord")
include(":scala-std-data-iso")
project(":scala-std-data-iso").projectDir = file("../scala-std-data-iso")
include(":scala-std-data-quantified")
project(":scala-std-data-quantified").projectDir = file("../scala-std-data-quantified")
