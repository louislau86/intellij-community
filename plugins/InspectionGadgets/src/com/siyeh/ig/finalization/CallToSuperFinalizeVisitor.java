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
package com.siyeh.ig.finalization;

import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

class CallToSuperFinalizeVisitor extends PsiRecursiveElementVisitor{
    private boolean callToSuperFinalizeFound = false;

    public void visitElement(@NotNull PsiElement element){
        if(!callToSuperFinalizeFound){
            super.visitElement(element);
        }
    }

    public void visitMethodCallExpression(@NotNull PsiMethodCallExpression expression){
        if(callToSuperFinalizeFound){
            return;
        }
        super.visitMethodCallExpression(expression);
        final PsiReferenceExpression methodExpression =
                expression.getMethodExpression();
        if(methodExpression == null){
            return;
        }
        final PsiExpression target = methodExpression.getQualifierExpression();
        if(!(target instanceof PsiSuperExpression)){
            return;
        }
        final String methodName = methodExpression.getReferenceName();
        if(!"finalize".equals(methodName)){
            return;
        }
        callToSuperFinalizeFound = true;
    }

    public boolean isCallToSuperFinalizeFound(){
        return callToSuperFinalizeFound;
    }
}
