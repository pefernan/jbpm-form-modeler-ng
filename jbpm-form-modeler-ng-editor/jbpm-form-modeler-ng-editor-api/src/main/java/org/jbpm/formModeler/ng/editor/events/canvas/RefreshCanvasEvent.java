package org.jbpm.formModeler.ng.editor.events.canvas;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.editor.events.FormModelerEvent;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;

@Portable
public class RefreshCanvasEvent extends FormModelerEvent {
    public RefreshCanvasEvent(FormEditorContextTO contextTO) {
        super();
        this.context = contextTO;
    }

    public RefreshCanvasEvent() {
    }
}
