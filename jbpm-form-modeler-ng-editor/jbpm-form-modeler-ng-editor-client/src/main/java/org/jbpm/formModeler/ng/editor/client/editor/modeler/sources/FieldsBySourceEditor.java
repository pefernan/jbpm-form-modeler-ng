package org.jbpm.formModeler.ng.editor.client.editor.modeler.sources;


import com.github.gwtbootstrap.client.ui.base.IconAnchor;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jbpm.formModeler.ng.editor.events.dataHolders.RefreshHoldersListEvent;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderFieldTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderTO;
import org.jbpm.formModeler.ng.editor.service.FormEditorService;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

public class FieldsBySourceEditor extends Composite {

    interface SourcesViewBinder
            extends
            UiBinder<Widget, FieldsBySourceEditor> {

    }

    @Inject
    private Caller<FormEditorService> editorService;

    private static SourcesViewBinder uiBinder = GWT.create(SourcesViewBinder.class);

    @UiField
    SimplePanel treeContainer;

    private FormEditorContextTO context;

    @PostConstruct
    public void initView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void initEditor(FormEditorContextTO ctx) {
        this.context = ctx;
        loadFormSources();
    }

    protected void loadFormSources() {
        editorService.call(new RemoteCallback<DataHolderTO[]>() {
            @Override
            public void callback(DataHolderTO[] dataHolderTOs) {
                loadFormSources(dataHolderTOs);
            }
        }).getAvailableDataHolders(context.getCtxUID());
    }

    protected void loadFormSources(DataHolderTO[] holders) {
        treeContainer.clear();
        Tree holdersTree = new Tree();
        treeContainer.add(holdersTree);

        for (DataHolderTO holder : holders) {
            TreeItem currentNode = null;
            if (holder.isCanHaveChild()) {
                currentNode = holdersTree.addItem(getSourceAddWidget(holder.getUniqueId(), holder.getRenderColor(), null));
            }

            for (final DataHolderFieldTO field : holder.getFields()) {

                ClickHandler handler = new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        editorService.call(new RemoteCallback<DataHolderTO[]>() {
                            @Override
                            public void callback(DataHolderTO[] holders) {
                                loadFormSources(holders);
                            }
                        }).addFieldFromHolder(context.getCtxUID(), field);
                    }
                };

                if (currentNode != null) currentNode.addItem(getSourceAddWidget(field.getId(), holder.getRenderColor(), handler));
                else holdersTree.add(getSourceAddWidget(field.getId(), holder.getRenderColor(), handler));
            }
        }
    }

    protected Widget getSourceAddWidget(String label, String renderColor, ClickHandler handler) {
        HorizontalPanel fieldWidget = new HorizontalPanel();
        fieldWidget.setWidth("100%");

        fieldWidget.add(new HTML(label));

        SimplePanel color = new SimplePanel();
        color.setWidth("20px");
        color.setHeight("20px");
        color.getElement().getStyle().setBackgroundColor(renderColor);

        SimplePanel colorPanel = new SimplePanel();
        colorPanel.add(color);
        colorPanel.getElement().getStyle().setPadding(3, Style.Unit.PX);

        fieldWidget.add(colorPanel);
        fieldWidget.setCellWidth(colorPanel, "20px");

        if (handler != null) {
            IconAnchor action = new IconAnchor();
            action.setIcon(IconType.PLAY);
            action.addClickHandler(handler);
            action.getElement().getStyle().setPadding(3, Style.Unit.PX);

            fieldWidget.add(action);
            fieldWidget.setCellWidth(action, "20px");
        }
        return fieldWidget;
    }

    public void refreshGrid(@Observes RefreshHoldersListEvent refreshHoldersListEvent) {
        if (context != null && context.getCtxUID().equals(refreshHoldersListEvent.getContext())) loadFormSources();
    }
}
