package com.graykode.foundry.run

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.util.ObjectUtils
import me.serce.solidity.lang.psi.SolContractOrLibElement

import me.serce.solidity.lang.psi.SolFunctionDefElement
import me.serce.solidity.lang.psi.Visibility

class FoundryTestRunLineMarkerProvider: RunLineMarkerContributor() {

    override fun getInfo(element: PsiElement): Info? {
        if (element is SolFunctionDefElement) {
            val methodExpr = ObjectUtils.tryCast(element, SolFunctionDefElement::class.java)
            val parentContracts = methodExpr?.contract?.getInheritanceSpecifierList()
            val parentContractNames = parentContracts?.map { it.getUserDefinedTypeName().name }
            if (parentContractNames?.contains("Test") == true) {
                if (element.name?.startsWith("test") == true && (element.visibility == Visibility.PUBLIC || element.visibility == Visibility.EXTERNAL)) {
                    return Info(AllIcons.RunConfigurations.TestState.Run, { element.name }, *ExecutorAction.getActions(0))
                }
            }
        }

        if (element is SolContractOrLibElement) {
            val contractExpr = ObjectUtils.tryCast(element, SolContractOrLibElement::class.java)
            val parentContractNames = contractExpr?.collectSupers?.map { it.name }
            if (parentContractNames?.contains("Test") == true) {
                return Info(AllIcons.RunConfigurations.TestState.Run_run, { contractExpr.name }, *ExecutorAction.getActions(0))
            }
        }

        return null
    }
}