package org.jbpm.formModeler.ng.editor.client.editor;

import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;
import org.uberfire.client.mvp.UberView;

public interface FormEditorView
            extends
            UberView<FormEditorPanelPresenter> {

        void setContext(FormEditorContextTO context);
}
