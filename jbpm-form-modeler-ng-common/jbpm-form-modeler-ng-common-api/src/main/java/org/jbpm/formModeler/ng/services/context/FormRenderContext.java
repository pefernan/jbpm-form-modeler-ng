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
package org.jbpm.formModeler.ng.services.context;

import org.jbpm.formModeler.ng.model.Form;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FormRenderContext implements Serializable {
    private String UID;
    private Form form;
    private boolean readonly = false;
    private Map<String, Object> data;
    private boolean submit = false;
    private int errors;
    private Map<String, Object> contextForms = new HashMap<String, Object>();
    private Map<String, Object> attributes = new HashMap<String, Object>();

    public FormRenderContext() {
    }

    public FormRenderContext(String uid, Form form, Map<String, Object> data) {
        this.UID = uid;
        this.form = form;
        this.data = this.data;
    }

    public String getUID() {
        return UID;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public boolean isSubmit() {
        return submit;
    }

    public void setSubmit(boolean submit) {
        this.submit = submit;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public int getErrors() {
        return errors;
    }

    public Map<String, Object> getContextForms() {
        return contextForms;
    }

    public void setContextForms(Map<String, Object> contextForms) {
        this.contextForms = contextForms;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
