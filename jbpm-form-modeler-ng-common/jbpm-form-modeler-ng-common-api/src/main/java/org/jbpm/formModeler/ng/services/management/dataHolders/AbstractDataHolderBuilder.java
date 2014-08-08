package org.jbpm.formModeler.ng.services.management.dataHolders;

public abstract class AbstractDataHolderBuilder implements DataHolderBuilder {
    @Override
    public boolean needsConfig() {
        return true;
    }
}
