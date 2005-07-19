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

import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

class LoopCountVisitor extends PsiRecursiveElementVisitor {
    private int m_count = 0;


    public void visitForStatement(@NotNull PsiForStatement psiForStatement) {
        super.visitForStatement(psiForStatement);
        m_count++;
    }

    public void visitForeachStatement(@NotNull PsiForeachStatement psiForStatement) {
        super.visitForeachStatement(psiForStatement);
        m_count++;
    }

    public void visitWhileStatement(@NotNull PsiWhileStatement psiWhileStatement) {
        super.visitWhileStatement(psiWhileStatement);
        m_count++;
    }

    public void visitDoWhileStatement(@NotNull PsiDoWhileStatement psiDoWhileStatement) {
        super.visitDoWhileStatement(psiDoWhileStatement);
        m_count++;
    }

    public void visitAnonymousClass(@NotNull PsiAnonymousClass aClass) {
        // no call to super, to keep it from drilling into anonymous classes
    }

    public int getCount() {
        return m_count;
    }
}
