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

import java.util.Map;

public abstract class Field extends FormElement {

    protected String name;

    protected Map label;

    protected Boolean fieldRequired = Boolean.FALSE;

    protected Boolean readonly = Boolean.FALSE;

    protected String inputBinding;
    protected String outputBinding;

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

    public Map getLabel() {
        return label;
    }

    public void setLabel(Map label) {
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

    public String getInputBinding() {
        return inputBinding;
    }

    public void setInputBinding(String inputBinding) {
        this.inputBinding = inputBinding;
    }

    public String getOutputBinding() {
        return outputBinding;
    }

    public void setOutputBinding(String outputBinding) {
        this.outputBinding = outputBinding;
    }
}
