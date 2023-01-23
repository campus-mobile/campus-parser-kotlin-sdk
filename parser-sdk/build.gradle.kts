/*
 * Copyright 2022 LLC Campus.
 */

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinSerialization)
    publishing
}

group = "me.campusapp.parsers"
version = "0.1.0"

dependencies {
    api(libs.coroutines)

    api(libs.kotlinSerialization)

    api(libs.ktorClient)
    implementation(libs.ktorClientOkHttp)
    implementation(libs.ktorClientLogging)

    api(libs.kotlinxDateTime)
    api(libs.apachePoi)

    testImplementation(libs.kotlinTestJUnit)
}
