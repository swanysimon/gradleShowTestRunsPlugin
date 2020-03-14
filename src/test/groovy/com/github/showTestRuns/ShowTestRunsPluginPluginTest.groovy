package com.github.showTestRuns

import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class ShowTestRunsPluginPluginTest extends Specification {

    def "plugin does not register task"() {
        given:
        def project = ProjectBuilder.builder().build()
        project.tasks.create("test", Test)
        def tasks = project.getAllTasks(true).clone()

        when:
        project.plugins.apply("com.github.show-test-runs")

        then:
        project.getAllTasks(true) == tasks
    }

    def "#fromTestLogging has one subkey per test task per log level"() {
        given:
        def project = ProjectBuilder.builder().build()
        project.tasks.create("test1", Test)
        project.tasks.create("test2", Test)

        when:
        project.plugins.apply("com.github.show-test-runs")
        def maps = fromTestLogging(project) { "blank" }

        then:
        maps.values()*.entrySet()*.key*.toSorted().every { it == ["test1", "test2"] }
    }

    def "plugin sets full exception format"() {
        given:
        def project = ProjectBuilder.builder().build()
        project.tasks.create("test", Test)

        when:
        project.plugins.apply("com.github.show-test-runs")
        def exceptionFormats = fromTestLogging(project) { it.exceptionFormat }

        then:
        exceptionFormats.values()*.test.every { it == TestExceptionFormat.FULL }
    }

    def "plugin sets failed test event"() {
        given:
        def project = ProjectBuilder.builder().build()
        project.tasks.create("test", Test)

        when:
        project.plugins.apply("com.github.show-test-runs")
        def logEvents = fromTestLogging(project) { it.events }

        then:
        logEvents.values()*.test.every { TestLogEvent.FAILED in it }
    }

    private static <T> Map<LogLevel, Map<String, T>> fromTestLogging(Project project, Closure<T> transform) {
        return LogLevel.values().collectEntries { LogLevel level -> [
                level,
                project.tasks.withType(Test).collectEntries { Test testTask ->
                    [testTask.getName(), transform.call(testTask.testLogging.get(level))]
                }
        ]}
    }
}
