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
package com.siyeh.ig.portability;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.siyeh.ig.BaseInspectionVisitor;
import com.siyeh.ig.MethodInspection;
import org.jetbrains.annotations.NotNull;

public class NativeMethodsInspection extends MethodInspection{
    public String getID(){
        return "NativeMethod";
    }

    public String getDisplayName(){
        return "Native method";
    }

    public String getGroupDisplayName(){
        return GroupNames.PORTABILITY_GROUP_NAME;
    }

    public String buildErrorString(PsiElement location){
        return "Methods declared '#ref' are non-portable #loc";
    }

    public BaseInspectionVisitor buildVisitor(){
        return new NativeMethodVisitor();
    }

    private static class NativeMethodVisitor extends BaseInspectionVisitor{


        public void visitMethod(@NotNull PsiMethod method){
            if(!method.hasModifierProperty(PsiModifier.NATIVE)){
                return;
            }
            registerModifierError(PsiModifier.NATIVE, method);
        }
    }
}
