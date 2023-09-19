package com.graykode.foundry.run

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import me.serce.solidity.lang.psi.impl.SolContractDefinitionImpl
import me.serce.solidity.lang.psi.impl.SolFunctionDefinitionImpl

class FoundryRunConfigProducer: LazyRunConfigurationProducer<FoundryRunConfiguration>() {
    override fun getConfigurationFactory(): ConfigurationFactory {
        return type.configurationFactory
    }

    override fun isConfigurationFromContext(
        configuration: FoundryRunConfiguration,
        context: ConfigurationContext
    ): Boolean {
        val element = context.psiLocation ?: return false

        when (element) {
            is SolContractDefinitionImpl -> {
                return configuration.testArgumentKey == "--match-contract" && configuration.testArgumentValue == "${element.name}"
            }

            is SolFunctionDefinitionImpl -> {
                return configuration.testArgumentKey == "--match-test" && configuration.testArgumentValue == "${element.name}"
            }

            else -> {
                val location = context.location ?: return false
                val virtualFile = location.virtualFile ?: return false
                return configuration.testArgumentKey == "--match-path" && configuration.testArgumentValue == virtualFile.path
            }
        }
    }

    override fun setupConfigurationFromContext(
        configuration: FoundryRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>
    ): Boolean {
        when (val element = sourceElement.get()) {
            is SolContractDefinitionImpl -> {
                configuration.name = "${element.name}"
                configuration.testArgumentKey = "--match-contract"
                configuration.testArgumentValue = "${element.name}"
                return true
            }

            is SolFunctionDefinitionImpl -> {
                configuration.name = "${element.name}"
                configuration.testArgumentKey = "--match-test"
                configuration.testArgumentValue = "${element.name}"
                return true
            }

            else -> {
                val location = context.location ?: return false
                val virtualFile = location.virtualFile ?: return false
                configuration.name = virtualFile.name
                configuration.testArgumentKey = "--match-path"
                configuration.testArgumentValue = virtualFile.path
                return true
            }
        }
    }
}