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

import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.services.management.forms.FieldBuilder;
import org.jbpm.formModeler.ng.services.management.forms.FieldManager;
import org.jbpm.formModeler.ng.services.management.forms.impl.builders.SimpleFieldBuilder;
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

    private List<Field> fieldTypes;
    private List<Field> decoratorTypes;
    private List<Field> complexTypes;

    private Logger log = LoggerFactory.getLogger(FieldManager.class);

    @Inject
    protected Instance<SimpleFieldBuilder> builders;

    private Map<String, String> iconsMappings = new HashMap<String, String>();
    private String defaultIcon = "fieldTypes/default.png";

    private ArrayList<String> hiddenFieldTypesCodes = new ArrayList<String>();

    private ArrayList<String> baseTypes = new ArrayList<String>();


    @PostConstruct
    protected void init() {

        fieldTypes = new ArrayList<Field>();

        for (FieldBuilder builder : builders) {
            fieldTypes.addAll(builder.buildList());
        }

        iconsMappings.put("InputTextShort", "fieldTypes/box_number.png");
        iconsMappings.put("InputTextInteger", "fieldTypes/box_number.png");
        iconsMappings.put("InputTextIBAN", "fieldTypes/box_number.png");
        iconsMappings.put("Separator", "fieldTypes/splitter_box.png");
        iconsMappings.put("InputTextLong", "fieldTypes/box_number.png");
        iconsMappings.put("InputDate", "fieldTypes/date_selector.png");
        iconsMappings.put("I18nHTMLText", "fieldTypes/rich_text_box.png");
        iconsMappings.put("HTMLEditor", "fieldTypes/rich_text_box.png");
        iconsMappings.put("InputTextArea", "fieldTypes/scroll_zone.png");
        iconsMappings.put("I18nTextArea", "fieldTypes/scroll_zone.png");
        iconsMappings.put("CheckBox", "fieldTypes/checkbox.png");
        iconsMappings.put("CheckBoxPrimitiveBoolean", "fieldTypes/checkbox.png");
        iconsMappings.put("InputShortDate", "fieldTypes/date_selector.png");
        iconsMappings.put("I18nText", "fieldTypes/textbox.png");
        iconsMappings.put("InputTextFloat", "fieldTypes/box_number.png");
        iconsMappings.put("InputTextBigDecimal", "fieldTypes/box_number.png");
        iconsMappings.put("InputTextBigInteger", "fieldTypes/box_number.png");
        iconsMappings.put("InputTextDouble", "fieldTypes/box_number.png");
        iconsMappings.put("HTMLLabel", "fieldTypes/rich_text_box.png");
        iconsMappings.put("InputText", "fieldTypes/textbox.png");
        iconsMappings.put("InputTextEmail", "fieldTypes/mailbox.png");
        iconsMappings.put("Subform", "fieldTypes/master_details.png");
        iconsMappings.put("MultipleSubform", "fieldTypes/master_details.png");
        iconsMappings.put("Document", "fieldTypes/file.png");
        iconsMappings.put("File", "fieldTypes/file.png");

        hiddenFieldTypesCodes.add("InputTextPrimitiveByte");
        hiddenFieldTypesCodes.add("InputTextPrimitiveShort");
        hiddenFieldTypesCodes.add("InputTextPrimitiveInteger");
        hiddenFieldTypesCodes.add("InputTextPrimitiveLong");
        hiddenFieldTypesCodes.add("InputTextPrimitiveFloat");
        hiddenFieldTypesCodes.add("InputTextPrimitiveDouble");
        hiddenFieldTypesCodes.add("CheckBoxPrimitiveBoolean");
        hiddenFieldTypesCodes.add("InputTextPrimitiveCharacter");
        hiddenFieldTypesCodes.add("I18nHTMLText");
        hiddenFieldTypesCodes.add("I18nText");
        hiddenFieldTypesCodes.add("I18nTextArea");
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
    public List<Field> getFields() {
        return fieldTypes;
    }

    @Override
    public void setFields(List<Field> fieldTypes) {
        this.fieldTypes = fieldTypes;
    }

    @Override
    public List<Field> getSuitableFields(Field field) {
        List<Field> result = new ArrayList<Field>();

        for (Field typeField : fieldTypes) {
            if (typeField.getClass().equals(field.getClass())) result.add(typeField.clone());
        }

        return result;
    }

    @Override
    public Field getFieldByCode(String typeCode) {
        for (Field field : fieldTypes) {
            if (field.getCode().equals(typeCode)) return field.clone();
        }
        return null;
    }

    @Override
    public Field getFieldByClass(String classType) {
        for (Field field : fieldTypes) {
            if (field.getFieldClass().equals(classType)) return field.clone();
        }
        return null;
    }
}
