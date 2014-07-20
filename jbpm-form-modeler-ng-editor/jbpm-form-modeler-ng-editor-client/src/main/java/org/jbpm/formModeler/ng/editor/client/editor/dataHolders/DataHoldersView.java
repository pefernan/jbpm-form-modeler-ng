package org.jbpm.formModeler.ng.editor.client.editor.dataHolders;


import com.google.gwt.view.client.HasData;
import org.jbpm.formModeler.ng.editor.client.editor.FormEditorPanelPresenter;
import org.jbpm.formModeler.ng.editor.model.DataHolderPageRow;
import org.uberfire.client.mvp.UberView;

public interface DataHoldersView extends UberView<FormEditorPanelPresenter> {
    HasData<DataHolderPageRow> getDataHoldersGrid();
}
