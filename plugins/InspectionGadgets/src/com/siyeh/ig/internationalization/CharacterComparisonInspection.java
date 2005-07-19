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
import com.intellij.psi.PsiBinaryExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.siyeh.ig.BaseInspectionVisitor;
import com.siyeh.ig.ExpressionInspection;
import com.siyeh.ig.psiutils.ComparisonUtils;
import com.siyeh.ig.psiutils.TypeUtils;
import org.jetbrains.annotations.NotNull;

public class CharacterComparisonInspection extends ExpressionInspection {
    public String getID(){
        return "CharacterComparison";
    }
    public String getDisplayName() {
        return "Character comparison";
    }

    public String getGroupDisplayName() {
        return GroupNames.INTERNATIONALIZATION_GROUP_NAME;
    }

    public String buildErrorString(PsiElement location) {
        return "Character comparison #ref in an internationalized context #loc";
    }

    public BaseInspectionVisitor buildVisitor() {
        return new CharacterComparisonVisitor();
    }

    private static class CharacterComparisonVisitor extends BaseInspectionVisitor {

        public void visitBinaryExpression(@NotNull PsiBinaryExpression expression) {
            super.visitBinaryExpression(expression);
            if(!(expression.getROperand() != null)){
                return;
            }
            if(!ComparisonUtils.isComparison(expression)){
                return;
            }
            if(ComparisonUtils.isEqualityComparison(expression)){
                return;
            }
            final PsiExpression lhs = expression.getLOperand();
            if (!isCharacter(lhs)) {
                return;
            }
            final PsiExpression rhs = expression.getROperand();

            if (!isCharacter(rhs)) {
                return;
            }
            registerError(expression);

        }
    }

    private static boolean isCharacter(PsiExpression lhs) {
       return  TypeUtils.expressionHasType("char", lhs) ||
                       TypeUtils.expressionHasType("java.lang.Character", lhs);
    }

}
