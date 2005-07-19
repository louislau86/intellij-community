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
package com.siyeh.ig.methodmetrics;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.siyeh.ig.BaseInspectionVisitor;
import org.jetbrains.annotations.NotNull;

public class NonCommentSourceStatementsInspection extends MethodMetricInspection {
    private static final int DEFAULT_LIMIT = 30;

    public String getID(){
        return "OverlyLongMethod";
    }
    public String getDisplayName() {
        return "Overly long method ";
    }

    public String getGroupDisplayName() {
        return GroupNames.METHODMETRICS_GROUP_NAME;
    }

    protected int getDefaultLimit() {
        return DEFAULT_LIMIT;
    }

    protected String getConfigurationLabel() {
        return "Non-comment source statements limit:";
    }

    public String buildErrorString(PsiElement location) {
        final PsiMethod method = (PsiMethod) location.getParent();
        assert method != null;
        final NCSSVisitor visitor=new NCSSVisitor();
        method.accept(visitor);
        final int statementCount = visitor.getStatementCount();
        return "#ref is too long (# Non-comment source statements = " + statementCount + ") #loc";
    }

    public BaseInspectionVisitor buildVisitor() {
        return new NonCommentSourceStatementsMethodVisitor();
    }

    private class NonCommentSourceStatementsMethodVisitor extends BaseInspectionVisitor {

        public void visitMethod(@NotNull PsiMethod method) {
            // note: no call to super
            final NCSSVisitor visitor = new NCSSVisitor();
            method.accept(visitor);
            final int count = visitor.getStatementCount();

            if (count <= getLimit()) {
                return;
            }
            registerMethodError(method);
        }
    }

}
