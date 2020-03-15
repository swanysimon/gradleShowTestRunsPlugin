package swanysimon.showTestRuns

import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Subject

class ShowTestRunsFastTest extends Specification {

    @Subject
    def project = ProjectBuilder.builder().build();

    def "plugin does not register task"() {
        given:
        project.tasks.create("test", Test)
        def tasks = project.getAllTasks(true).clone()

        when:
        project.plugins.apply("com.github.show-test-runs")

        then:
        project.getAllTasks(true) == tasks
    }

    def "#fromTestLogging has one subkey per test task per log level"() {
        given:
        project.tasks.create("test1", Test)
        project.tasks.create("test2", Test)

        when:
        project.plugins.apply("com.github.show-test-runs")

        then:
        fromTestLogging(project) { "blank" }
                .values()
                *.entrySet()
                *.key
                *.toSorted()
                .every { it == ["test1", "test2"] }
    }

    def "plugin sets full exception format"() {
        given:
        project.tasks.create("test", Test)

        when:
        project.plugins.apply("com.github.show-test-runs")

        then:
        fromTestLogging(project) { it.exceptionFormat }
                .values()
                *.test
                .every { it == TestExceptionFormat.FULL }
    }

    def "plugin sets failed test event"() {
        given:
        project.tasks.create("test", Test)

        when:
        project.plugins.apply("com.github.show-test-runs")

        then:
        fromTestLogging(project) { it.events }
                .values()
                *.test
                .every { TestLogEvent.FAILED in it }
    }

    private static <T> Map<LogLevel, Map<String, T>> fromTestLogging(Project project, Closure<T> transform) {
        def evaluate = { LogLevel level ->
            project.tasks.withType(Test).collectEntries {
                [it.name, transform(it.testLogging.get(level))]
            }
        }
        return LogLevel.values().collectEntries { [it, evaluate(it)] }
    }
}
