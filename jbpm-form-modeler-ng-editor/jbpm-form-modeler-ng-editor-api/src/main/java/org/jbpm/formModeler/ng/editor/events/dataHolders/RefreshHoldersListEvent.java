package org.jbpm.formModeler.ng.editor.events.dataHolders;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.editor.events.FormModelerEvent;

@Portable
public class RefreshHoldersListEvent extends FormModelerEvent {

    public RefreshHoldersListEvent() {
        super();
    }

    public RefreshHoldersListEvent(String context) {
        this.context = context;
    }
}
