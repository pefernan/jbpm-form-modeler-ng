package org.jbpm.formModeler.ng.editor.client.editor.dataHolders;


import com.google.gwt.view.client.HasData;
import org.jbpm.formModeler.ng.editor.client.editor.FormEditorPanelPresenter;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderBuilderTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderInfo;
import org.uberfire.client.mvp.UberView;

public interface DataHoldersView extends UberView<FormEditorPanelPresenter> {
    void initDataHolderBuilders(DataHolderBuilderTO[] builders);
    HasData<DataHolderInfo> getDataHoldersGrid();
}
