package org.jbpm.formModeler.ng.editor.model.dataHolders;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class RangedDataHolderBuilderTO extends DataHolderBuilderTO {
    private String[] values;

    public RangedDataHolderBuilderTO() {
        super();
    }

    public RangedDataHolderBuilderTO(String type, String label, String[] values) {
        super(type, label);
        this.values = values;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }
}
