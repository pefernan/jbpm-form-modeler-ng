package org.jbpm.formModeler.ng.services.context;

import org.jbpm.formModeler.ng.model.Form;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ContextConfiguration {
    private Form form;
    private Map<String, Object> inputData;
    private Map<String, Object> outputData;
    private Map<String, Object> attributes;
    private Locale locale;
    private Map<String, Object> contextForms;

    public ContextConfiguration(Form form, Map<String, Object> inputData, Map<String, Object> outputData, Locale locale) {
        this.form = form;
        this.inputData = inputData;
        this.outputData = outputData;
        this.attributes = new HashMap<String, Object>();
        this.locale = locale;
    }

    public Form getForm() {
        return form;
    }

    public Map<String, Object> getInputData() {
        return inputData;
    }

    public Map<String, Object> getOutputData() {
        return outputData;
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
