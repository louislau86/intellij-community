/*
 * Copyright 2003-2005 Dave Griffith
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
package com.siyeh.ig.errorhandling;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiTryStatement;
import com.siyeh.ig.BaseInspectionVisitor;
import com.siyeh.ig.StatementInspection;
import com.siyeh.ig.StatementInspectionVisitor;
import com.siyeh.ig.psiutils.ControlFlowUtils;
import org.jetbrains.annotations.NotNull;

public class FinallyBlockCannotCompleteNormallyInspection extends StatementInspection {
    public String getID(){
        return "finally";
    }

    public String getDisplayName() {
        return "'finally' block which can not complete normally";
    }

    public String getGroupDisplayName() {
        return GroupNames.ERRORHANDLING_GROUP_NAME;
    }

    public boolean isEnabledByDefault(){
        return true;
    }
    public String buildErrorString(PsiElement location) {
        return "#ref block can not complete normally #loc";
    }

    public BaseInspectionVisitor buildVisitor() {
        return new FinallyBlockCannotCompleteNormallyVisitor();
    }

    private static class FinallyBlockCannotCompleteNormallyVisitor extends StatementInspectionVisitor {


        public void visitTryStatement(@NotNull PsiTryStatement statement) {
            super.visitTryStatement(statement);
            final PsiCodeBlock finallyBlock = statement.getFinallyBlock();
            if (finallyBlock == null) {
                return;
            }
            if(ControlFlowUtils.codeBlockMayCompleteNormally(finallyBlock)){
                return;
            }
            final PsiElement[] children = statement.getChildren();
            for(final PsiElement child : children){
                final String childText = child.getText();
                if("finally".equals(childText)){
                    registerError(child);
                    return;
                }
            }
        }
    }

}
