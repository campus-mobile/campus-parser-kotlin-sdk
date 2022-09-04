rootProject.name = "campus-parser-kotlin-sdk"

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

include(":parser-sdk")
include(":parser-tests-sdk")
include(":samples:spbstu-parser")
