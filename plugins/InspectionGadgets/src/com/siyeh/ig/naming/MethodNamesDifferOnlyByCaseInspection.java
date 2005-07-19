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
package com.siyeh.ig.naming;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.siyeh.ig.BaseInspectionVisitor;
import com.siyeh.ig.InspectionGadgetsFix;
import com.siyeh.ig.MethodInspection;
import com.siyeh.ig.fixes.RenameFix;
import org.jetbrains.annotations.NotNull;

public class MethodNamesDifferOnlyByCaseInspection extends MethodInspection {
    public String getID(){
        return "MethodNamesDifferingOnlyByCase";
    }
    private final RenameFix fix = new RenameFix();

    public String getDisplayName() {
        return "Method names differing only by case";
    }

    public String getGroupDisplayName() {
        return GroupNames.NAMING_CONVENTIONS_GROUP_NAME;
    }

    public String buildErrorString(Object arg) {
        return "Method names '#ref' and '" + arg + "' differ only by case";
    }

    public BaseInspectionVisitor buildVisitor() {
        return new OverloadedMethodsWithSameNumberOfParametersVisitor();
    }

    protected boolean buildQuickFixesOnlyForOnTheFlyErrors(){
        return true;
    }

    protected InspectionGadgetsFix buildFix(PsiElement location){
        return fix;
    }

    private static class OverloadedMethodsWithSameNumberOfParametersVisitor extends BaseInspectionVisitor {

        public void visitMethod(@NotNull PsiMethod method) {
            if (method.isConstructor()) {
                return;
            }
            final PsiIdentifier nameIdentifier = method.getNameIdentifier();
            if (nameIdentifier == null) {
                return;
            }
            final String methodName = method.getName();
            if (methodName == null) {
                return;
            }
            final PsiClass aClass = method.getContainingClass();
            if (aClass == null) {
                return;
            }
            final PsiMethod[] methods = aClass.getMethods();
            for(PsiMethod testMethod : methods){
                final String testMethName = testMethod.getName();
                if(testMethName != null && !methodName.equals(testMethName) &&
                        methodName.equalsIgnoreCase(testMethName)){
                    registerError(nameIdentifier, testMethName);
                }
            }
        }
    }

}
