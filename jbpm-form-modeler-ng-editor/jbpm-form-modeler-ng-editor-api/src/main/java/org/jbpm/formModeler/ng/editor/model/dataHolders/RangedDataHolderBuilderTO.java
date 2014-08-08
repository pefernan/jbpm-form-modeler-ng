package org.jbpm.formModeler.ng.editor.model.dataHolders;

import org.jboss.errai.common.client.api.annotations.Portable;

import java.util.Map;

@Portable
public class RangedDataHolderBuilderTO extends DataHolderBuilderTO {
    private Map<String, String> values;

    public RangedDataHolderBuilderTO() {
        super();
    }

    public RangedDataHolderBuilderTO(String type, String label, boolean needsConfig, Map<String, String> values) {
        super(type, label, needsConfig);
        this.values = values;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }
}
