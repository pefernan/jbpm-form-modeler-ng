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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ContextConfiguration {
    private String formTemplate;
    private Form form;
    private String serializedStatus;
    private Map<String, Object> inputData;
    private Map<String, Object> attributes = new HashMap<String, Object>();
    private Locale locale;
    private Map<String, Object> contextForms = new HashMap<String, Object>();

    public ContextConfiguration(String formTemplate, Locale locale) {
        this.formTemplate = formTemplate;
        this.locale = locale;
    }

    public ContextConfiguration(String formTemplate, Map<String, Object> inputData, Locale locale) {
        this.formTemplate = formTemplate;
        this.inputData = inputData;
        this.locale = locale;
    }

    public ContextConfiguration(String formTemplate, Map<String, Object> inputData, Locale locale, String serializedStatus) {
        this.formTemplate = formTemplate;
        this.inputData = inputData;
        this.locale = locale;
        this.serializedStatus = serializedStatus;
    }

    public ContextConfiguration(Form form, Map<String, Object> inputData, Locale locale) {
        this.form = form;
        this.inputData = inputData;
        this.locale = locale;
    }

    public Form getForm() {
        return form;
    }

    public String getFormTemplate() {
        return formTemplate;
    }

    public String getSerializedStatus() {
        return serializedStatus;
    }

    public Map<String, Object> getInputData() {
        return inputData;
    }

    public Locale getLocale() {
        return locale;
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public void addAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public Map<String, Object> getContextForms() {
        return contextForms;
    }

    public void setContextForms(Map<String, Object> contextForms) {
        this.contextForms = contextForms;
    }
}
