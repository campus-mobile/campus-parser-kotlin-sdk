/*
 * Copyright 2022 LLC Campus.
 */

plugins {
    alias(libs.plugins.kotlinJvm)
    `maven-publish`
}

java {
    withJavadocJar()
    withSourcesJar()
}

dependencies {
    api(projects.parserSdk)

    api(libs.kotlinTestJUnit)
    api(libs.okhttpClient)
    api(libs.okhttpMockServer)

    api(libs.coroutinesTest)
    api(libs.ktorClientMock)
}

publishing.publications {
    register<MavenPublication>("gpr") {
        from(components.getByName("java"))
    }
}
