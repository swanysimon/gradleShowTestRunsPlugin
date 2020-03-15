package com.github.showTestRuns

import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logger
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

class PluginApplier implements Runnable {

    private final TaskContainer tasks
    private final Logger logger
    private final ShowTestRunsExtension config

    PluginApplier(Project project, ShowTestRunsExtension config) {
        this.tasks = project.tasks
        this.logger = project.logger
        this.config = config
    }

    @Override
    void run() {
        tasks.withType(Test).configureEach {
            if (config.ignore.any { ignored -> it.name =~ ignored }) {
                logger.debug("Task {} found in {} ignore list. Skipping task", it.name, iiShowTestRunsPlugin.PLUGIN_NAME)
                return
            }

            def testLogging = it.testLogging
            testLogging.exceptionFormat = TestExceptionFormat.FULL
            testLogging.events += TestLogEvent.FAILED

            LogLevel.values().each {
                testLogging.get(it).exceptionFormat = TestExceptionFormat.FULL
                testLogging.get(it).events += TestLogEvent.FAILED
            }

            it.afterSuite { TestDescriptor desc, TestResult result ->
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

    }
}
