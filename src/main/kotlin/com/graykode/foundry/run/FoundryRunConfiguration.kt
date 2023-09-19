package com.graykode.foundry.run

import com.intellij.execution.Executor
import com.intellij.execution.configuration.EnvironmentVariablesData
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.LocatableConfigurationBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import org.jdom.Element


class FoundryRunConfiguration(project: Project, configurationFactory: ConfigurationFactory, name: String) :
    LocatableConfigurationBase<RunProfileState>(project, configurationFactory, name) {
    var testArgumentKey: String = ""
    var testArgumentValue: String = ""
    var additionalArguments: List<String> = emptyList()
    var environmentVariables: EnvironmentVariablesData = EnvironmentVariablesData.DEFAULT

    private companion object {
        const val ADDITIONAL_ARGUMENTS = "additionalArguments"
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        return FoundryRunState(this, environment)
    }
    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> =
        FoundryRunSettingsEditor(project)

    override fun readExternal(element: Element) {
        super.readExternal(element)
        val additionalArguments = element.getAttributeValue(ADDITIONAL_ARGUMENTS)
        if (!StringUtil.isEmpty(additionalArguments)) {
            this.additionalArguments = additionalArguments.split(" ")
        }
        environmentVariables = EnvironmentVariablesData.readExternal(element)
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        if (!additionalArguments.isEmpty()) {
            element.setAttribute(ADDITIONAL_ARGUMENTS, additionalArguments.joinToString(" "))
        }
        environmentVariables.writeExternal(element)
    }
}