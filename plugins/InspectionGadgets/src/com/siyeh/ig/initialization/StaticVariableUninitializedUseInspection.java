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
package com.siyeh.ig.initialization;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.psi.*;
import com.siyeh.ig.BaseInspectionVisitor;
import com.siyeh.ig.FieldInspection;
import com.siyeh.ig.psiutils.ClassUtils;
import com.siyeh.ig.psiutils.InitializationReadUtils;
import com.siyeh.ig.ui.SingleCheckboxOptionsPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

public class StaticVariableUninitializedUseInspection extends FieldInspection {
    /** @noinspection PublicField*/
    public boolean m_ignorePrimitives = false;

    public String getID(){
        return "StaticVariableUsedBeforeInitialization";
    }
    public String getDisplayName() {
        return "Static variable used before initialization";
    }

    public String getGroupDisplayName() {
        return GroupNames.INITIALIZATION_GROUP_NAME;
    }

    public String buildErrorString(PsiElement location) {
        return "Static variable #ref used before initialization #loc";
    }

    public JComponent createOptionsPanel() {
        return new SingleCheckboxOptionsPanel("Ignore primitive fields",
                this, "m_ignorePrimitives");
    }

    public BaseInspectionVisitor buildVisitor() {
        return new StaticVariableInitializationVisitor();
    }

    private class StaticVariableInitializationVisitor
            extends BaseInspectionVisitor {

        public void visitField(@NotNull PsiField field) {
            if (!field.hasModifierProperty(PsiModifier.STATIC)) {
                return;
            }
            if (field.getInitializer() != null) {
                return;
            }
            final PsiClass containingClass = field.getContainingClass();

            if (containingClass == null) {
                return;
            }
            if (containingClass.isEnum()) {
                return;
            }
            if (m_ignorePrimitives) {
                final PsiType type = field.getType();
                if (ClassUtils.isPrimitive(type)) {
                    return;
                }
            }
            final PsiClassInitializer[] initializers = containingClass.getInitializers();
            // Do the static initializers come in actual order in file? (They need to.)
            final InitializationReadUtils iru = new InitializationReadUtils();

            for(final PsiClassInitializer initializer : initializers){
                if(initializer.hasModifierProperty(PsiModifier.STATIC)){
                    final PsiCodeBlock body = initializer.getBody();
                    if(iru.blockMustAssignVariable(field, body)){
                        break;
                    }
                }
            }

            final List<PsiExpression> badReads = iru.getUninitializedReads();
            for(PsiExpression badRead : badReads){
                registerError(badRead);
            }
        }

    }
}
