package com.graykode.foundry.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import javax.swing.Icon

val type = ConfigurationTypeUtil.findConfigurationType(FoundryConfigurationType::class.java)

class FoundryConfigurationType : ConfigurationTypeBase("FoundryConfigurationType", "Foundry", "Run Foundry Test", FoundryIcons.FOUNDRY) {
    val configurationFactory: ConfigurationFactory

    init {
        configurationFactory = object : ConfigurationFactory(this) {
            override fun getId(): String {
                return name
            }

            override fun createTemplateConfiguration(p0: Project): RunConfiguration {
                return  FoundryRunConfiguration(p0, this, "Template config")
            }

            override fun getIcon(): Icon {
                return FoundryIcons.FOUNDRY
            }

            override fun isApplicable(project: Project): Boolean {
                return true
            }
        }
        addFactory(configurationFactory)
    }
}
