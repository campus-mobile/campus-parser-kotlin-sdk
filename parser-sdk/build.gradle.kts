/*
 * Copyright 2022 LLC Campus.
 */

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinSerialization)
    `maven-publish`
}

dependencies {
    api(libs.coroutines)

    api(libs.kotlinSerialization)

    api(libs.ktorClient)
    implementation(libs.ktorClientOkHttp)
    implementation(libs.ktorClientLogging)
    implementation(libs.urlValidator)

    api(libs.kotlinxDateTime)
    api(libs.apachePoi)
    api(libs.log4jApi)

    testImplementation(libs.kotlinTestJUnit)
}
