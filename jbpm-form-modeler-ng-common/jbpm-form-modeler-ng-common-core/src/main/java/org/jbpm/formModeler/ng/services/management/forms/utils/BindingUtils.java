/**
 * Copyright (C) 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.formModeler.ng.services.management.forms.utils;

import org.apache.commons.lang.StringUtils;
import org.jbpm.formModeler.ng.model.*;

import java.util.LinkedList;

public class BindingUtils {
    public static String generateBinding(DataHolder holder, DataFieldHolder field) {
        if (!holder.canHaveChildren()) return holder.getUniqueId();
        return holder.getUniqueId() + "/" + field.getId();
    }

    public static boolean isFieldBinded(Form form, DataFieldHolder fieldHolder) {
        if (fieldHolder == null) return false;
        return getFielForBindingExpression(generateBinding(fieldHolder.getHolder(), fieldHolder), form) != null;
    }


    public static final Field getFielForBindingExpression(String bindingExpression, Form form) {
        for (FormElement formElement : form.getElements()) {
            Field field = (Field) formElement;
            if (!StringUtils.isEmpty(field.getBindingExpression())) {
                if (field.getBindingExpression().equals(bindingExpression))
                    return field;
            }
        }
        return null;
    }

    public static DataHolder getDataHolderForField(Field field) {
        if (field == null || field.getBindingExpression() == null) return null;

        for (DataHolder holder : field.getForm().getHolders()) {
            if (holderSupportsExpression(holder, field.getBindingExpression())) return holder;
        }
        return null;
    }

    protected static boolean holderSupportsExpression(DataHolder holder, String bindingExpression) {
        if (StringUtils.isEmpty(bindingExpression)) return false;

        String[] parts = bindingExpression.split("/");

        if (parts.length <= 2) return holderSupportsId(holder, parts[0]);

        return false;
    }

    protected static boolean holderSupportsId(DataHolder holder, String holderId) {
        if (StringUtils.isEmpty(holderId)) return false;
        return holder.getUniqueId().equals(holderId);
    }

    public static String extractBindingExpression(String expression) {
        if (StringUtils.isEmpty(expression)) return "";
        String[] parts = expression.split("/");
        if (parts.length == 1) return parts[0];
        return parts[1];
    }
}
