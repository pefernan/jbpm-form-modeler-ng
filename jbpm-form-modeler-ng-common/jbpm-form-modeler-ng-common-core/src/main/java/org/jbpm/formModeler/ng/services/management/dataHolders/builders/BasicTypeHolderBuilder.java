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
package org.jbpm.formModeler.ng.services.management.dataHolders.builders;

import org.jbpm.formModeler.ng.model.BasicTypeField;
import org.jbpm.formModeler.ng.model.DataHolder;
import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.services.management.dataHolders.AbstractRangedDataHolderBuilder;
import org.jbpm.formModeler.ng.services.management.dataHolders.DataHolderBuildConfig;
import org.jbpm.formModeler.ng.services.management.forms.FieldManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class BasicTypeHolderBuilder extends AbstractRangedDataHolderBuilder {
    public static final String HOLDER_TYPE_BASIC_TYPE = "basicType";

    @Inject
    private FieldManager fieldManager;

    @Override
    public String getId() {
        return HOLDER_TYPE_BASIC_TYPE;
    }

    @Override
    public DataHolder buildDataHolder(DataHolderBuildConfig config) {
        String fieldClass = config.getClassName();
        if (fieldManager.getFieldByClass(fieldClass) == null) return null;

        return new BasicTypeDataHolder(config.getHolderId(), config.getInputId(), config.getOutputId(), fieldClass, config.getRenderColor());
    }

    @Override
    public boolean supportsPropertyType(String typeClass, Map<String, Object> context) {
        return fieldManager.getFieldByClass(typeClass) != null;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public Map<String, String> getHolderSources(String context) {
        Map<String, String> result = new TreeMap<String, String>();
        try {
            List<BasicTypeField> allFieldTypes = fieldManager.getBasicFields();
            for (Field field : allFieldTypes) {
                if (!fieldManager.isVisible(field.getCode())) continue;
                String supportedClassName = field.getFieldClass();
                int lastIndex = supportedClassName.lastIndexOf(".");
                if (lastIndex != -1) supportedClassName = supportedClassName.substring(lastIndex + 1);
                result.put(field.getFieldClass(), supportedClassName);
            }

        } catch (Throwable e) {
            result.put("-", "-");
        }
        return result;
    }

    @Override
    public String getDataHolderName(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("org.jbpm.formModeler.ng.dataHolders.messages", locale);
        return bundle.getString("dataHolder_basicType");
    }
}
