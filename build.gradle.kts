plugins {
    id("com.diffplug.gradle.spotless") version "3.26.0"
}

group = "green.sailor.mc"
version = "1.0"


subprojects {
    repositories {
        mavenCentral()
        maven(url = "https://maven.fabricmc.net")
    }

    apply(plugin = "com.diffplug.gradle.spotless")
}
