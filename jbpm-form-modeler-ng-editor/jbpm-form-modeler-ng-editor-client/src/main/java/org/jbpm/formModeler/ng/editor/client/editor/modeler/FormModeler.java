package org.jbpm.formModeler.ng.editor.client.editor.modeler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jbpm.formModeler.ng.common.client.renderer.FormRendererComponent;
import org.jbpm.formModeler.ng.common.client.rendering.event.FieldChangedEvent;
import org.jbpm.formModeler.ng.editor.client.editor.dataHolders.DataHoldersEditor;
import org.jbpm.formModeler.ng.editor.client.editor.modeler.canvas.FormCanvas;
import org.jbpm.formModeler.ng.editor.client.editor.modeler.sources.FieldsBySourceEditor;
import org.jbpm.formModeler.ng.editor.events.canvas.DeleteFieldEvent;
import org.jbpm.formModeler.ng.editor.events.canvas.RefreshCanvasEvent;
import org.jbpm.formModeler.ng.editor.events.canvas.StartEditFieldPropertyEvent;
import org.jbpm.formModeler.ng.editor.events.dataHolders.RefreshHoldersListEvent;
import org.jbpm.formModeler.ng.editor.model.EditionContextTO;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;
import org.jbpm.formModeler.ng.editor.service.FormEditorService;
import org.uberfire.mvp.Command;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

public class FormModeler extends Composite {

    interface FormModelerViewBinder
            extends
            UiBinder<Widget, FormModeler> {

    }

    @Inject
    private Caller<FormEditorService> editorService;

    @Inject
    private FormRendererComponent rendererComponent;

    @Inject
    private DataHoldersEditor holdersEditor;

    @UiField
    SimplePanel fieldsContainer;

    @Inject
    private FieldsBySourceEditor fieldsBySourceEditor;

    @UiField
    SimplePanel canvasContainer;

    @Inject
    private FormCanvas canvas;

    @UiField
    SimplePanel propertiesContainer;

    private FormEditorContextTO context;

    private String editionCtxUID = null;
    private String editedField = null;

    private static FormModelerViewBinder uiBinder = GWT.create(FormModelerViewBinder.class);

    public void initEditor(FormEditorContextTO ctx) {
        this.context = ctx;

        fieldsBySourceEditor.initEditor(context);
        canvas.initContext(context);
    }

    @PostConstruct
    public void initView() {
        initWidget(uiBinder.createAndBindUi(this));
        fieldsContainer.add(fieldsBySourceEditor);
        canvasContainer.add(canvas);
    }

    public void editField(@Observes final StartEditFieldPropertyEvent event) {
        if (context.getCtxUID().equals(event.getContext())) {
            if (editionCtxUID != null) {
                propertiesContainer.clear();
                updateFieldContext(editionCtxUID, rendererComponent.getFormValues(), false);
            }
            editorService.call(new RemoteCallback<EditionContextTO>() {
                @Override
                public void callback(final EditionContextTO response) {
                    editedField = event.getFieldUid();
                    loadEditionForm(response);
                }
            }).startFieldEdition(event.getContext(), event.getFieldUid());
        }
    }

    public void deleteField(@Observes final DeleteFieldEvent event) {
        if (context.getCtxUID().equals(event.getContext())) {
            editorService.call(new RemoteCallback<String>() {
                                   @Override
                                   public void callback(String jsonResponse) {
                                       if (editionCtxUID != null && event.getFieldUid().equals(editedField)) {
                                           propertiesContainer.clear();
                                           updateFieldContext(editionCtxUID, rendererComponent.getFormValues(), false);
                                           editionCtxUID = null;
                                           editedField = null;
                                       }
                                       if (jsonResponse != null) {
                                           canvas.refreshContext(jsonResponse);
                                           fieldsBySourceEditor.loadFormSources();
                                       }
                                   }
                               }, new ErrorCallback<Object>() {
                                   @Override
                                   public boolean error(Object message, Throwable throwable) {
                                       Window.alert("Something wong happened: " + message);
                                       return false;
                                   }
                               }
            ).removeFieldFromForm(context.getCtxUID(), Long.decode(event.getFieldUid()));
        }
    }

    protected void updateFieldContext(String editionCtxUID, String marshalledCtx, boolean persist) {
        editorService.call(new RemoteCallback<String>() {
            @Override
            public void callback(String response) {
                if (response != null) canvas.refreshContext(response);
            }
        }).editFieldValue(context.getCtxUID(), editionCtxUID, marshalledCtx, persist);
    }

    protected void loadEditionForm(EditionContextTO contextTO) {
        editionCtxUID = contextTO.getEditionContext();
        rendererComponent.renderFormContent(contextTO.getMarshalledContext());
        propertiesContainer.add(rendererComponent);
        rendererComponent.setOnFieldChange(new Command() {
            @Override
            public void execute() {
                FieldChangedEvent event = rendererComponent.getFieldChangedEvent();
                if (event != null) {
                    if (event.getFieldId().equals("code")) {
                        editorService.call(new RemoteCallback<EditionContextTO>() {
                            @Override
                            public void callback(EditionContextTO response) {
                                propertiesContainer.clear();
                                updateFieldContext(editionCtxUID, rendererComponent.getFormValues(), false);
                                loadEditionForm(response);
                            }
                        }).changeFieldType(event.getCtxUID(), event.getFieldId(), event.getNewValue(), context.getCtxUID());
                    } else {
                        updateFieldContext(editionCtxUID, rendererComponent.getFormValues(), true);
                    }
                }
            }
        });
    }
}
