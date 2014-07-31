package org.jbpm.formModeler.ng.editor.events.dataHolders;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.editor.events.FormModelerEvent;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderTO;

@Portable
public class DeleteDataHolderEvent extends FormModelerEvent {
    private DataHolderTO dataHolder;

    public DeleteDataHolderEvent(FormEditorContextTO context, DataHolderTO dataHolder) {
        this.context = context;
        this.dataHolder = dataHolder;
    }

    public DeleteDataHolderEvent() {
    }

    public DataHolderTO getDataHolder() {
        return dataHolder;
    }

    public void setDataHolder(DataHolderTO dataHolder) {
        this.dataHolder = dataHolder;
    }
}
