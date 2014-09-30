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
package org.jbpm.formModeler.ng.model;

import java.util.HashMap;
import java.util.Map;

public abstract class Field extends FormElement {

    protected String name;

    protected Map<String, String> label = new HashMap<String, String>();

    protected Boolean fieldRequired = Boolean.FALSE;

    protected Boolean readonly = Boolean.FALSE;

    protected String bindingExpression;

    public abstract String getCode();

    public abstract String getFieldClass();

    public abstract FieldValueMarshaller getMarshaller();

    public abstract String getIcon();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getLabel() {
        return label;
    }

    public void setLabel(Map<String, String> label) {
        this.label = label;
    }

    public Boolean getFieldRequired() {
        return fieldRequired;
    }

    public void setFieldRequired(Boolean fieldRequired) {
        this.fieldRequired = fieldRequired;
    }

    public Boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public String getBindingExpression() {
        return bindingExpression;
    }

    public void setBindingExpression(String bindingExpression) {
        this.bindingExpression = bindingExpression;
    }

    public void copyValues(Field source) {
        if (source == null) return;

        setId(source.getId());
        setName(source.getName());
        setLabel(source.getLabel());
        setFieldRequired(source.getFieldRequired());
        setReadonly(source.getReadonly());
        setBindingExpression(source.getBindingExpression());
        setRow(source.getRow());
        setColumn(source.getColumn());
        setForm(source.getForm());
        setCustomProperties(source.getCustomProperties());
    }
}
