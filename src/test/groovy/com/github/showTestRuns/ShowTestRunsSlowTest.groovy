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
        buildFile << """
            plugins {
                id "com.github.show-test-runs"
            }
            task empty {}
        """
        settingsFile = tmp.newFile("settings.gradle")
        settingsFile << "rootProject.name = 'show-test-runs-test'"
    }

    def cleanup() {
        buildFile.delete()
        settingsFile.delete()
    }

    def "plugin loads"() {
        expect:
        GradleRunner.create()
                .withPluginClasspath()
                .forwardOutput()
                .withProjectDir(tmp.root)
                .withArguments("empty")
                .build()
                .tasks
                *.path
                .contains(":empty")
    }
}
