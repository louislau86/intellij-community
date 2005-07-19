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
package com.siyeh.ig.bitwise;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.ConstantExpressionUtil;
import com.intellij.psi.util.PsiUtil;
import com.siyeh.ig.BaseInspectionVisitor;
import com.siyeh.ig.ExpressionInspection;
import org.jetbrains.annotations.NotNull;

public class ShiftOutOfRangeInspection extends ExpressionInspection{
    public String getDisplayName(){
        return "Shift operation by inappropriate constant";
    }

    public String getGroupDisplayName(){
        return GroupNames.BITWISE_GROUP_NAME;
    }

    public boolean isEnabledByDefault(){
        return true;
    }

    public String buildErrorString(PsiElement location){
        final PsiBinaryExpression binaryExp = (PsiBinaryExpression) location.getParent();
        assert binaryExp != null;
        final PsiExpression rhs = binaryExp.getROperand();
        final Integer value = (Integer) ConstantExpressionUtil.computeCastTo(rhs,
                                                                  PsiType.INT);
        if(value>0){
            return "Shift operation #ref by overly large constant value #loc";
        } else{
            return "Shift operation #ref by negative constant value #loc";
        }
    }

    public BaseInspectionVisitor buildVisitor(){
        return new ShiftOutOfRange();
    }

    private static class ShiftOutOfRange extends BaseInspectionVisitor{

        public void visitBinaryExpression(@NotNull PsiBinaryExpression expression){
            super.visitBinaryExpression(expression);
            if(!(expression.getROperand() != null)){
                return;
            }
            final PsiJavaToken sign = expression.getOperationSign();
            final IElementType tokenType = sign.getTokenType();
            if(!tokenType.equals(JavaTokenType.LTLT) &&
                       !tokenType.equals(JavaTokenType.GTGT) &&
                       !tokenType.equals(JavaTokenType.GTGTGT)){
                return;
            }

            final PsiType expressionType = expression.getType();
            if(expressionType == null){
                return;
            }
            final PsiExpression rhs = expression.getROperand();
            if(rhs == null)
            {
                return;
            }
            if(!PsiUtil.isConstantExpression(rhs)){
                return;
            }
            final Integer valueObject =
                    (Integer) ConstantExpressionUtil.computeCastTo(rhs,
                                                                   PsiType.INT);
            if(valueObject == null)
            {
                return;
            }
            if(expressionType.equals(PsiType.LONG)){
                if(valueObject < 0 || valueObject>63)
                {
                    registerError(sign);
                }
            }
            if(expressionType.equals(PsiType.INT)){
                if(valueObject < 0 || valueObject > 31){
                    registerError(sign);
                }
            }
        }
    }

}
