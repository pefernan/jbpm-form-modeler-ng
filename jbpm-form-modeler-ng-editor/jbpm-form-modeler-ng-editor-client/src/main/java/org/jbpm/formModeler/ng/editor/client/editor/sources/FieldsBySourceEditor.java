package org.jbpm.formModeler.ng.editor.client.editor.sources;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jbpm.formModeler.ng.editor.client.editor.canvas.FormCanvas;
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

    @UiField
    SimplePanel canvasContainer;

    @Inject
    private FormCanvas formCanvas;

    private FormEditorContextTO context;

    @PostConstruct
    public void initView() {
        initWidget( uiBinder.createAndBindUi( this ) );
        canvasContainer.add(formCanvas);
    }

    public void initEditor(FormEditorContextTO ctx) {
        this.context = ctx;
        loadFormSources();
        formCanvas.initContext(ctx);
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
                SafeHtmlBuilder htmlBuilder = new SafeHtmlBuilder();
                String html = "<div style='width: 20px; height: 20px; background-color:" + holder.getRenderColor() + ";'></div>";
                htmlBuilder.appendEscaped(holder.getInputId());
                htmlBuilder.appendHtmlConstant(html);
                currentNode = holdersTree.addItem(htmlBuilder.toSafeHtml());
            }

            for (DataHolderFieldTO field : holder.getFields()) {
                HolderFieldWidget fieldWidget = new HolderFieldWidget(field, holder.getRenderColor());
                if (currentNode != null) currentNode.addItem(fieldWidget);
                else holdersTree.add(fieldWidget);
            }
        }
    }

    private class HolderFieldWidget extends Composite {
        private HolderFieldWidget(final DataHolderFieldTO fieldTO, String color) {
            HorizontalPanel horizontalPanel = new HorizontalPanel();
            horizontalPanel.add(new HTML(fieldTO.getId()));
            horizontalPanel.add(new HTML("<div style='width: 20px; height: 20px; background-color:" + color + ";'></div>"));
            Button action = new Button("add");
            action.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    editorService.call(new RemoteCallback<DataHolderTO[]>() {
                        @Override
                        public void callback(DataHolderTO[] holders) {
                            loadFormSources(holders);
                        }
                    }).addFieldFromHolder(context.getCtxUID(), fieldTO);
                }
            });
            horizontalPanel.add(action);
            initWidget(horizontalPanel);
        }
    }

    public void refreshGrid(@Observes RefreshHoldersListEvent refreshHoldersListEvent) {
        if (context != null && context.getCtxUID().equals(refreshHoldersListEvent.getContext())) loadFormSources();
    }
}
