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
}
