package org.jbpm.formModeler.ng.editor.client.editor.canvas;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import org.jbpm.formModeler.ng.common.client.rendering.FormDescription;
import org.jbpm.formModeler.ng.common.client.rendering.FormRendererManager;
import org.jbpm.formModeler.ng.common.client.rendering.renderers.FormRenderer;
import org.jbpm.formModeler.ng.editor.client.editor.rendering.Editor;
import org.jbpm.formModeler.ng.editor.client.editor.rendering.EditorFormRendererManager;
import org.jbpm.formModeler.ng.editor.events.canvas.RefreshCanvasEvent;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

public class FormCanvas extends Composite {
    interface CanvasViewBinder
            extends
            UiBinder<Widget, FormCanvas> {

    }

    private static CanvasViewBinder uiBinder = GWT.create(CanvasViewBinder.class);

    @UiField
    FormPanel formContent;

    @Inject
    private EditorFormRendererManager formRendererManager;

    private FormDescription description;
    private FormEditorContextTO context;

    @PostConstruct
    public void initView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void initContext(FormEditorContextTO context) {
        this.context = context;

        formContent.clear();

        description = JsonUtils.safeEval(context.getMarshalledContext());

        FormRenderer renderer = formRendererManager.getRendererByType(description.getDisplayMode());

        Panel content = renderer.generateForm(description);

        if (content != null) {
            formContent.add(content);
        }
    }

    protected void refreshContext(@Observes RefreshCanvasEvent event) {
        if (context != null && context.getCtxUID().equals(event.getContext().getCtxUID())) initContext(event.getContext());
    }
}
