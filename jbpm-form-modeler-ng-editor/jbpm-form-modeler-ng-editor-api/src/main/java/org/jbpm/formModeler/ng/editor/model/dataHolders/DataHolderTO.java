package org.jbpm.formModeler.ng.editor.model.dataHolders;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.uberfire.paging.AbstractPageRow;


@Portable
public class DataHolderTO extends AbstractPageRow {
    private String type;
    private String uniqueId;
    private String className;
    private String renderColor;
    private boolean canHaveChild;
    private DataHolderFieldTO[] fields;

    public DataHolderTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getRenderColor() {
        return renderColor;
    }

    public void setRenderColor(String renderColor) {
        this.renderColor = renderColor;
    }

    public boolean isCanHaveChild() {
        return canHaveChild;
    }

    public void setCanHaveChild(boolean canHaveChild) {
        this.canHaveChild = canHaveChild;
    }

    public DataHolderFieldTO[] getFields() {
        return fields;
    }

    public void setFields(DataHolderFieldTO[] fields) {
        this.fields = fields;
    }
}
