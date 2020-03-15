package com.github.showTestRuns

import org.gradle.api.Plugin
import org.gradle.api.Project

class ShowTestRunsPlugin implements Plugin<Project> {

    public static final String EXTENSION_NAME = "showTestRuns";
    public static final String PLUGIN_NAME = "show-test-runs";

    @Override
    void apply(Project project) {
        def config = project.extensions.create(EXTENSION_NAME, ShowTestRunsExtension)
        new PluginApplier(project, config).run();
    }
}
