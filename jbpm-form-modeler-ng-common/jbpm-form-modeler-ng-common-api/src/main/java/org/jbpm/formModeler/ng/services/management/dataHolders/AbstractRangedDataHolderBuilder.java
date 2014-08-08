package org.jbpm.formModeler.ng.services.management.dataHolders;

public abstract class AbstractRangedDataHolderBuilder implements RangedDataHolderBuilder {
    @Override
    public boolean needsConfig() {
        return true;
    }
}
