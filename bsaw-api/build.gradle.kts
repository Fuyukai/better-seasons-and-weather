plugins {
    id("java")
}

group = "green.sailor.mc"
version = "1.0"

dependencies {
    compileOnly(group = "org.jetbrains", name = "annotations", version = "16.0.2")
}

spotless {
    java {
        licenseHeaderFile("$rootDir/gradle/LICENCE-HEADER.LGPL")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
