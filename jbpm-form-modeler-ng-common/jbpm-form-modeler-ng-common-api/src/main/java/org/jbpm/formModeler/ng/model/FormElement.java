package org.jbpm.formModeler.ng.model;

import java.io.Serializable;
import java.util.Map;

public abstract class FormElement implements Serializable, Comparable<FormElement> {
    protected Long id;

    protected String name;

    protected Boolean groupWithPrevious = Boolean.FALSE;

    protected Form form;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    @Override
    public int compareTo(FormElement o) {
        return this.getId().compareTo(o.getId());
    }
}
