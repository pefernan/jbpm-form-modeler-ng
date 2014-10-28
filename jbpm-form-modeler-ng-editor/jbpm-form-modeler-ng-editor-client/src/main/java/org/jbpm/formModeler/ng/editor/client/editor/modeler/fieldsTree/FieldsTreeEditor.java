package org.jbpm.formModeler.ng.editor.client.editor.modeler.fieldsTree;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jbpm.formModeler.ng.common.client.rendering.FieldProviderManager;
import org.jbpm.formModeler.ng.common.client.rendering.fields.FieldRenderer;
import org.jbpm.formModeler.ng.editor.client.resources.FormModelerEditorResources;
import org.jbpm.formModeler.ng.editor.client.resources.i18n.Constants;
import org.jbpm.formModeler.ng.editor.client.resources.images.tree.FieldsTreeResources;
import org.jbpm.formModeler.ng.editor.events.canvas.RefreshCanvasEvent;
import org.jbpm.formModeler.ng.editor.events.canvas.StartEditFieldPropertyEvent;
import org.jbpm.formModeler.ng.editor.events.dataHolders.RefreshHoldersListEvent;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderFieldTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.FormDataHoldersTO;
import org.jbpm.formModeler.ng.editor.service.FormEditorService;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class FieldsTreeEditor extends Composite {

    interface SourcesViewBinder
            extends
            UiBinder<Widget, FieldsTreeEditor> {

    }

    @Inject
    private Event<StartEditFieldPropertyEvent> fieldPropertyEvent;

    @Inject
    private Event<RefreshCanvasEvent> refreshCanvasEvent;

    @Inject
    private Caller<FormEditorService> editorService;

    @Inject
    private FieldProviderManager fieldProviderManager;

    private static SourcesViewBinder uiBinder = GWT.create(SourcesViewBinder.class);

    @UiField
    SimplePanel treeContainer;

    private FormEditorContextTO context;

    private Constants constants = Constants.INSTANCE;

    private FieldsTreeResources treeResources = FormModelerEditorResources.INSTANCE.getTreeResources();

    private DataHoldersTree paletteTree = new DataHoldersTree(treeResources);

    private DataHoldersTree fieldSourcesTree = new DataHoldersTree(treeResources);

    private DataHoldersTree fullTree = new DataHoldersTree(treeResources);

    String currentField = "";
    private Map<String, Panel> bindedFieldsPanels = new HashMap<String, Panel>();

    @PostConstruct
    public void initView() {
        initWidget(uiBinder.createAndBindUi(this));
        TreeItem palette = fullTree.addItem(new SafeHtml() {
            @Override
            public String asString() {
                return constants.form_modeler_fields_palette();
            }
        });
        palette.addItem(paletteTree);

        TreeItem sources = fullTree.addItem(new SafeHtml() {
            @Override
            public String asString() {
                return constants.form_modeler_sources();
            }
        });
        sources.addItem(fieldSourcesTree);
        loadVisibleFields();
        treeContainer.add(fullTree);
    }

    public void initEditor(FormEditorContextTO ctx) {
        this.context = ctx;
        loadFormSources();
    }

    public void loadFormSources() {
        editorService.call(new RemoteCallback<FormDataHoldersTO>() {
            @Override
            public void callback(FormDataHoldersTO formHolders) {
                loadFormSources(formHolders);
            }
        }).getAvailableDataHolders(context.getCtxUID());
    }

    protected void loadFormSources(FormDataHoldersTO formHolders) {
        bindedFieldsPanels.clear();
        fieldSourcesTree.clear();

        loadChildElements(formHolders.getComplexHolders(), null);

        TreeItem unrelated = null;
        if (fieldSourcesTree.getItemCount() > 0) {
            unrelated = new TreeItem(new SafeHtml() {
                @Override
                public String asString() {
                    return "< " +constants.form_modeler_unrelated_sources() + " >";
                }
            });
            fieldSourcesTree.addItem(unrelated);
        }

        loadChildElements(formHolders.getSimpleHolders(), unrelated);

    }

    protected void loadChildElements(DataHolderTO[] holders, TreeItem forcedNode) {
        TreeItem currentNode = null;

        if (forcedNode != null) currentNode = forcedNode;

        for (DataHolderTO holder : holders) {
            if (holder.isCanHaveChild()) {
                currentNode = fieldSourcesTree.addItem(getSourceAddWidget(holder.getUniqueId(), null));
            }

            for (final DataHolderFieldTO field : holder.getFields()) {
                Panel item;
                if (field.isBinded()) {
                    item = getBindedWidget(field, holder);
                    bindedFieldsPanels.put(field.getBindedFieldId(), item);
                }
                else item = getSourceAddWidget(field, holder);


                if (currentNode != null) currentNode.addItem(item);
                else fieldSourcesTree.add(item);
            }
        }
    }

    protected Panel getBindedWidget(final DataHolderFieldTO field, final DataHolderTO holder) {
        ClickHandler handler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                fieldPropertyEvent.fire(new StartEditFieldPropertyEvent(context.getCtxUID(), field.getBindedFieldId()));
            }
        };

        Panel element =  getSourceToAdd(field, holder, handler, treeResources.editField());
        element.addStyleName("bindedElementOff");

        return element;
    }

    protected Panel getSourceAddWidget(final DataHolderFieldTO field, final DataHolderTO holder) {
        ClickHandler handler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                editorService.call(new RemoteCallback<FormDataHoldersTO>() {
                    @Override
                    public void callback(FormDataHoldersTO formHolders) {
                        loadFormSources(formHolders);
                    }
                }).addFieldFromHolder(context.getCtxUID(), field);
            }
        };
        return getSourceToAdd(field, holder, handler, treeResources.addField());
    }

    protected Panel getSourceToAdd(final DataHolderFieldTO field, final DataHolderTO holder, ClickHandler handler, ImageResource resource) {
        HorizontalPanel fieldWidget = new HorizontalPanel();
        fieldWidget.setWidth("100%");

        FieldRenderer renderer = fieldProviderManager.getProviderByType(field.getTypeCode());

        Image typeImage = new Image(renderer.getImage());
        fieldWidget.add(typeImage);
        fieldWidget.setCellWidth(typeImage, "20px");

        fieldWidget.add(new HTML(field.getId()));

        Image action = new Image(resource);
        action.addClickHandler(handler);
        action.getElement().getStyle().setPadding(3, Style.Unit.PX);
        fieldWidget.add(action);

        fieldWidget.add(action);
        fieldWidget.setCellWidth(action, "20px");
        return fieldWidget;
    }

    protected Widget getSourceAddWidget(String label, ClickHandler handler) {
        HorizontalPanel fieldWidget = new HorizontalPanel();
        fieldWidget.setWidth("100%");

        fieldWidget.add(new HTML(label));

        if (handler != null) {
            Image action = new Image(treeResources.addField());
            action.addClickHandler(handler);
            action.getElement().getStyle().setPadding(3, Style.Unit.PX);
            fieldWidget.add(action);
        }
        return fieldWidget;
    }

    protected void loadVisibleFields() {
        for (final FieldRenderer renderer : fieldProviderManager.getVisibleRenderers()) {
            HorizontalPanel availableType = new HorizontalPanel();
            availableType.setWidth("100%");

            Image typeImage = new Image(renderer.getImage());
            availableType.add(typeImage);
            availableType.setCellWidth(typeImage, "20px");

            availableType.add(new HTML(renderer.getLabel()));

            Image action = new Image(treeResources.addField());
            action.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    editorService.call(new RemoteCallback<String>() {
                        @Override
                        public void callback(String marshalledContext) {
                            if (marshalledContext != null) refreshCanvasEvent.fire(new RefreshCanvasEvent(context.getCtxUID(), marshalledContext));
                        }
                    }).addFieldFromTypeCode(context.getCtxUID(), renderer.getCode());
                }
            });
            action.getElement().getStyle().setPadding(3, Style.Unit.PX);
            availableType.add(action);

            availableType.add(action);
            availableType.setCellWidth(action, "20px");
            paletteTree.add(availableType);
        }
    }

    public void refreshGrid(@Observes RefreshHoldersListEvent refreshHoldersListEvent) {
        if (context != null && context.getCtxUID().equals(refreshHoldersListEvent.getContext())) loadFormSources();
    }

    public void unSelect(@Observes StartEditFieldPropertyEvent event) {
        if (event.getContext().equals(context.getCtxUID())) {
            if (!event.getFieldId().equals(currentField)) {
                Panel currentPanel = bindedFieldsPanels.get(currentField);
                if (currentPanel != null) {
                    currentPanel.removeStyleName("bindedElementOn");
                }
                currentPanel = bindedFieldsPanels.get(event.getFieldId());
                if (currentPanel != null) {
                    currentPanel.addStyleName("bindedElementOn");
                }
                currentField = event.getFieldId();
            }
        }
    }
}
