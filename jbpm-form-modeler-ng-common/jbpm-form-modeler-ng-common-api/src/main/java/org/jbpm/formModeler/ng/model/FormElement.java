package org.jbpm.formModeler.ng.model;

import java.io.Serializable;
import java.util.Map;

public abstract class FormElement implements Serializable, Comparable<FormElement> {
    protected Long id;

    protected Integer row;

    protected Integer column;

    protected Boolean groupWithPrevious = Boolean.FALSE;

    protected Form form;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getGroupWithPrevious() {
        return groupWithPrevious;
    }

    public void setGroupWithPrevious(Boolean groupWithPrevious) {
        this.groupWithPrevious = groupWithPrevious;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    @Override
    public int compareTo(FormElement o) {

        int row = getRow().compareTo(o.getRow());
        if (row != 0) return row;
        return getColumn().compareTo(o.getColumn());
    }

    public abstract Map<String, String> getCustomProperties();
    public abstract void setCustomProperties(Map<String, String> properties);
}
