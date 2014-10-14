package org.jbpm.formModeler.ng.editor.client.editor.modeler;

import com.github.gwtbootstrap.client.ui.*;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Image;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
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
import org.jbpm.formModeler.ng.editor.client.editor.modeler.fieldsTree.FieldsTreeEditor;
import org.jbpm.formModeler.ng.editor.client.resources.FormModelerEditorResources;
import org.jbpm.formModeler.ng.editor.client.resources.i18n.Constants;
import org.jbpm.formModeler.ng.editor.client.resources.images.FormModelerEditorImageResources;
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

    private Constants constants = Constants.INSTANCE;

    private FormModelerEditorImageResources images = FormModelerEditorResources.INSTANCE.getEditorImages();

    @Inject
    private Caller<FormEditorService> editorService;

    @Inject
    private FormRendererComponent rendererComponent;

    @Inject
    private DataHoldersEditor holdersEditor;

    @UiField
    Navbar header;

    @UiField
    NavText layoutText;

    @UiField
    ButtonGroup layoutButtons;

    @UiField
    NavText labelText;

    @UiField
    ButtonGroup labelPositionButtons;

    @UiField
    SimplePanel fieldsContainer;

    @Inject
    private FieldsTreeEditor fieldsTreeEditor;

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

        fieldsTreeEditor.initEditor(context);
        canvas.initContext(context);
        header.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);
        layoutText.setText(constants.form_modeler_layout());
        labelText.setText(constants.form_modeler_label_position());
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
        Button result = new Button();
        result.add(new Image(getImageForLayout(layout)));
        if (layout.equals(definition.getLayout().getId())) result.setEnabled(false);
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

    protected ImageResource getImageForLayout(String layout) {
        ImageResource resource;

        if (layout.equals("columns")) resource = images.columnsLayout();
        else resource = images.defaultLayout();

        return resource;
    }

    protected void initLabelPositionButtons() {
        labelPositionButtons.clear();
        FormDefinition definition = canvas.getFormDefinition();
        labelPositionButtons.add(getLabelPositionButton(FormLayoutRenderer.LABEL_MODE_DEFAULT, definition));
        labelPositionButtons.add(getLabelPositionButton(FormLayoutRenderer.LABEL_MODE_LEFT, definition));
        labelPositionButtons.add(getLabelPositionButton(FormLayoutRenderer.LABEL_MODE_LEFT_ALIGNED, definition));
    }

    protected Button getLabelPositionButton(final String labelPosition, final FormDefinition definition) {
        Button result = new Button();
        result.add(new Image(getImageForLabel(labelPosition)));
        if (labelPosition.equals(definition.getLabelMode())) result.setEnabled(false);
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

    protected ImageResource getImageForLabel(String labelPosition) {
        ImageResource resource;

        if (labelPosition.equals(FormLayoutRenderer.LABEL_MODE_LEFT)) resource = images.labelLeft();
        else if (labelPosition.equals(FormLayoutRenderer.LABEL_MODE_LEFT_ALIGNED)) resource = images.labelLeftAligned();
        else resource = images.labelDefault();

        return resource;
    }

    @PostConstruct
    public void initView() {
        initWidget(uiBinder.createAndBindUi(this));
        fieldsContainer.add(fieldsTreeEditor);
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
                                           fieldsTreeEditor.loadFormSources();
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
