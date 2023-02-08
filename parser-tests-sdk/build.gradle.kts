/*
 * Copyright 2022 LLC Campus.
 */

plugins {
    alias(libs.plugins.kotlin)
    `maven-publish`
}

dependencies {
    api(projects.parserSdk)

    api(libs.kotlinTestJUnit)
    api(libs.okhttpClient)
    api(libs.okhttpMockServer)

    api(libs.coroutinesTest)
    api(libs.ktorClientMock)
}
