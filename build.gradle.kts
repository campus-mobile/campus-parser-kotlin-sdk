/*
 * Copyright 2022 LLC Campus.
 */

import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

subprojects {
    tasks.withType<AbstractTestTask> {
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events = setOf(
                TestLogEvent.SKIPPED,
                TestLogEvent.PASSED,
                TestLogEvent.FAILED
            )
            showStandardStreams = true
        }
        outputs.upToDateWhen { false }
    }

    plugins.withId("org.gradle.maven-publish") {
        group = "me.campusapp.parsers"
        version = "0.7.0"

        configure<JavaPluginExtension> {
            withJavadocJar()
            withSourcesJar()
        }

        configure<PublishingExtension> {
            repositories {
                maven {
                    name = "GitHubPackages"
                    url = uri("https://maven.pkg.github.com/campus-mobile/campus-parser-kotlin-sdk")
                    credentials {
                        username = project.findProperty("gpr.user")?.toString() ?: System.getenv("GPR_USERNAME")
                        password = project.findProperty("gpr.key")?.toString() ?: System.getenv("GPR_TOKEN")
                    }
                }
                maven {
                    name = "SpacePackages"
                    url = uri("https://maven.pkg.jetbrains.space/campus/p/public/maven")
                    credentials {
                        username = System.getenv("SPACE_USERNAME")
                        password = System.getenv("SPACE_PASSWORD")
                    }
                }
            }
            publications {
                register<MavenPublication>("gpr") {
                    from(components.getByName("java"))
                }
            }
        }
    }
}
