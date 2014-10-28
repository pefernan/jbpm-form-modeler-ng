package org.jbpm.formModeler.ng.editor.events.canvas;

import org.jbpm.formModeler.ng.editor.events.FormModelerEvent;

public class StartEditFieldPropertyEvent extends FormModelerEvent {
    private String fieldId;

    public StartEditFieldPropertyEvent() {
    }

    public StartEditFieldPropertyEvent(String context, String fieldId) {
        this.context = context;
        this.fieldId = fieldId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }
}
