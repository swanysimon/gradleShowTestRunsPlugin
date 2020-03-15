# Show Test Runs #

An opinionated [Gradle] plugin to make it slightly nicer to read test results.

The plugin adds the following functionality:

 1. Always prints out full stacktraces.
  
 1. Always report test failure events.
    
 1. Prints a summary of test results for a project. At lifecycle and above,
    shows the total number of tests run and the number of failing tests. At
    the default logging level and above.

# Installation #

Apply the plugin with the standard [Gradle plugin convention]:

```groovy
plugins {
    id "com.github.show-test-runs" version "<current version>"
}
```

# Configuration #

This plugin is not configurable.

# License #

This plugin is made available under the [MIT License].

[find operator]: http://groovy-lang.org/operators.html#_find_operator
[Gradle]: https://gradle.org/
[MIT License]: LICENSE
