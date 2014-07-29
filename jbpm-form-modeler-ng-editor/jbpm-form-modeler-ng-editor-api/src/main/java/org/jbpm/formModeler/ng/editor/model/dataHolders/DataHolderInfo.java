package org.jbpm.formModeler.ng.editor.model.dataHolders;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.uberfire.paging.AbstractPageRow;


@Portable
public class DataHolderInfo extends AbstractPageRow {
    private String type;
    private String uniqueId;
    private String inputId;
    private String outputId;
    private String className;
    private String renderColor;

    public DataHolderInfo() {
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

    public String getInputId() {
        return inputId;
    }

    public void setInputId(String inputId) {
        this.inputId = inputId;
    }

    public String getOutputId() {
        return outputId;
    }

    public void setOutputId(String outputId) {
        this.outputId = outputId;
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
}
