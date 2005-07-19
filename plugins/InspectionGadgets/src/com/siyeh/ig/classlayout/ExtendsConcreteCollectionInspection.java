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
package com.siyeh.ig.classlayout;

import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.siyeh.ig.BaseInspectionVisitor;
import com.siyeh.ig.ClassInspection;
import com.siyeh.ig.InspectionGadgetsFix;
import com.siyeh.ig.fixes.ReplaceInheritanceWithDelegationFix;
import com.siyeh.ig.psiutils.CollectionUtils;
import org.jetbrains.annotations.NotNull;

public class ExtendsConcreteCollectionInspection extends ClassInspection{
    private final ReplaceInheritanceWithDelegationFix fix = new ReplaceInheritanceWithDelegationFix();

    public String getID(){
        return "ClassExtendsConcreteCollection";
    }

    public String getDisplayName(){
        return "Class explicitly extends a Collection class";
    }

    public String getGroupDisplayName(){
        return GroupNames.INHERITANCE_GROUP_NAME;
    }

    public String buildErrorString(PsiElement location){
        final PsiClass aClass = (PsiClass) location.getParent();
        final PsiClass superClass = aClass.getSuperClass();
        assert superClass != null;
        return "Class '#ref' explicitly extends " + superClass.getQualifiedName() +" #loc";
    }

    protected InspectionGadgetsFix buildFix(PsiElement location){
        return fix;
    }

    public BaseInspectionVisitor buildVisitor(){
        return new ExtendsConcreteCollectionVisitor();
    }

    private static class ExtendsConcreteCollectionVisitor extends BaseInspectionVisitor{

        public void visitClass(@NotNull PsiClass aClass){
            if(aClass.isInterface() || aClass.isAnnotationType()|| aClass.isEnum()){
                return;
            }
            final PsiClass superClass = aClass.getSuperClass();
            if(superClass == null)
            {
                return;
            }
            if(!CollectionUtils.isCollectionClass(superClass))
            {
                return;
            }
            registerClassError(aClass);
        }
    }
}
