package org.jbpm.formModeler.ng.editor.client.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jbpm.formModeler.ng.editor.client.editor.dataHolders.DataHoldersEditor;
import org.jbpm.formModeler.ng.editor.client.editor.sources.FieldsBySourceEditor;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class FormEditorViewImpl extends Composite implements FormEditorView {

    interface FormEditorViewBinder
            extends
            UiBinder<Widget, FormEditorViewImpl> {

    }

    @UiField
    TabLayoutPanel tabPanel;

    @Inject
    private DataHoldersEditor holdersEditor;

    @Inject
    private FieldsBySourceEditor fieldsBySourceEditor;

    private FormEditorPanelPresenter presenter;

    private FormEditorContextTO context;

    private static FormEditorViewBinder uiBinder = GWT.create(FormEditorViewBinder.class);

    @Override
    public void init(FormEditorPanelPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setContext(FormEditorContextTO ctx) {
        this.context = ctx;

        holdersEditor.initEditor(context);
        fieldsBySourceEditor.initEditor(context);
    }

    @PostConstruct
    public void initView() {
        initWidget(uiBinder.createAndBindUi(this));
        tabPanel.setHeight("600px");
        tabPanel.add(holdersEditor, "DATA_HOLDERS");
        tabPanel.add(fieldsBySourceEditor, "FIELD_BY_SOURCES");
        tabPanel.selectTab(0);
    }
}
