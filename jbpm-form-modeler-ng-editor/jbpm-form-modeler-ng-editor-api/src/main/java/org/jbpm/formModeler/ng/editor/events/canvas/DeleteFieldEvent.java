package org.jbpm.formModeler.ng.editor.events.canvas;

import org.jbpm.formModeler.ng.editor.events.FormModelerEvent;

public class DeleteFieldEvent extends FormModelerEvent {
    private String fieldId;

    public DeleteFieldEvent() {
    }

    public DeleteFieldEvent(String context, String fieldId) {
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
