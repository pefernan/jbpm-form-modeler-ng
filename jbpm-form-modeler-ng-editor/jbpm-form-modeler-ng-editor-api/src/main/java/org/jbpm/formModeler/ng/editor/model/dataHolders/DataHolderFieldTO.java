package org.jbpm.formModeler.ng.editor.model.dataHolders;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class DataHolderFieldTO {
    private String icon;
    private String holderId;
    private String id;
    private String className;

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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
