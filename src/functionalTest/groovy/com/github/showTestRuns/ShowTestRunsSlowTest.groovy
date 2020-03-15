package com.github.showTestRuns

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Shared
import spock.lang.Specification

class ShowTestRunsSlowTest extends Specification {

    @Rule TemporaryFolder tmp = new TemporaryFolder()
    @Shared def buildFile
    @Shared def settingsFile

    def setup() {
        buildFile = tmp.newFile("build.gradle")
        settingsFile = tmp.newFile("settings.gradle")
        buildFile << """
            plugins {
                id "com.github.show-test-runs"
            }
        """
        settingsFile << ""
    }

    def "test plugin loads"() {
        given:
        buildFile << """
            showTestRuns { 
                ignore = []
            }
        """

        when:
        def project = GradleRunner.create()
                .withProjectDir(tmp.root)
                .withArguments("test")
                .build()
        println(project)

        then:
        project.output.isEmpty()
    }
}
