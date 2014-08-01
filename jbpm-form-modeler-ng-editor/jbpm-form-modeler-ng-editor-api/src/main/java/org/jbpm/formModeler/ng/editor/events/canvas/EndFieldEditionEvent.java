package org.jbpm.formModeler.ng.editor.events.canvas;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.editor.events.FormModelerEvent;

@Portable
public class EndFieldEditionEvent extends FormModelerEvent {
    private String editionContext;
    private String marshalledContext;
    private boolean persist;

    public EndFieldEditionEvent() {
    }

    public EndFieldEditionEvent(String context, String editionContext, boolean persist, String marshalledContext) {
        this.context = context;
        this.editionContext = editionContext;
        this.persist = persist;
        this.marshalledContext = marshalledContext;
    }

    public String getEditionContext() {
        return editionContext;
    }

    public void setEditionContext(String editionContext) {
        this.editionContext = editionContext;
    }

    public String getMarshalledContext() {
        return marshalledContext;
    }

    public void setMarshalledContext(String marshalledContext) {
        this.marshalledContext = marshalledContext;
    }

    public boolean isPersist() {
        return persist;
    }

    public void setPersist(boolean persist) {
        this.persist = persist;
    }
}
