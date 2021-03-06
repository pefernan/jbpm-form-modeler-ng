package org.jbpm.formModeler.ng.editor.events.canvas;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.editor.events.FormModelerEvent;

@Portable
public class RefreshCanvasEvent extends FormModelerEvent {
    private String marshalledContext;

    public RefreshCanvasEvent(String context, String marshalledContext) {
        this.context = context;
        this.marshalledContext = marshalledContext;
    }

    public RefreshCanvasEvent() {
    }

    public String getMarshalledContext() {
        return marshalledContext;
    }

    public void setMarshalledContext(String marshalledContext) {
        this.marshalledContext = marshalledContext;
    }
}
