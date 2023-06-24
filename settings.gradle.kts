rootProject.name = "campus-parser-kotlin-sdk"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    dependencyResolutionManagement {

    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlinx-atomicfu") {
                val version = requested.version
                useModule("org.jetbrains.kotlinx:atomicfu-gradle-plugin:$version")
            }
        }
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

include(":parser-sdk")
include(":parser-tests-sdk")
include(":samples:spbstu-parser")
