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
package com.siyeh.ig.encapsulation;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifier;
import com.siyeh.ig.BaseInspectionVisitor;
import com.siyeh.ig.FieldInspection;
import com.siyeh.ig.InspectionGadgetsFix;
import com.siyeh.ig.fixes.EncapsulateVariableFix;
import org.jetbrains.annotations.NotNull;

public class PackageVisibleFieldInspection extends FieldInspection {
    private final EncapsulateVariableFix fix = new EncapsulateVariableFix();

    public String getDisplayName() {
        return "Package-visible field";
    }

    public String getGroupDisplayName() {
        return GroupNames.ENCAPSULATION_GROUP_NAME;
    }

    public String buildErrorString(PsiElement location) {
        return "Package-visible field #ref #loc";
    }

    protected InspectionGadgetsFix buildFix(PsiElement location) {
        return fix;
    }

    protected boolean buildQuickFixesOnlyForOnTheFlyErrors() {
        return true;
    }

    public BaseInspectionVisitor buildVisitor() {
        return new ProtectedFieldVisitor();
    }

    private static class ProtectedFieldVisitor extends BaseInspectionVisitor {


        public void visitField(@NotNull PsiField field) {
            if (field.hasModifierProperty(PsiModifier.PROTECTED) ||
                    field.hasModifierProperty(PsiModifier.PUBLIC) ||
                    field.hasModifierProperty(PsiModifier.PRIVATE)) {
                return;
            }
            if (field.hasModifierProperty(PsiModifier.STATIC) &&
                    field.hasModifierProperty(PsiModifier.FINAL)) {
                return;
            }
            registerFieldError(field);
        }

    }

}
