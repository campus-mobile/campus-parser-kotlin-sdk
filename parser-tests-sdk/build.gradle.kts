/*
 * Copyright 2022 LLC Campus.
 */

plugins {
    alias(libs.plugins.kotlin)
}

group = "me.campusapp.parsers"
version = "0.1.0"

dependencies {
    implementation(libs.kotlinSerialization)

    api(libs.kotlinTestJUnit)
    api(libs.okhttpClient)
    api(libs.okhttpMockServer)
}
