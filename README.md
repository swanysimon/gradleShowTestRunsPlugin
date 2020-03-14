# Show Test Runs #

An opinionated [Gradle] plugin to make it slightly nicer to read test results.

The plugin adds the following functionality:

 1. Always prints out stacktraces and test failures. No more do you have to
    rerun a test or dig through a test results XML to find the cause of failure.
    
 2. Prints a summary of test results for a project. At lifecycle and above,
    shows the total number of tests run and the number of failing tests. At info
    level and above.

# Installation #

Apply the plugin with the standard [Gradle plugin convention]:

```groovy
plugins {
    id "com.github.show-test-runs" version "<current version>"
}
```

# Configuration #

Currently the plugin has one configuration option: the ability to exclude test
task from the plugin's logging configuration. To do so, add your test task name
to the plugin's configuration block, or a pattern as defined by Groovy's
[find operator]. For example, if your projectn has special integration tests
you'd like to exclude from this plugin:

```groovy
showTestRuns {
    ignore "integTest"
}
```

The plugin logs tasks that were excluded from the configuration at debug level.

# License #

This plugin is made available under the [MIT License].

[find operator]: http://groovy-lang.org/operators.html#_find_operator
[Gradle]: https://gradle.org/
[MIT License]: LICENSE
