/*
 * Copyright 2022 LLC Campus.
 */

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinSerialization)
}

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
