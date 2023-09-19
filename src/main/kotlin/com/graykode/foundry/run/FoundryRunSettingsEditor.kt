package com.graykode.foundry.run

import com.intellij.execution.configuration.EnvironmentVariablesComponent
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.UIUtil
import javax.swing.JPanel
import javax.swing.JTextField

class FoundryRunSettingsEditor(private val project: Project) : SettingsEditor<FoundryRunConfiguration>() {
    private val additionalArguments = JTextField()
    private val environmentVarsComponent = EnvironmentVariablesComponent()

    private val panel: JPanel by lazy {
        FormBuilder.createFormBuilder()
            .setAlignLabelOnRight(false)
            .setHorizontalGap(UIUtil.DEFAULT_HGAP)
            .setVerticalGap(UIUtil.DEFAULT_VGAP)
            .addLabeledComponent("Program arguments:", additionalArguments)
            .addComponent(environmentVarsComponent)
            .panel
    }

    override fun resetEditorFrom(configuration: FoundryRunConfiguration) {
        additionalArguments.text = configuration.additionalArguments.joinToString(" ")
        environmentVarsComponent.envData = configuration.environmentVariables
    }

    override fun applyEditorTo(configuration: FoundryRunConfiguration) {
        configuration.additionalArguments = additionalArguments.text.split(" ")
        configuration.environmentVariables = environmentVarsComponent.envData
    }

    override fun createEditor() = panel
}