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

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.InputStream;
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

    @PostConstruct
    protected void init() {
        Map<String, Properties> formResources = new HashMap<String, Properties>();

        systemForms = new ArrayList<Form>();

        for (String lang : localeManager.getPlatformAvailableLangs()) {
            try {
                String key = lang.equals(localeManager.getDefaultLang()) ? "" : "_" + lang;

                InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(getFormResourcesPath(key));

                if (in == null) continue;
                Properties props = new Properties();
                props.load(in);
                formResources.put(lang, props);
            } catch (Exception e) {
                log.warn("Error loading resources form lang \"{}\": {}", lang, e);
            }
        }

        deployFieldTypeForm("default", formResources);
        deployFieldTypesForms(fieldManager.getFields(), formResources);
    }

    protected void deployFieldTypesForms(List<Field> fieldTypes, Map<String, Properties> formResources) {
        if (fieldTypes == null) return;
        for (Field fieldType : fieldTypes) {
            deployFieldTypeForm(fieldType.getCode(), formResources);
        }
    }

    protected void deployFieldTypeForm(String formName, Map<String, Properties> formResources) {
        String formPath = getFormPath(formName);
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(formPath);
            if (is == null) return;
            Form systemForm = formSerializationManager.loadFormFromXML(is, formResources);
            systemForms.add(systemForm);
        } catch (Exception e) {
            log.error("Error reading core form file: " + formPath, e);
        }
    }

    public String getFormPath(String formName) {
        return "org/jbpm/formModeler/ng/forms/" + formName + ".form";
    }

    public String getFormResourcesPath(String lang) {
        return "org/jbpm/formModeler/ng/forms/forms-resources" + lang + ".properties";
    }

    @Override
    public List<Form> getSystemForms() {
        return systemForms;
    }

    @Override
    public Form getFormForFieldEdition(String code) throws Exception {
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
    public void promoteField(Form pForm, int fieldPos, int destPos, boolean groupWithPrevious, boolean nextFieldGrouped) throws Exception {
        final List<Field> fields = new ArrayList(pForm.getFormFields());
        Collections.sort(fields, new Field.Comparator());

        boolean wasGrouped = false;
        for (Field formField : fields) {
            int position = formField.getPosition();
            if (position == fieldPos) {
                formField.setPosition(destPos);
                wasGrouped = Boolean.TRUE.equals(formField.getGroupWithPrevious());
                formField.setGroupWithPrevious(Boolean.valueOf(groupWithPrevious));
            } else if (position >= destPos && position < fieldPos) {
                formField.setPosition(formField.getPosition() + 1);
                if (position == destPos && !Boolean.TRUE.equals(formField.getGroupWithPrevious()))
                    formField.setGroupWithPrevious(Boolean.valueOf(nextFieldGrouped));
            } else if (position == fieldPos + 1 && Boolean.TRUE.equals(formField.getGroupWithPrevious()))
                formField.setGroupWithPrevious(Boolean.valueOf(wasGrouped));
        }
    }

    @Override
    public void degradeField(Form pForm, int fieldPos, int destPos, boolean groupWithPrevious, boolean nextFieldGrouped) throws Exception {
        List<Field> fields = new ArrayList(pForm.getFormFields());
        Collections.sort(fields, new Field.Comparator());

        boolean wasGrouped = false;
        for (Field formField : fields) {
            int position = formField.getPosition();
            if (position == fieldPos) {
                formField.setPosition(destPos);
                wasGrouped = Boolean.TRUE.equals(formField.getGroupWithPrevious());
                formField.setGroupWithPrevious(Boolean.valueOf(groupWithPrevious));
            } else if (position <= destPos && position > fieldPos) {
                formField.setPosition(formField.getPosition() - 1);
                if (position == fieldPos + 1 && Boolean.TRUE.equals(formField.getGroupWithPrevious()))
                    formField.setGroupWithPrevious(Boolean.valueOf(wasGrouped));
            } else if (position == destPos + 1 && !Boolean.TRUE.equals(formField.getGroupWithPrevious()))
                formField.setGroupWithPrevious(Boolean.valueOf(nextFieldGrouped));
        }
    }

    @Override
    public void changeFieldPosition(Form pForm, int fieldPos, int destPos, boolean groupWithPrevious, boolean nextFieldGrouped) throws Exception {
        final List<Field> fields = new ArrayList(pForm.getFormFields());
        Collections.sort(fields, new Field.Comparator());

        boolean promote = destPos < fieldPos;

        boolean checkGroup = false;
        Boolean wasGrouped = false;

        for (Field formField : fields) {
            int position = formField.getPosition();

            if (position == fieldPos) {
                formField.setPosition(destPos);
                checkGroup = true;
                wasGrouped = formField.getGroupWithPrevious();
                formField.setGroupWithPrevious(Boolean.valueOf(groupWithPrevious));
            } else if (promote) {
                if (position >= destPos && position < fieldPos) {
                    formField.setPosition(formField.getPosition() + 1);
                    if (position == destPos) formField.setGroupWithPrevious(Boolean.valueOf(nextFieldGrouped));
                    if (checkGroup) {
                        if (formField.getGroupWithPrevious()) {
                            formField.setGroupWithPrevious(wasGrouped);
                        }
                    }
                }
            } else {
                if (position > fieldPos && position <= destPos) {
                    formField.setPosition(formField.getPosition() - 1);
                    if (position == destPos) formField.setGroupWithPrevious(Boolean.valueOf(nextFieldGrouped));
                    if (checkGroup) {
                        if (formField.getGroupWithPrevious()) {
                            formField.setGroupWithPrevious(wasGrouped);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void moveTop(Form pForm, int fieldPos) throws Exception {
        Set<Field> fields = pForm.getFormFields();
        for (Field formField : fields) {
            if (formField.getPosition() == fieldPos) {
                formField.setPosition(0);
                formField.setGroupWithPrevious(Boolean.FALSE);
            } else if (formField.getPosition() < fieldPos) {
                formField.setPosition(formField.getPosition() + 1);
            }
        }
    }

    @Override
    public void moveBottom(Form pForm, int fieldPos) throws Exception {
        Set<Field> fields = pForm.getFormFields();
        for (Field formField : fields) {
            if (formField.getPosition() == fieldPos) {
                formField.setPosition(fields.size() - 1);
                formField.setGroupWithPrevious(Boolean.FALSE);
            } else if (formField.getPosition() > fieldPos) {
                formField.setPosition(formField.getPosition() - 1);
            }
        }
    }

    @Override
    public void moveUp(Form pForm, int fieldPos) throws Exception {
        Set<Field> fields = pForm.getFormFields();
        if (fieldPos < 1 || fieldPos >= fields.size()) {
            log.error("Cannot move up field in position " + fieldPos);
        } else {
            for (Field formField : fields) {
                if (formField.getPosition() == fieldPos) {
                    formField.setPosition(fieldPos - 1);
                } else if (formField.getPosition() == fieldPos - 1) {
                    formField.setPosition(fieldPos);
                }
            }
        }
    }

    @Override
    public void groupWithPrevious(Form pForm, int fieldPos, boolean value) throws Exception {
        Set<Field> fields = pForm.getFormFields();
        if (fieldPos < 1 || fieldPos >= fields.size()) {
            log.warn("Cannot change field in position " + fieldPos);
        } else {
            for (Field formField : fields) {
                if (formField.getPosition() == fieldPos) {
                    formField.setGroupWithPrevious(Boolean.valueOf(value));
                }
            }
        }
    }

    @Override
    public void moveDown(Form pForm, int fieldPos) throws Exception {
        Set<Field> fields = pForm.getFormFields();
        if (fieldPos < 0 || fieldPos >= fields.size() - 1) {
            log.warn("Cannot move down field in position " + fieldPos);
        } else {
            for (Field formField : fields) {
                if (formField.getPosition() == fieldPos) {
                    formField.setPosition(fieldPos + 1);
                } else if (formField.getPosition() == fieldPos + 1) {
                    formField.setPosition(fieldPos);
                }
            }
        }
    }

    @Override
    public void deleteField(Form pForm, int fieldPos) throws Exception {
        Set fields = pForm.getFormFields();
        if (fieldPos < 0 || fieldPos >= fields.size()) {
            log.warn("Cannot delete field in position " + fieldPos);
        } else {
            for (Iterator iterator = fields.iterator(); iterator.hasNext(); ) {
                Field formField = (Field) iterator.next();
                if (formField.getPosition() == fieldPos) {
                    iterator.remove();
                } else if (formField.getPosition() > fieldPos) {
                    formField.setPosition(formField.getPosition() - 1);
                }
            }
        }
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
                if (!BindingUtils.isFieldBinded(form, fieldHolder)) addDataHolderField(form, holder, fieldHolder);
            }
        }
    }

    @Override
    public void addDataHolderField(Form form, DataHolder holder, DataFieldHolder field) {
        if (form == null || holder == null || field == null) return;

        Map label = new HashMap();

        label.put(localeManager.getDefaultLang(), field.getId() + " (" + holder.getUniqueId() + ")");

        String inputBinging = BindingUtils.generateInputBinding(holder, field);
        String outputBinding = BindingUtils.generateOutputBinding(holder, field);

        Field fieldType = fieldManager.getFieldByClass(field.getClassName());

        String fieldName;
        if (!holder.canHaveChildren()) {
            fieldName = holder.getUniqueId();
        } else {
            fieldName = holder.getUniqueId() + "_" + field.getId();
        }

        int i = 0;
        String tmpFName = fieldName;
        while (form.getField(tmpFName) != null) {
            tmpFName = fieldName + "_" + i;
        }

        fieldType.setId(generateUniqueId());
        fieldType.setName(tmpFName);
        fieldType.setLabel(label);
        fieldType.setInputBinding(inputBinging);
        fieldType.setOutputBinding(outputBinding);
        form.addField(fieldType);
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
