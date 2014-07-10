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
import org.jbpm.formModeler.ng.model.DataFieldHolder;
import org.jbpm.formModeler.ng.model.DataHolder;
import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.model.Form;

public class BindingUtils {
    public static String generateInputBinding(DataHolder holder, DataFieldHolder field) {
        return holder.getUniqueId() + "/" + field.getId();
    }

    public static String generateOutputBinding(DataHolder holder, DataFieldHolder field) {
        return holder.getUniqueId() + "/" + field.getId();
    }

    public static String generateBackGuardsInputBinding(DataHolder holder, DataFieldHolder field) {
        return holder.getInputId() + "/" + field.getId();
    }

    public static String generateBackGuardsOutputBinding(DataHolder holder, DataFieldHolder field) {
        return holder.getOutputId() + "/" + field.getId();
    }

    public static boolean isFieldBinded(Form form, DataFieldHolder fieldHolder) {
        if (fieldHolder == null) return false;

        String inputBinding = generateInputBinding(fieldHolder.getHolder(), fieldHolder);
        String outputBinding = generateOutputBinding(fieldHolder.getHolder(), fieldHolder);

        String backInputBinding = generateBackGuardsInputBinding(fieldHolder.getHolder(), fieldHolder);
        String backOutputBinding = generateBackGuardsOutputBinding(fieldHolder.getHolder(), fieldHolder);

        for (Field field : form.getFormFields()) {
            if (!StringUtils.isEmpty(field.getInputBinding())) {
                if (field.getInputBinding().equals(inputBinding) || field.getInputBinding().equals(backInputBinding))
                    return true;
            }

            if (!StringUtils.isEmpty(field.getOutputBinding())) {
                if (field.getOutputBinding().equals(outputBinding) || field.getOutputBinding().equals(backOutputBinding))
                    return true;
            }
        }

        return false;
    }


}
