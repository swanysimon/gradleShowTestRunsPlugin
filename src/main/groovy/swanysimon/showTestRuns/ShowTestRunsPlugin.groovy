package swanysimon.showTestRuns

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logger
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

class ShowTestRunsPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def configureTest = { Test task, Logger logger ->
            task.testLogging.exceptionFormat = TestExceptionFormat.FULL
            task.testLogging.events += TestLogEvent.FAILED

            LogLevel.values().each {
                task.testLogging.get(it).exceptionFormat = TestExceptionFormat.FULL
                task.testLogging.get(it).events += TestLogEvent.FAILED
            }

            task.afterSuite { TestDescriptor desc, TestResult result ->
                // only report results for topmost module or suite
                if (desc.parent) {
                    return
                }

                logger.lifecycle(
                        "Test Results: {} {} tests, {} passed, {} skipped, {} failed",
                        result.resultType,
                        result.testCount,
                        result.successfulTestCount,
                        result.skippedTestCount,
                        result.failedTestCount)
            }
        }

        project.tasks.withType(Test).configureEach { configureTest(it, project.logger) }
    }
}
