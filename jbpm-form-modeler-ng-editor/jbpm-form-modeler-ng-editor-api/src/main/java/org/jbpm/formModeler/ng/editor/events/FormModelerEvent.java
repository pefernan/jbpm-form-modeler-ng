package org.jbpm.formModeler.ng.editor.events;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class FormModelerEvent {
    protected String context;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
