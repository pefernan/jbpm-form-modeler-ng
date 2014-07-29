package org.jbpm.formModeler.ng.editor.events;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;

@Portable
public class FormModelerEvent {
    protected FormEditorContextTO context;

    public FormEditorContextTO getContext() {
        return context;
    }

    public void setContext(FormEditorContextTO context) {
        this.context = context;
    }
}
