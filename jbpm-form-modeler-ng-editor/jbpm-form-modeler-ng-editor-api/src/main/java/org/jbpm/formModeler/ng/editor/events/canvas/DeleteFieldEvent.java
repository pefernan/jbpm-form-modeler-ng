package org.jbpm.formModeler.ng.editor.events.canvas;

import org.jbpm.formModeler.ng.editor.events.FormModelerEvent;

public class DeleteFieldEvent extends FormModelerEvent {
    private String fieldUid;

    public DeleteFieldEvent() {
    }

    public DeleteFieldEvent(String context, String fieldUid) {
        this.context = context;
        this.fieldUid = fieldUid;
    }

    public String getFieldUid() {
        return fieldUid;
    }

    public void setFieldUid(String fieldUid) {
        this.fieldUid = fieldUid;
    }
}
