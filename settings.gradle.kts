pluginManagement {
    repositories {
        jcenter()
        maven {
            name = "Fabric"
            url = java.net.URI("https://maven.fabricmc.net/")
        }
        gradlePluginPortal()
    }
}

rootProject.name = "bsaw"
include("bsaw-impl")
include("bsaw-api")
