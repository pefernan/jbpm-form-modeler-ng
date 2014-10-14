package org.jbpm.formModeler.ng.editor.model.dataHolders;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class DataHolderFieldTO {
    private String icon;
    private String holderId;
    private String id;
    private String typeCode;
    private boolean binded = false;
    private String bindedFieldId;

    public String getHolderId() {
        return holderId;
    }

    public void setHolderId(String holderId) {
        this.holderId = holderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isBinded() {
        return binded;
    }

    public void setBinded(boolean binded) {
        this.binded = binded;
    }

    public void setBindedFieldId(String bindedFieldId) {
        this.bindedFieldId = bindedFieldId;
    }

    public String getBindedFieldId() {
        return bindedFieldId;
    }
}
