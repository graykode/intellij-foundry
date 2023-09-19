package com.graykode.foundry.run

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsActions
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import org.jdom.Element

fun VirtualFile.toPsiFile(project: Project): PsiFile? =
    PsiManager.getInstance(project).findFile(this)

@NlsActions.ActionText
fun String.capitalized(): String = StringUtil.capitalize(this)

fun Element.readString(name: String): String? =
    children
        .find { it.name == "option" && it.getAttributeValue("name") == name }
        ?.getAttributeValue("value")