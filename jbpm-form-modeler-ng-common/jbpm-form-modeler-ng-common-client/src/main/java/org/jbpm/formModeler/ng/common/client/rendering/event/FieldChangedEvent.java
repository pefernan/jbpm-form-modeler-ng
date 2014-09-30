package org.jbpm.formModeler.ng.common.client.rendering.event;

public class FieldChangedEvent {
    private String ctxUID;
    private String fieldId;
    private String newValue;

    public FieldChangedEvent(String ctxUID, String fieldId, String newValue) {
        this.ctxUID = ctxUID;
        this.fieldId = fieldId;
        this.newValue = newValue;
    }

    public FieldChangedEvent() {

    }

    public String getCtxUID() {
        return ctxUID;
    }

    public void setCtxUID(String ctxUID) {
        this.ctxUID = ctxUID;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}
