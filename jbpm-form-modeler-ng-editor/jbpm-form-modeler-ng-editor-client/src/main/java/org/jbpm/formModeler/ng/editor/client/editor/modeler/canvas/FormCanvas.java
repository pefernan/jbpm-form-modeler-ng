package org.jbpm.formModeler.ng.editor.client.editor.modeler.canvas;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import org.jbpm.formModeler.ng.common.client.renderer.AbstractFormRendererComponent;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormDefinition;
import org.jbpm.formModeler.ng.editor.events.canvas.RefreshCanvasEvent;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;

public class FormCanvas extends AbstractFormRendererComponent {

    interface CanvasViewBinder
            extends
            UiBinder<Widget, FormCanvas> {

    }

    private static CanvasViewBinder uiBinder = GWT.create(CanvasViewBinder.class);

    @UiField
    FormPanel formContent;

    private FormEditorContextTO ctx;

    @PostConstruct
    public void initCanvas() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void initContext(FormEditorContextTO context) {
        this.ctx = context;

        renderFormContent(context.getMarshalledContext());
    }

    public void setFormLayoutRenderer(FormContext ctx) {
        renderer = formRendererManager.getLayoutRendererByType("editor-" + ctx.getFormDefinition().getLayout().getId());
    }

    public void refreshContext(String marshalledContext) {
        renderFormContent(marshalledContext);
    }

    protected void refreshContext(@Observes RefreshCanvasEvent event) {
        if (ctx != null && event.getContext().equals(ctx.getCtxUID())) {
            renderFormContent(event.getMarshalledContext());
        }
    }

    public FormDefinition getFormDefinition() {
        return context.getFormDefinition();
    }

    @Override
    protected Panel getFormContainer() {
        return formContent;
    }
}
