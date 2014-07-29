package org.jbpm.formModeler.ng.editor.events.dataHolders;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.editor.events.FormModelerEvent;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;

@Portable
public class RefreshHoldersListEvent extends FormModelerEvent {

    public RefreshHoldersListEvent() {
        super();
    }

    public RefreshHoldersListEvent(FormEditorContextTO context) {
        this.context = context;
    }
}
