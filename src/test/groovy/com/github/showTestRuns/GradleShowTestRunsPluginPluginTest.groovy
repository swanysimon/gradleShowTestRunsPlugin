/*
 * This Groovy source file was generated by the Gradle 'init' task.
 */
package com.github.showTestRuns

import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import spock.lang.Specification

/**
 * A simple unit test for the 'com.github.showTestRuns.greeting' plugin.
 */
public class GradleShowTestRunsPluginPluginTest extends Specification {
    def "plugin registers task"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.plugins.apply("com.github.showTestRuns.greeting")

        then:
        project.tasks.findByName("greeting") != null
    }
}
