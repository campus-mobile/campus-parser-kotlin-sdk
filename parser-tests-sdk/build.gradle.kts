/*
 * Copyright 2022 LLC Campus.
 */

plugins {
    alias(libs.plugins.kotlin)
}

dependencies {
    implementation(libs.kotlinSerialization)

    api(libs.kotlinTestJUnit)
    api(libs.okhttpClient)
    api(libs.okhttpMockServer)
}
