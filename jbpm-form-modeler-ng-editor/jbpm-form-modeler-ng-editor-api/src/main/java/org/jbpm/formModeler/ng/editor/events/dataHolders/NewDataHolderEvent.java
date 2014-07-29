package org.jbpm.formModeler.ng.editor.events.dataHolders;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.editor.events.FormModelerEvent;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderInfo;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;

@Portable
public class NewDataHolderEvent extends FormModelerEvent {
    protected DataHolderInfo dataHolder;

    public NewDataHolderEvent(FormEditorContextTO context, DataHolderInfo dataHolder) {
        this.context = context;
        this.dataHolder = dataHolder;
    }

    public NewDataHolderEvent() {
        super();
    }

    public DataHolderInfo getDataHolder() {
        return dataHolder;
    }

    public void setDataHolder(DataHolderInfo dataHolder) {
        this.dataHolder = dataHolder;
    }
}
