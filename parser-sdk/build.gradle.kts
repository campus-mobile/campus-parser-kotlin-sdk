/*
 * Copyright 2022 LLC Campus.
 */

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.atomicfu)
    `maven-publish`
}

kotlin {
    jvm()
    js {
        nodejs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("reflect"))

                api(libs.coroutines)

                api(libs.kotlinSerialization)

                api(libs.ktorClient)
                implementation(libs.ktorClientLogging)

                api(libs.kotlinxDateTime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlinTestJUnit)
            }
        }

        val jvmMain by getting {
            dependencies {
                api(libs.ktorClientOkHttp)

                api(libs.apachePoi)

                implementation(libs.urlValidator)

                api(libs.log4jApi)
            }
        }

        val jsMain by getting {
            dependencies {
                api(libs.ktorClientJS)
            }
        }
    }
}
