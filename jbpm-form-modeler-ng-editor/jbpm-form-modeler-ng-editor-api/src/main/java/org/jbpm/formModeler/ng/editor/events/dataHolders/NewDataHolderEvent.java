package org.jbpm.formModeler.ng.editor.events.dataHolders;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.editor.events.FormModelerEvent;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderTO;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;

@Portable
public class NewDataHolderEvent extends FormModelerEvent {
    protected DataHolderTO dataHolder;

    public NewDataHolderEvent(FormEditorContextTO context, DataHolderTO dataHolder) {
        this.context = context;
        this.dataHolder = dataHolder;
    }

    public NewDataHolderEvent() {
        super();
    }

    public DataHolderTO getDataHolder() {
        return dataHolder;
    }

    public void setDataHolder(DataHolderTO dataHolder) {
        this.dataHolder = dataHolder;
    }
}
