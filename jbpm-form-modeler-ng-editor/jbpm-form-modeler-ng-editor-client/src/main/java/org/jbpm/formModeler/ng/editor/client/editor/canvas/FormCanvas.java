package org.jbpm.formModeler.ng.editor.client.editor.canvas;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.event.HideEvent;
import com.github.gwtbootstrap.client.ui.event.HideHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import org.jbpm.formModeler.ng.common.client.renderer.FormRendererComponent;
import org.jbpm.formModeler.ng.common.client.rendering.FormDescription;
import org.jbpm.formModeler.ng.common.client.rendering.FormRendererManager;
import org.jbpm.formModeler.ng.common.client.rendering.renderers.FormRenderer;
import org.jbpm.formModeler.ng.editor.events.FormModelerEvent;
import org.jbpm.formModeler.ng.editor.events.canvas.EndFieldEditionEvent;
import org.jbpm.formModeler.ng.editor.events.canvas.LoadFieldEditionContextEvent;
import org.jbpm.formModeler.ng.editor.events.canvas.RefreshCanvasEvent;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
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
    private FormRendererManager formRendererManager;

    @Inject
    private FormRendererComponent rendererComponent;

    @Inject
    private Event<FormModelerEvent> modelerEvent;

    private FormDescription description;
    private FormEditorContextTO context;
    private String editionContext;

    @PostConstruct
    public void initView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void initContext(FormEditorContextTO context) {
        this.context = context;

        formContent.clear();

        description = JsonUtils.safeEval(context.getMarshalledContext());

        FormRenderer renderer = formRendererManager.getRendererByType("editor-" + description.getDisplayMode());

        Panel content = renderer.generateForm(description);

        if (content != null) {
            formContent.add(content);
        }
    }

    protected void refreshContext(@Observes RefreshCanvasEvent event) {
        if (event.getContext().equals(context.getCtxUID())) {
            context.setMarshalledContext(event.getMarshalledContext());
            initContext(context);
        }
    }

    protected void loadEditionContext(@Observes LoadFieldEditionContextEvent event) {
        if (event.getContext().equals(context.getCtxUID())) {
            editionContext = event.getEditionContext();
            rendererComponent.renderFormContent(event.getMarshalledContext());
            final Modal edition = new Modal();
            edition.addHideHandler(new HideHandler() {
                @Override
                public void onHide(HideEvent hideEvent) {
                    modelerEvent.fire(new EndFieldEditionEvent(context.getCtxUID(), editionContext, false, ""));
                    editionContext = "";
                }
            });

            VerticalPanel panel = new VerticalPanel();

            panel.add(rendererComponent);

            Button ok = new Button("!!!!Save");
            ok.setType(ButtonType.PRIMARY);
            ok.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    String formContent = rendererComponent.getFormContent();
                    modelerEvent.fire(new EndFieldEditionEvent(context.getCtxUID(), editionContext, true, formContent));
                    editionContext = "";
                    edition.hide();
                }
            });

            panel.add(ok);

            edition.add(panel);
            edition.show();
        }
    }
}
