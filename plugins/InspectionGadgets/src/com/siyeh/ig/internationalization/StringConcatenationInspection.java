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
package com.siyeh.ig.internationalization;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import com.siyeh.ig.BaseInspectionVisitor;
import com.siyeh.ig.ExpressionInspection;
import com.siyeh.ig.psiutils.TypeUtils;
import org.jetbrains.annotations.NotNull;

public class StringConcatenationInspection extends ExpressionInspection {

    public String getDisplayName() {
        return "String concatenation";
    }

    public String getGroupDisplayName() {
        return GroupNames.INTERNATIONALIZATION_GROUP_NAME;
    }

    public String buildErrorString(PsiElement location) {
        return "String concatenation (#ref) in an internationalized context #loc";
    }

    public BaseInspectionVisitor buildVisitor() {
        return new StringConcatenationVisitor();
    }

    private static class StringConcatenationVisitor extends BaseInspectionVisitor {


        public void visitBinaryExpression(@NotNull PsiBinaryExpression expression) {
            super.visitBinaryExpression(expression);
            if(!(expression.getROperand() != null))
            {
                return;
            }
            final PsiJavaToken sign = expression.getOperationSign();
            final IElementType tokenType = sign.getTokenType();
            if (!JavaTokenType.PLUS.equals(tokenType)) {
                return;
            }
            final PsiExpression lhs = expression.getLOperand();

            final PsiType lhsType = lhs.getType();
            final PsiExpression rhs = expression.getROperand();
            if(rhs == null)
            {
                return;
            }
            final PsiType rhsType = rhs.getType();
            if(!TypeUtils.isJavaLangString(lhsType) &&
                       !TypeUtils.isJavaLangString(rhsType)){
                return;
            }
            registerError(sign);

        }

    }

}
