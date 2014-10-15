package org.jbpm.formModeler.ng.common.client.rendering.event;

public class FieldChangedEvent {
    private String ctxUID;
    private String fieldName;
    private String fieldUid;
    private String newValue;

    public FieldChangedEvent(String ctxUID, String fieldUid, String fieldName, String newValue) {
        this.ctxUID = ctxUID;
        this.fieldUid = fieldUid;
        this.fieldName = fieldName;
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

    public String getFieldUid() {
        return fieldUid;
    }

    public void setFieldUid(String fieldUid) {
        this.fieldUid = fieldUid;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}
