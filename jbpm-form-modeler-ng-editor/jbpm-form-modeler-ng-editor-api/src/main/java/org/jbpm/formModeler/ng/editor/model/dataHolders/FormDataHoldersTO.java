package org.jbpm.formModeler.ng.editor.model.dataHolders;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class FormDataHoldersTO {
    private DataHolderTO[] complexHolders;
    private DataHolderTO[] simpleHolders;

    public DataHolderTO[] getComplexHolders() {
        return complexHolders;
    }

    public void setComplexHolders(DataHolderTO[] complexHolders) {
        this.complexHolders = complexHolders;
    }

    public DataHolderTO[] getSimpleHolders() {
        return simpleHolders;
    }

    public void setSimpleHolders(DataHolderTO[] simpleHolders) {
        this.simpleHolders = simpleHolders;
    }
}
