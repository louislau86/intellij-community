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
package com.siyeh.ig.security;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.siyeh.ig.BaseInspectionVisitor;
import com.siyeh.ig.ClassInspection;
import com.siyeh.ig.psiutils.ControlFlowUtils;
import com.siyeh.ig.psiutils.SerializationUtils;
import org.jetbrains.annotations.NotNull;

public class DeserializableClassInSecureContextInspection extends ClassInspection {

    public String getDisplayName() {
        return "Deserializable class in secure context";
    }

    public String getGroupDisplayName() {
        return GroupNames.SECURITY_GROUP_NAME;
    }

    public String buildErrorString(PsiElement location) {
        return "Class #ref may be deserialized, compromising security #loc";
    }

    public BaseInspectionVisitor buildVisitor() {
        return new DeserializableClassInSecureContextVisitor();
    }

    private static class DeserializableClassInSecureContextVisitor extends BaseInspectionVisitor {

        public void visitClass(@NotNull PsiClass aClass) {
            // no call to super, so it doesn't drill down
            if (aClass.isInterface() || aClass.isAnnotationType()|| aClass.isEnum()) {
                return;
            }
            if (!SerializationUtils.isSerializable(aClass)) {
                return;
            }
            final PsiMethod[] methods = aClass.getMethods();
            for(final PsiMethod method : methods){
                if(SerializationUtils.isReadObject(method)){
                    if(ControlFlowUtils.methodAlwaysThrowsException(method)){
                        return;
                    }
                }
            }
            registerClassError(aClass);
        }
    }
}
