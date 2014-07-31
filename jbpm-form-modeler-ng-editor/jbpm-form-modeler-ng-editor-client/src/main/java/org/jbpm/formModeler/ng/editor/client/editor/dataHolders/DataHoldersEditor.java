package org.jbpm.formModeler.ng.editor.client.editor.dataHolders;

import com.github.gwtbootstrap.client.ui.*;
import com.google.gwt.cell.client.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jbpm.formModeler.ng.editor.events.FormModelerEvent;
import org.jbpm.formModeler.ng.editor.events.dataHolders.DeleteDataHolderEvent;
import org.jbpm.formModeler.ng.editor.events.dataHolders.NewDataHolderEvent;
import org.jbpm.formModeler.ng.editor.events.dataHolders.RefreshHoldersListEvent;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderBuilderTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.RangedDataHolderBuilderTO;
import org.jbpm.formModeler.ng.editor.service.FormEditorService;
import org.kie.uberfire.client.tables.PagedTable;
import org.uberfire.paging.PageRequest;
import org.uberfire.paging.PageResponse;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Dependent
public class DataHoldersEditor extends Composite {
    private static final int PAGE_SIZE = 10;

    interface DataHoldersViewBinder
            extends
            UiBinder<Widget, DataHoldersEditor> {

    }

    private static DataHoldersViewBinder uiBinder = GWT.create(DataHoldersViewBinder.class);

    private AsyncDataProvider<DataHolderTO> holdersProvider;

    @Inject
    private Event<FormModelerEvent> formModelerEvent;

    @Inject
    Caller<FormEditorService> editorService;

    @UiField
    ControlGroup idGroup;

    @UiField
    TextBox id;

    @UiField
    ControlGroup inputGroup;

    @UiField
    TextBox inputId;

    @UiField
    ControlGroup outputGroup;

    @UiField
    TextBox outputId;

    @UiField
    ControlGroup typeGroup;

    @UiField
    VerticalPanel holderTypes;

    @UiField
    ControlGroup colorGroup;

    @UiField
    TextBox color;

    @UiField
    Button okButton;

    private String holderType = "";

    private String className = "";

    @UiField
    protected SimplePanel holdersList;

    private FormEditorContextTO context;

    private PagedTable<DataHolderTO> grid;

    @PostConstruct
    public void initView() {
        initWidget( uiBinder.createAndBindUi( this ) );

        okButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                DataHolderTO info = new DataHolderTO();
                info.setUniqueId(id.getText());
                info.setInputId(inputId.getText());
                info.setOutputId(outputId.getText());
                info.setType(holderType);
                info.setClassName(className);
                info.setRenderColor(color.getText());
                formModelerEvent.fire(new NewDataHolderEvent(context, info));
            }
        });

        grid = new PagedTable<DataHolderTO>(PAGE_SIZE);

        List<HasCell<DataHolderTO, ?>> cells = new LinkedList<HasCell<DataHolderTO, ?>>();
        cells.add(new DataHolderDeleteCell("delete", new ActionCell.Delegate<DataHolderTO>() {

            @Override
            public void execute(DataHolderTO row) {
                formModelerEvent.fire(new DeleteDataHolderEvent(context, row));
            }
        }));

        CompositeCell deleteCell = new CompositeCell(cells);
        Column<DataHolderTO, DataHolderTO> deleteHolder = new Column<DataHolderTO, DataHolderTO>(deleteCell) {
            @Override
            public DataHolderTO getValue(DataHolderTO row) {
                return row;
            }
        };

        grid.addColumn(deleteHolder, "");

        TextColumn<DataHolderTO> id = new TextColumn<DataHolderTO>() {
            public String getValue( DataHolderTO row ) {
                return row.getUniqueId();
            }
        };
        grid.addColumn(id, "Holder Id.");

        TextColumn<DataHolderTO> input = new TextColumn<DataHolderTO>() {
            public String getValue( DataHolderTO row ) {
                return row.getInputId();
            }
        };
        grid.addColumn(input, "Input Id.");

        TextColumn<DataHolderTO> output = new TextColumn<DataHolderTO>() {
            public String getValue( DataHolderTO row ) {
                return row.getOutputId();
            }
        };
        grid.addColumn(output, "Output Id.");

        TextColumn<DataHolderTO> type = new TextColumn<DataHolderTO>() {
            public String getValue( DataHolderTO row ) {
                return row.getType();
            }
        };
        grid.addColumn(type, "Type");

        TextColumn<DataHolderTO> className = new TextColumn<DataHolderTO>() {
            public String getValue( DataHolderTO row ) {
                return row.getClassName();
            }
        };
        grid.addColumn(className, "Class");

        final SafeHtmlCell cell = new SafeHtmlCell();
        Column<DataHolderTO, SafeHtml> holderColor = new Column<DataHolderTO, SafeHtml>(cell) {
            public SafeHtml getValue( DataHolderTO row ) {
                SafeHtmlBuilder sb = new SafeHtmlBuilder();
                String html = "<div style='width: 100%; background-color:" + row.getRenderColor() + ";'>&nbsp;</div>";
                sb.appendHtmlConstant(html);
                return sb.toSafeHtml();
            }
        };
        grid.addColumn(holderColor, "");

        holdersList.add(grid);
    }

    public void initEditor(FormEditorContextTO context) {
        this.context = context;
        editorService.call(new RemoteCallback<DataHolderBuilderTO[]>() {
            @Override
            public void callback(DataHolderBuilderTO[] builders) {
                initDataHolderBuilders(builders);
            }
        }).getAvailableDataHolderBuilders(context.getCtxUID());
        refreshDataHoldersTable();
    }

    protected void refreshDataHoldersTable() {
        holdersProvider = new AsyncDataProvider<DataHolderTO>() {
            protected void onRangeChanged( HasData<DataHolderTO> display ) {
                final Range range = display.getVisibleRange();
                PageRequest request = new PageRequest( range.getStart(),
                        range.getLength() );

                editorService.call( new RemoteCallback<PageResponse<DataHolderTO>>() {
                    @Override
                    public void callback( final PageResponse<DataHolderTO> response ) {
                        updateRowCount(response.getTotalRowSize(), response.isTotalRowSizeExact());
                        updateRowData(response.getStartRowIndex(), response.getPageRowList());
                    }
                } ).listFormDataHolders(request, context.getCtxUID());
            }
        };
        holdersProvider.addDataDisplay(grid);
    }

    protected void initDataHolderBuilders(DataHolderBuilderTO[] builders) {
        holderTypes.clear();
        int row = 0;
        for (final DataHolderBuilderTO builder : builders) {
            final int currentRow = row;
            SafeHtmlBuilder htmlBuilder = new SafeHtmlBuilder();
            RadioButton button = new RadioButton("type", htmlBuilder.appendHtmlConstant(builder.getLabel()).toSafeHtml());
            button.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    holderType = builder.getType();
                    className = "";

                    int i = 0;
                    for (Iterator<Widget> it = holderTypes.iterator(); it.hasNext();) {
                        Widget widget = it.next();
                        if (i % 2 != 0) {
                            widget.setVisible(i == (currentRow + 1));
                        }
                        i++;
                    }
                }
            });
            holderTypes.add(button);
            Widget inputValue;
            if (builder instanceof RangedDataHolderBuilderTO) {
                final ListBox typeValue = new ListBox();

                RangedDataHolderBuilderTO rangedBuilder = (RangedDataHolderBuilderTO) builder;

                String[] values = rangedBuilder.getValues();

                for (String value : values) {
                    typeValue.addItem(value);
                }

                typeValue.addChangeHandler(new ChangeHandler() {
                    @Override
                    public void onChange(ChangeEvent changeEvent) {
                        className = typeValue.getValue();
                    }
                });
                inputValue = typeValue;
            } else {
                TextBox typeValue = new TextBox();
                typeValue.addValueChangeHandler(new ValueChangeHandler<String>() {
                    @Override
                    public void onValueChange(ValueChangeEvent<String> changeEvent) {
                        className = changeEvent.getValue();
                    }
                });
                inputValue = typeValue;
            }
            button.setValue(row == 0);
            inputValue.setVisible(row == 0);

            holderTypes.add(inputValue);
            row += 2;
        }
    }

    private class DataHolderDeleteCell implements HasCell<DataHolderTO, DataHolderTO> {
        private ActionCell<DataHolderTO> fCell;

        public DataHolderDeleteCell(String text, ActionCell.Delegate<DataHolderTO> delegate) {
            fCell = new ActionCell<DataHolderTO>(text, delegate);
        }

        @Override
        public Cell<DataHolderTO> getCell() {
            return fCell;
        }

        @Override
        public FieldUpdater<DataHolderTO, DataHolderTO> getFieldUpdater() {
            return null;
        }

        @Override
        public DataHolderTO getValue(DataHolderTO object) {
            return object;
        }
    }

    public void refreshGrid(@Observes RefreshHoldersListEvent refreshHoldersListEvent) {
        if (context != null && context.getCtxUID().equals(refreshHoldersListEvent.getContext().getCtxUID())) refreshDataHoldersTable();
    }
}
