package org.jbpm.formModeler.ng.editor.client.editor.modeler;

import com.github.gwtbootstrap.client.ui.*;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jbpm.formModeler.ng.common.client.renderer.FormRendererComponent;
import org.jbpm.formModeler.ng.common.client.rendering.event.FieldChangedEvent;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.layouts.FormLayoutRenderer;
import org.jbpm.formModeler.ng.editor.client.editor.dataHolders.DataHoldersEditor;
import org.jbpm.formModeler.ng.editor.client.editor.modeler.canvas.FormCanvas;
import org.jbpm.formModeler.ng.editor.client.editor.modeler.sources.FieldsBySourceEditor;
import org.jbpm.formModeler.ng.editor.events.canvas.DeleteFieldEvent;
import org.jbpm.formModeler.ng.editor.events.canvas.StartEditFieldPropertyEvent;
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
    Navbar header;

    @UiField
    ButtonGroup layoutButtons;

    @UiField
    ButtonGroup labelPositionButtons;

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
        header.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        initLayoutButtons();
        initLabelPositionButtons();
    }

    protected void initLayoutButtons() {
        layoutButtons.clear();
        FormDefinition definition = canvas.getFormDefinition();

        layoutButtons.add(getLayoutButton("default",  definition));
        layoutButtons.add(getLayoutButton("columns",  definition));
    }

    protected Button getLayoutButton(final String layout, final FormDefinition definition) {
        Button result = new Button(layout);
        if (layout.equals(definition.getLayout().getId())) result.setType(ButtonType.INVERSE);
        else {
            result.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (Window.confirm("Are you sure??")) {
                        editorService.call(new RemoteCallback<String>() {
                            @Override
                            public void callback(String response) {
                                if (response != null) {
                                    canvas.refreshContext(response);
                                    initLayoutButtons();
                                }
                            }
                        }).changeFormLayout(context.getCtxUID(), layout);
                    }
                }
            });
        }
        return result;
    }

    protected void initLabelPositionButtons() {
        labelPositionButtons.clear();
        FormDefinition definition = canvas.getFormDefinition();
        labelPositionButtons.add(getLabelPositionButton(FormLayoutRenderer.LABEL_MODE_BEFORE, definition));
        labelPositionButtons.add(getLabelPositionButton(FormLayoutRenderer.LABEL_MODE_LEFT, definition));
        labelPositionButtons.add(getLabelPositionButton(FormLayoutRenderer.LABEL_MODE_LEFT_ALIGNED, definition));
    }

    protected Button getLabelPositionButton(final String labelPosition, final FormDefinition definition) {
        Button result = new Button(labelPosition);
        if (labelPosition.equals(definition.getLabelMode())) result.setType(ButtonType.INVERSE);
        else {
            result.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    editorService.call(new RemoteCallback<String>() {
                        @Override
                        public void callback(String response) {
                            if (response != null) {
                                canvas.refreshContext(response);
                                initLabelPositionButtons();
                            }
                        }
                    }).changeFormLabelPosition(context.getCtxUID(), labelPosition);
                }
            });
        }
        return result;
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
        if (contextTO == null) return;
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
