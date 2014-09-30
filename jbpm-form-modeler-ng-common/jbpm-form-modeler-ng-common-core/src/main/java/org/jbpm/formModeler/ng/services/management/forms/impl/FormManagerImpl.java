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
package org.jbpm.formModeler.ng.services.management.forms.impl;

import org.apache.commons.lang.StringUtils;
import org.jbpm.formModeler.ng.model.DataFieldHolder;
import org.jbpm.formModeler.ng.model.DataHolder;
import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.model.Form;
import org.jbpm.formModeler.ng.services.LocaleManager;
import org.jbpm.formModeler.ng.services.management.forms.FieldManager;
import org.jbpm.formModeler.ng.services.management.forms.FormManager;
import org.jbpm.formModeler.ng.services.management.forms.FormSerializationManager;
import org.jbpm.formModeler.ng.services.management.forms.utils.BindingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class FormManagerImpl implements FormManager {
    private Logger log = LoggerFactory.getLogger(FormManagerImpl.class);

    @Inject
    private FormSerializationManager formSerializationManager;

    @Inject
    private FieldManager fieldManager;

    @Inject
    private LocaleManager localeManager;

    private List<Form> systemForms;

    @Override
    public void setSystemForms(List<Form> systemForms) {
        this.systemForms = systemForms;
    }

    @Override
    public List<Form> getSystemForms() {
        return systemForms;
    }

    @Override
    public Form getFormForFieldEdition(String code) {
        if (StringUtils.isEmpty(code)) {
            log.error("Found field type without code.");
        } else {
            Form result = getFormByName(code);

            if (result != null) return result;
            // Pattern search
            for (Form form : systemForms) {
                if (form.getName() != null) {
                    try {
                        if (code.matches(form.getName())) {
                            return form;
                        }
                    } catch (Exception pse) {
                        //Ignore wrong patterns
                    }
                }
            }
        }
        return getFormByName("default");
    }

    @Override
    public Form getFormByName(String name) {
        for (Form form : systemForms) {
            if (form.getName() != null && form.getName().equals(name))
                return form;
        }
        return null;
    }

    @Override
    public Form getFormById(final Long id) {
        for (Form form : systemForms) {
            if (form.getId().equals(id)) return form;
        }
        return null;
    }

    @Override
    public Form createForm() {
        Form form = new Form();
        return form;
    }

    @Override
    public Form createForm(String name) {
        Form form = new Form(generateUniqueId(), name);
        return form;
    }

    @Override
    public void changeFieldPosition(Form form, Long fieldId, int row, int column, boolean newLine) {
        int rows = form.getElementsGrid().size();
        Field field = form.deleteField(fieldId);
        if (field == null) return;

        if (rows > form.getElementsGrid().size() && row > field.getRow()) row--;

        LinkedList<Field> fields;
        if (newLine) {
            fields = new LinkedList<Field>();
            form.getElementsGrid().add(row, fields);
        } else {
            fields = form.getElementsGrid().get(row);
        }
        fields.add(column, field);
    }

    @Override
    public void moveFirst(Form form, Long fieldId) {
        Field field = form.deleteField(fieldId);
        if (field == null) return;
        LinkedList<Field> fields = new LinkedList<Field>();
        fields.add(field);
        form.getElementsGrid().addFirst(fields);
    }

    @Override
    public void moveLast(Form form, Long fieldId) {
        Field field = form.deleteField(fieldId);
        if (field == null) return;
        LinkedList<Field> fields = new LinkedList<Field>();
        fields.add(field);
        form.getElementsGrid().addLast(fields);
    }

    @Override
    public boolean changeFieldType(Form form, Field field, String code) {
        Field result = fieldManager.getFieldByCode(code);
        if (result == null) return false;
        try {
            result.copyValues(field);
            for (LinkedList<Field> row : form.getElementsGrid()) {
                int index = row.indexOf(field);
                if (index != -1) {
                    row.remove(index);
                    row.add(index, result);
                    return true;
                }
            }
        } catch (Exception ex) {
            log.warn("Error changing fieldType for field '{}' type '{}' to '{}': {}", field.getName(), field.getCode(), code, ex);

        }
        return false;
    }

    @Override
    public void addDataHolderToForm(Form form, DataHolder holder) {
        if (holder != null) form.addDataHolder(holder);
    }

    @Override
    public void removeDataHolderFromForm(Form form, String holderId) {
        if ((holderId != null)) {
            form.removeDataHolder(holderId);
        }
    }

    @Override
    public void addAllDataHolderFieldsToForm(Form form, String holderId) {
        if (holderId != null) {

            DataHolder holder = form.getDataHolderById(holderId);

            addDataHolderFields(form, holder, true);
        }
    }

    public void addAllDataHolderFieldsToForm(Form form, DataHolder holder) {
        addDataHolderFields(form, holder, false);
    }

    protected void addDataHolderFields(Form form, DataHolder holder, boolean existing) {
        if (holder != null) {
            if (!existing) {
                if (form.containsHolder(holder)) return;
                else form.addDataHolder(holder);
            }
            if (!form.containsHolder(holder)) return;
            Set<DataFieldHolder> holderFields = holder.getFieldHolders();
            for (DataFieldHolder fieldHolder : holderFields) {
                addDataHolderField(form, holder, fieldHolder);
            }
        }
    }

    @Override
    public boolean addDataHolderField(Form form, DataHolder holder, DataFieldHolder holderField) {
        if (form == null || holder == null || holderField == null) return false;

        if (BindingUtils.isFieldBinded(form, holderField)) return false;

        Map label = new HashMap();

        label.put(localeManager.getDefaultLang(), holderField.getId() + " (" + holder.getUniqueId() + ")");

        String inputBinging = BindingUtils.generateInputBinding(holder, holderField);

        Field field = fieldManager.getFieldByClass(holderField.getClassName());

        String fieldName;
        if (!holder.canHaveChildren()) {
            fieldName = holder.getUniqueId();
        } else {
            fieldName = holder.getUniqueId() + "_" + holderField.getId();
        }

        int i = 0;
        String tmpFName = fieldName;
        while (form.getField(tmpFName) != null) {
            tmpFName = fieldName + "_" + i;
        }

        field.setId(generateUniqueId());
        field.setName(tmpFName);
        field.setLabel(label);
        field.setBindingExpression(inputBinging);
        field.setForm(form);
        return form.addField(field);
    }


    protected static Long generateUniqueId() {
        UUID idOne = UUID.randomUUID();
        String str = "" + idOne;
        int uid = str.hashCode();
        String filterStr = "" + uid;
        str = filterStr.replaceAll("-", "");
        return Long.decode(str);
    }
}
