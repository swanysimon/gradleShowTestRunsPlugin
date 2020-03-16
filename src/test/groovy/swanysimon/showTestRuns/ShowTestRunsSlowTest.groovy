package swanysimon.showTestRuns

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ShowTestRunsSlowTest extends Specification {

    static def GRADLE_VERSIONS = [
            "6.0.1",
            "6.1.1",
    ]

    @Rule TemporaryFolder tmp = new TemporaryFolder()
    @Shared File buildFile
    @Shared def settingsFile

    def setup() {
        def javaDir = tmp.newFolder("src", "test", "java", "test")
        def javaClass = new File(javaDir, "Test.java")
        javaClass << """
            package test;
            
            public interface Test {}
        """

        buildFile = tmp.newFile("build.gradle")
        buildFile << """
            plugins {
                id "java"
                id "swanysimon.show-test-runs"
            }
            
            task pluginTest(type: Test) {
                println("hello")
            }
        """

        settingsFile = tmp.newFile("settings.gradle")
        settingsFile << "rootProject.name = 'show-test-runs-test'"
    }

    def cleanup() {
        tmp.root.listFiles().each {
            if (it.isDirectory()) {
                it.deleteDir()
            } else {
                it.delete()
            }
        }
    }

    @Unroll
    def "plugin works with gradle version #gradleVersion"() {
        given:
        def runner = runner(gradleVersion).withArguments("pluginTest").build()

        expect:
        runner.task(":pluginTest").outcome == TaskOutcome.SUCCESS
        runner.output.lines().any { it =~ '^hello$' }
        runner.output.lines().any { it =~ '^Test Results: SUCCESS 0 tests, 0 passed, 0 skipped, 0 failed$' }

        where:
        gradleVersion << GRADLE_VERSIONS
    }

    private GradleRunner runner(String gradleVersion) {
        return GradleRunner.create()
                .withPluginClasspath()
                .forwardOutput()
                .withProjectDir(tmp.root)
                .withGradleVersion(gradleVersion)
    }
}
