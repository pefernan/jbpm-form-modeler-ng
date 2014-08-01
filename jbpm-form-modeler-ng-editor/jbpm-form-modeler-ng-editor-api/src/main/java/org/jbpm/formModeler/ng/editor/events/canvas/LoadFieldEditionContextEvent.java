package org.jbpm.formModeler.ng.editor.events.canvas;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.editor.events.FormModelerEvent;

@Portable
public class LoadFieldEditionContextEvent extends FormModelerEvent {
    private String editionContext;
    private String marshalledContext;

    public LoadFieldEditionContextEvent() {
    }

    public LoadFieldEditionContextEvent(String context, String editionContext, String marshalledContext) {
        this.context = context;
        this.editionContext = editionContext;
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
}
