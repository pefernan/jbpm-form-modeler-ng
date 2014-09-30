package org.jbpm.formModeler.ng.editor.client.editor.modeler.canvas;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import org.jbpm.formModeler.ng.common.client.rendering.FormRendererManager;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.renderers.FormRenderer;
import org.jbpm.formModeler.ng.editor.events.FormModelerEvent;
import org.jbpm.formModeler.ng.editor.events.canvas.RefreshCanvasEvent;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@Dependent
public class FormCanvas extends Composite {
    interface CanvasViewBinder
            extends
            UiBinder<Widget, FormCanvas> {

    }

    private static CanvasViewBinder uiBinder = GWT.create(CanvasViewBinder.class);

    @UiField
    FormPanel formContent;

    @Inject
    private FormRendererManager formRendererManager;

    @Inject
    private Event<FormModelerEvent> modelerEvent;

    private FormContext jsonContext;

    private JSONObject values;

    private FormEditorContextTO context;

    @PostConstruct
    public void initView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void initContext(FormEditorContextTO context) {
        this.context = context;

        initContext(context.getMarshalledContext());
    }

    public void initContext(String ctxJson) {
        formContent.clear();

        jsonContext = JsonUtils.safeEval(ctxJson);

        values = new JSONObject(jsonContext.getContextStatus());

        FormRenderer renderer = formRendererManager.getRendererByType("editor-" + jsonContext.getFormDefinition().getDisplayMode());

        Panel content = renderer.generateForm(jsonContext);

        if (content != null) {
            formContent.add(content);
        }
    }

    public void refreshContext(String marshalledContext) {
        initContext(marshalledContext);
    }

    protected void refreshContext(@Observes RefreshCanvasEvent event) {
        if (context != null && event.getContext().equals(context.getCtxUID())) {
            initContext(event.getMarshalledContext());
        }
    }
}
