plugins {
    kotlin("jvm") version("1.3.61")
    id("fabric-loom") version("0.2.6-SNAPSHOT")
}

group = "green.sailor.mc"
version = "1.0"

repositories {
    maven {
        name = "NerdHubMC"
        setUrl("https://maven.abusedmaster.xyz")
    }
}

fun DependencyHandlerScope.cca(name: String) =
    this.modImplementation(
        group = "com.github.NerdHubMC.Cardinal-Components-API",
        name = "cardinal-components-$name",
        version = "2.2.0"
    ) {
        exclude(group = "net.fabricmc")
        exclude(group = "net.fabricmc.fabric-api")
    }


dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = "1.15.2-pre2")
    mappings(
        group = "net.fabricmc", name = "yarn",
        version = "1.15.2-pre2+build.8", classifier = "v2"
    )

    // == Fabric Deps == //
    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = "0.7.3+build.176")
    modImplementation(
        group = "net.fabricmc.fabric-api",
        name = "fabric-api",
        version = "0.4.28+build.288-1.15"
    )

    // == Kotlin Deps == //
    modImplementation(
        group = "net.fabricmc",
        name = "fabric-language-kotlin",
        version = "1.3.61+build.2"
    )

    // == CCA == //
    cca("base")
    cca("entity")
    cca("world")

    // == Subproject == //
    api(project(":bsaw-api"))
    include(project(":bsaw-api"))
}

spotless {
    java {
        licenseHeaderFile("$rootDir/gradle/LICENCE-HEADER.GPL")
    }

    kotlin {
        targetExclude("build/generated/**")
        ktlint().userData(
            mapOf(
                "disabled_rules" to "no-wildcard-imports",
                "max_line_length" to "100"
            )
        )
        licenseHeaderFile("$rootDir/gradle/LICENCE-HEADER.GPL")
    }
}

tasks {
    compileKotlin { kotlinOptions.jvmTarget = "1.8" }
    compileTestKotlin { kotlinOptions.jvmTarget = "1.8" }
}
