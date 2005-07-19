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
package com.siyeh.ig.verbose;

import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

 class VariableUsedInInnerClassVisitor extends PsiRecursiveElementVisitor
{
    private final PsiVariable variable;
    private boolean usedInInnerClass = false;
    private boolean inInnerClass = false;

    VariableUsedInInnerClassVisitor(PsiVariable variable)
    {
        super();
        this.variable = variable;
    }

    public void visitElement(@NotNull PsiElement element){
        if(!usedInInnerClass){
            super.visitElement(element);
        }
    }

    public void visitAnonymousClass(@NotNull PsiAnonymousClass psiAnonymousClass)
    {
        if(usedInInnerClass){
            return;
        }
        final boolean wasInInnerClass = inInnerClass;
        inInnerClass = true;
        super.visitAnonymousClass(psiAnonymousClass);
        inInnerClass = wasInInnerClass;
    }

    public void visitReferenceExpression(@NotNull PsiReferenceExpression reference)
    {
        if(usedInInnerClass){
            return;
        }
        super.visitReferenceExpression(reference);
        if(inInnerClass){
            final PsiElement element = reference.resolve();
            if(variable.equals(element)){
                usedInInnerClass = true;
            }
        }
    }

    public boolean isUsedInInnerClass()
    {
        return usedInInnerClass;
    }
}
