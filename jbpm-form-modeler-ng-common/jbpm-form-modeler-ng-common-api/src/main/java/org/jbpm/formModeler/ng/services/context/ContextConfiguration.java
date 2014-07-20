package org.jbpm.formModeler.ng.services.context;

import org.jbpm.formModeler.ng.model.Form;

import java.util.HashMap;
import java.util.Map;

public class ContextConfiguration {
    private Form form;
    private Map<String, Object> loadData;
    private Map<String, Object> attributes;

    public ContextConfiguration(Form form, Map<String, Object> loadData) {
        this.form = form;
        this.loadData = loadData;
        this.attributes = new HashMap<String, Object>();
    }

    public Form getForm() {
        return form;
    }

    public Map<String, Object> getLoadData() {
        return loadData;
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public void addAttribute(String name, Object value) {
        attributes.put(name, value);
    }
}
