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
package com.siyeh.ig.fixes;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.IncorrectOperationException;
import com.siyeh.ig.InspectionGadgetsFix;
import com.siyeh.ig.psiutils.ClassUtils;

public class MakeSerializableFix extends InspectionGadgetsFix{
    public String getName(){
        return "Make class Serializable";
    }

    public void doFix(Project project, ProblemDescriptor descriptor)
            throws IncorrectOperationException{
        final PsiElement nameElement = descriptor.getPsiElement();
        final PsiClass containingClass = ClassUtils.getContainingClass(nameElement);
        assert containingClass != null;
        final PsiManager psiManager = containingClass.getManager();
        final PsiElementFactory elementFactory = psiManager.getElementFactory();
        final GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        final PsiJavaCodeReferenceElement ref = elementFactory.createReferenceElementByFQClassName("java.io.Serializable",
                                                                                                   scope);
        final PsiReferenceList implementsList = containingClass.getImplementsList();
        assert implementsList != null;
        implementsList.add(ref);
    }
}
