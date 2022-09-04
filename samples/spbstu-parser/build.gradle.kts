/*
 * Copyright 2022 LLC Campus.
 */

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlinSerialization)
}

dependencies {
    api(projects.parserSdk)

    implementation(libs.jsoup)

    implementation(libs.coroutines)

    implementation(libs.kotlinSerialization)

    implementation(libs.ktorClient)
    implementation(libs.ktorClientOkHttp)
    implementation(libs.ktorClientLogging)

    implementation(libs.kotlinxDateTime)
    implementation(libs.apachePoi)

    testImplementation(projects.parserTestsSdk)
    testImplementation(libs.ktorClientMock)
    testImplementation(libs.coroutinesTest)
}
