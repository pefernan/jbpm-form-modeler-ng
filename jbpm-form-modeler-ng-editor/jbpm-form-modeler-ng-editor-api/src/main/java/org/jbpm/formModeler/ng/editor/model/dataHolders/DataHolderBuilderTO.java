package org.jbpm.formModeler.ng.editor.model.dataHolders;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class DataHolderBuilderTO {
    private String type;
    private String label;
    private Boolean needsConfig;

    public DataHolderBuilderTO() {
    }

    public DataHolderBuilderTO(String type, String label, boolean needsConfig) {
        this.type = type;
        this.label = label;
        this.needsConfig = needsConfig;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getNeedsConfig() {
        return needsConfig;
    }

    public void setNeedsConfig(Boolean needsConfig) {
        this.needsConfig = needsConfig;
    }
}
