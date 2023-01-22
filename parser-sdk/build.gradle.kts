/*
 * Copyright 2022 LLC Campus.
 */

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinSerialization)
}

group = "me.campusapp.parsers"
version = "0.1.0"

dependencies {
    implementation(libs.coroutines)

    implementation(libs.kotlinSerialization)

    implementation(libs.ktorClient)
    implementation(libs.ktorClientOkHttp)
    implementation(libs.ktorClientLogging)

    implementation(libs.kotlinxDateTime)
    implementation(libs.apachePoi)

    testImplementation(libs.kotlinTestJUnit)
}
