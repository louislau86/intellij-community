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
package com.siyeh.ig.performance;

import com.intellij.psi.*;
import com.intellij.psi.util.InheritanceUtil;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

class MethodReferenceVisitor extends PsiRecursiveElementVisitor{
    private boolean m_referencesStaticallyAccessible = true;
    private PsiMethod m_method;

    MethodReferenceVisitor(PsiMethod method){
        super();
        m_method = method;
    }

    public boolean areReferencesStaticallyAccessible(){
        return m_referencesStaticallyAccessible;
    }

    public void visitReferenceElement(PsiJavaCodeReferenceElement reference){
        if(!m_referencesStaticallyAccessible){
            return;
        }
        super.visitReferenceElement(reference);
        final PsiElement parent =
                PsiTreeUtil.getParentOfType(reference, PsiNewExpression.class);
        if(parent == null){
            return;
        }
        final PsiElement resolvedElement = reference.resolve();
        if(!(resolvedElement instanceof PsiClass)){
            return;
        }
        final PsiClass aClass = (PsiClass) resolvedElement;
        final PsiElement scope = aClass.getScope();
        if(!(scope instanceof PsiClass)){
            return;
        }
        m_referencesStaticallyAccessible &= aClass.hasModifierProperty(PsiModifier.STATIC);
    }

    public void visitReferenceExpression(@NotNull PsiReferenceExpression expression){
        if(!m_referencesStaticallyAccessible){
            return;
        }
        super.visitReferenceExpression(expression);

        final PsiElement element = expression.resolve();
        if(element instanceof PsiField){
            m_referencesStaticallyAccessible &= isFieldStaticallyAccessible((PsiField) element);
        } else if(element instanceof PsiMethod){
            m_referencesStaticallyAccessible &= isMethodStaticallyAccessible((PsiMethod) element);
        }
    }

    public void visitThisExpression(@NotNull PsiThisExpression expression){
        if(!m_referencesStaticallyAccessible){
            return;
        }
        super.visitThisExpression(expression);
        m_referencesStaticallyAccessible = false;
    }

    private boolean isMethodStaticallyAccessible(PsiMethod method){
        if(m_method.equals(method)){
            return true;
        }
        if(method.hasModifierProperty(PsiModifier.STATIC)){
            return true;
        }
        if(method.isConstructor()){
            return true;
        }
        final PsiClass referenceContainingClass = m_method.getContainingClass();
        final PsiClass methodContainingClass = method.getContainingClass();
        return !InheritanceUtil.isInheritorOrSelf(referenceContainingClass,
                                                  methodContainingClass, true);
    }

    private boolean isFieldStaticallyAccessible(PsiField field){
        if(field.hasModifierProperty(PsiModifier.STATIC)){
            return true;
        }
        final PsiClass referenceContainingClass = m_method.getContainingClass();
        final PsiClass fieldContainingClass = field.getContainingClass();
        return !InheritanceUtil.isInheritorOrSelf(referenceContainingClass,
                                                  fieldContainingClass, true);
    }
}