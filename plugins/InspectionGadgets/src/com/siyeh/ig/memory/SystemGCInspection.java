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
package com.siyeh.ig.memory;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.psi.*;
import com.siyeh.ig.BaseInspectionVisitor;
import com.siyeh.ig.ExpressionInspection;
import org.jetbrains.annotations.NotNull;

public class SystemGCInspection extends ExpressionInspection {
    public String getID(){
        return "CallToSystemGC";
    }

    public String getDisplayName() {
        return "Calls to System.gc() or Runtime.gc()";
    }

    public String getGroupDisplayName() {
        return GroupNames.MEMORY_GROUP_NAME;
    }

    public String buildErrorString(PsiElement location) {
        return "#ref should not be called in production code #loc";
    }

    public BaseInspectionVisitor buildVisitor() {
        return new SystemGCVisitor();
    }

    private static class SystemGCVisitor extends BaseInspectionVisitor {

        public void visitMethodCallExpression(@NotNull PsiMethodCallExpression expression) {
            super.visitMethodCallExpression(expression);
            final PsiReferenceExpression methodExpression = expression.getMethodExpression();
            if (methodExpression == null) {
                return;
            }
            final String methodName = methodExpression.getReferenceName();
            if (!"gc".equals(methodName)) {
                return;
            }
            final PsiMethod method = expression.resolveMethod();
            if (method == null) {
                return;
            }
            final PsiParameterList paramList = method.getParameterList();
            if (paramList == null) {
                return;
            }
            final PsiParameter[] parameters = paramList.getParameters();
            if (parameters.length != 0) {
                return;
            }
            final PsiClass aClass = method.getContainingClass();
            if(aClass == null)
            {
                return;
            }
            final String className = aClass.getQualifiedName();
            if (!"java.lang.System".equals(className) &&
                    !"java.lang.Runtime".equals(className)) {
                return;
            }
            registerError(expression);
        }
    }

}
