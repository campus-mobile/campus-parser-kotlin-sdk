/*
 * Copyright 2022 LLC Campus.
 */

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinSerialization)
    `maven-publish`
}

group = "me.campusapp.parsers"
version = "0.2.0"

dependencies {
    api(libs.coroutines)

    api(libs.kotlinSerialization)

    api(libs.ktorClient)
    implementation(libs.ktorClientOkHttp)
    implementation(libs.ktorClientLogging)

    api(libs.kotlinxDateTime)
    api(libs.apachePoi)
    api(libs.log4jApi)

    testImplementation(libs.kotlinTestJUnit)
}
