/*
 * Copyright 2014-2019 Rik van der Kleij
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package intellij.haskell.action

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.{PsiElement, PsiFile, TokenType}
import intellij.haskell.HaskellNotificationGroup
import intellij.haskell.external.component.{FileModuleIdentifiers, HaskellComponentsManager, StackProjectManager}
import intellij.haskell.psi.HaskellTypes.HS_NEWLINE
import intellij.haskell.psi._
import intellij.haskell.util.{HaskellEditorUtil, StringUtil}

class ShowTypeAction extends AnAction {

  override def update(actionEvent: AnActionEvent) {
    HaskellEditorUtil.enableAction(onlyForSourceFile = true, actionEvent)
  }

  def actionPerformed(actionEvent: AnActionEvent) {
    if (!StackProjectManager.isInitializing(actionEvent.getProject)) {
      ActionUtil.findActionContext(actionEvent).foreach(actionContext => {
        val editor = actionContext.editor
        val psiFile = actionContext.psiFile

        actionContext.selectionModel match {
          case Some(sm) => HaskellComponentsManager.findTypeInfoForSelection(psiFile, sm) match {
            case Right(info) => HaskellEditorUtil.showHint(editor, StringUtil.escapeString(info.typeSignature))
            case _ => HaskellEditorUtil.showHint(editor, "Could not determine type for selection")
          }
          case _ => ()
            Option(psiFile.findElementAt(editor.getCaretModel.getOffset)).filterNot(e => e.getNode.getElementType == HS_NEWLINE || e.getNode.getElementType == TokenType.WHITE_SPACE).orElse(Option(psiFile.findElementAt(editor.getCaretModel.getOffset - 1))).foreach { psiElement =>
              ShowTypeAction.showTypeAsHint(actionContext.project, editor, psiElement, psiFile)
            }
        }
      })
    }
    else {
      HaskellEditorUtil.showHaskellSupportIsNotAvailableWhileInitializing(actionEvent.getProject)
    }
  }
}

object ShowTypeAction {

  def showTypeAsHint(project: Project, editor: Editor, psiElement: PsiElement, psiFile: PsiFile, sticky: Boolean = false): Unit = {
    showTypeInfo(project, editor, psiElement, psiFile, sticky = sticky)
  }

  private def showTypeInfo(project: Project, editor: Editor, psiElement: PsiElement, psiFile: PsiFile, sticky: Boolean = false): Unit = {
    showTypeSignatureAsHint(project, editor, sticky, getTypeInfo(psiFile, psiElement))
  }

  private def getTypeInfo(psiFile: PsiFile, psiElement: PsiElement): String = {
    HaskellComponentsManager.findTypeInfoForElement(psiElement) match {
      case Right(info) =>
        val typeSignatureFromScope =
          if (info.withFailure) {
            findTypeSignatureFromScope(psiFile, psiElement)
          } else {
            None
          }
        typeSignatureFromScope.getOrElse(info.typeSignature)

      case Left(noInfo) =>
        findTypeSignatureFromScope(psiFile, psiElement) match {
          case Some(typeSignature) => typeSignature
          case None =>
            val message = s"Could not determine type for `${psiElement.getText}`. ${noInfo.message}"
            HaskellNotificationGroup.logInfoEvent(psiFile.getProject, message)
            message
        }
    }
  }

  private def showTypeSignatureAsHint(project: Project, editor: Editor, sticky: Boolean, typeSignature: String): Unit = {
    HaskellEditorUtil.showHint(editor, StringUtil.escapeString(typeSignature), sticky)
  }

  private def findTypeSignatureFromScope(psiFile: PsiFile, psiElement: PsiElement) = {
    if (HaskellPsiUtil.findExpression(psiElement).isDefined) {
      HaskellPsiUtil.findQualifiedName(psiElement).flatMap(qualifiedNameElement => {
        val definedInFile = HaskellComponentsManager.findDefinitionLocation(psiFile, qualifiedNameElement, None).toOption.map(_.namedElement.getContainingFile)
        if (definedInFile.contains(psiFile)) {
          // To prevent stale type info while compilation errors
          None
        } else {
          val name = qualifiedNameElement.getName
          val declaration = FileModuleIdentifiers.findAvailableModuleIdentifiers(psiFile).find(_.name == name).map(_.declaration)
          declaration.orElse(HaskellPsiUtil.findHaskellDeclarationElements(psiFile).find(_.getIdentifierElements.exists(_.getName == name)).map(_.getText.replaceAll("""\s+""", " ")))
        }
      })
    } else {
      None
    }
  }
}
