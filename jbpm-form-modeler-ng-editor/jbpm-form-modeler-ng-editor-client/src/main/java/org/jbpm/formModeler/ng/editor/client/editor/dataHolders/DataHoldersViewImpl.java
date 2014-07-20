package org.jbpm.formModeler.ng.editor.client.editor.dataHolders;

import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.jbpm.formModeler.ng.editor.client.editor.FormEditorPanelPresenter;
import org.jbpm.formModeler.ng.editor.model.DataHolderPageRow;
import org.kie.uberfire.client.tables.PagedTable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@Templated(value = "DataHoldersViewImpl.html")
public class DataHoldersViewImpl extends Composite implements DataHoldersView {
    private static final int PAGE_SIZE = 10;

    @Inject
    @DataField
    private SimplePanel holdersList;

    private FormEditorPanelPresenter presenter;

    private PagedTable<DataHolderPageRow> grid;

    public DataHoldersViewImpl() {

    }

    @Override
    public HasData<DataHolderPageRow> getDataHoldersGrid() {
        return grid;
    }

    @Override
    public void init(FormEditorPanelPresenter presenter) {
        this.presenter = presenter;

    }

    @PostConstruct
    public void initGrid() {
        grid = new PagedTable<DataHolderPageRow>(PAGE_SIZE);

        TextColumn<DataHolderPageRow> id = new TextColumn<DataHolderPageRow>() {
            public String getValue( DataHolderPageRow row ) {
                return row.getUniqueId();
            }
        };
        grid.addColumn(id, "Holder Id.");

        TextColumn<DataHolderPageRow> input = new TextColumn<DataHolderPageRow>() {
            public String getValue( DataHolderPageRow row ) {
                return row.getInputId();
            }
        };
        grid.addColumn(input, "Input Id.");

        TextColumn<DataHolderPageRow> output = new TextColumn<DataHolderPageRow>() {
            public String getValue( DataHolderPageRow row ) {
                return row.getOutputId();
            }
        };
        grid.addColumn(output, "Output Id.");

        TextColumn<DataHolderPageRow> type = new TextColumn<DataHolderPageRow>() {
            public String getValue( DataHolderPageRow row ) {
                return row.getType();
            }
        };
        grid.addColumn(type, "Type");

        TextColumn<DataHolderPageRow> className = new TextColumn<DataHolderPageRow>() {
            public String getValue( DataHolderPageRow row ) {
                return row.getClassName();
            }
        };
        grid.addColumn(className, "Class");

        TextColumn<DataHolderPageRow> holderColor = new TextColumn<DataHolderPageRow>() {
            public String getValue( DataHolderPageRow row ) {
                return row.getRenderColor();
            }
        };
        grid.addColumn(holderColor, "");

        holdersList.add(grid);
    }


}
