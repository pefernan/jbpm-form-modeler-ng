package org.jbpm.formModeler.ng.editor.model;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class EditionContextTO {
    private String editionContext;
    private String marshalledContext;

    public EditionContextTO() {
    }

    public EditionContextTO(String editionContext, String marshalledContext) {
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
