package org.jbpm.formModeler.ng.model;

import java.io.Serializable;
import java.util.Map;

public abstract class FormElement implements Serializable, Comparable<FormElement> {
    protected Long id;

    protected int position;

    protected Boolean groupWithPrevious;

    protected Form form;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    @Override
    public int compareTo(FormElement o) {
        return new Integer(getPosition()).compareTo(o.getPosition());
    }

    public static class Comparator implements java.util.Comparator<FormElement> {
        public int compare(FormElement f1, FormElement f2) {
            int pos1 = f1.getPosition();
            int pos2 = f2.getPosition();
            if (pos1 != pos2)
                return f1.getPosition() - f2.getPosition();
            else
                return (int) (f1.getId().longValue() - f2.getId().longValue());
        }
    }

    public abstract Map<String, String> getCustomProperties();
    public abstract void setCustomProperties(Map<String, String> properties);
}
