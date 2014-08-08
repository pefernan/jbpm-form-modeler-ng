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

import org.apache.commons.lang.SerializationUtils;
import org.jbpm.formModeler.ng.model.BasicTypeField;
import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.services.management.forms.FieldManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@ApplicationScoped
public class FieldManagerImpl implements FieldManager {

    private List<BasicTypeField> fieldTypes;
    private List<Field> decoratorTypes;
    private List<Field> complexTypes;

    private Logger log = LoggerFactory.getLogger(FieldManager.class);

    @Inject
    protected Instance<BasicTypeField> basicFields;

    private ArrayList<String> hiddenFieldTypesCodes = new ArrayList<String>();

    private Map<String, Field> backguardCompatibilityFields = new HashMap<String, Field>();
    private Map<String, Field> compatibleClasses = new HashMap<String, Field>();

    private ArrayList<String> baseTypes = new ArrayList<String>();


    @PostConstruct
    protected void init() {

        fieldTypes = new ArrayList<BasicTypeField>();

        for (BasicTypeField field : basicFields) {
            fieldTypes.add(field);
            // Storing primitive fields for backguards comptaibility
            if (field.getCode().equals("InputTextByte")) {
                backguardCompatibilityFields.put("InputTextPrimitiveByte", field);
                compatibleClasses.put(byte.class.getName(), field);
            } else if (field.getCode().equals("InputTextCharacter")) {
                backguardCompatibilityFields.put("InputTextPrimitiveCharacter", field);
                compatibleClasses.put(char.class.getName(), field);
            } else if (field.getCode().equals("InputTextShort")) {
                backguardCompatibilityFields.put("InputTextPrimitiveShort", field);
                compatibleClasses.put(short.class.getName(), field);
            } else if (field.getCode().equals("InputTextInteger")) {
                backguardCompatibilityFields.put("InputTextPrimitiveInteger", field);
                compatibleClasses.put(int.class.getName(), field);
            } else if (field.getCode().equals("InputTextLong")) {
                backguardCompatibilityFields.put("InputTextPrimitiveLong", field);
                compatibleClasses.put(long.class.getName(), field);
            } else if (field.getCode().equals("InputTextFloat")) {
                backguardCompatibilityFields.put("InputTextPrimitiveFloat", field);
                compatibleClasses.put(float.class.getName(), field);
            } else if (field.getCode().equals("InputTextDouble")) {
                backguardCompatibilityFields.put("InputTextPrimitiveDouble", field);
                compatibleClasses.put(double.class.getName(), field);
            } else if (field.getCode().equals("CheckBox")) {
                backguardCompatibilityFields.put("CheckBoxPrimitiveBoolean", field);
                compatibleClasses.put(boolean.class.getName(), field);
            }
        }

        hiddenFieldTypesCodes.add("I18nText");
        hiddenFieldTypesCodes.add("CustomInput");

        baseTypes.add("InputText");
        baseTypes.add("InputDate");
        baseTypes.add("CheckBox");
        baseTypes.add("InputTextCharacter");
        baseTypes.add("InputTextByte");
        baseTypes.add("InputTextShort");
        baseTypes.add("InputTextInteger");
        baseTypes.add("InputTextLong");
        baseTypes.add("InputTextFloat");
        baseTypes.add("InputTextDouble");
        baseTypes.add("InputTextBigDecimal");
        baseTypes.add("InputTextBigInteger");
    }

    @Override
    public List<BasicTypeField> getBasicFields() {
        return fieldTypes;
    }

    @Override
    public List<Field> getSuitableFields(Field field) {
        List<Field> result = new ArrayList<Field>();

        for (Field typeField : fieldTypes) {
            if (typeField.getClass().equals(field.getClass())) result.add(typeField);
        }

        return result;
    }

    @Override
    public Field getFieldByCode(String typeCode) {
        for (Field field : fieldTypes) {
            if (field.getCode().equals(typeCode)) return (Field) SerializationUtils.clone(field);
        }
        return backguardCompatibilityFields.get(typeCode);
    }

    @Override
    public Field getFieldByClass(String classType) {
        for (Field field : fieldTypes) {
            if (field.getFieldClass().equals(classType)) return (Field)SerializationUtils.clone(field);
        }
        return compatibleClasses.get(classType);
    }

    @Override
    public boolean isVisible(String typeCode) {
        return !hiddenFieldTypesCodes.contains(typeCode);
    }
}
