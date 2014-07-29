package org.jbpm.formModeler.ng.editor.client.editor.dataHolders;

import com.github.gwtbootstrap.client.ui.*;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.RadioButton;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.cell.client.SafeHtmlCell;
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
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.HasData;
import org.jbpm.formModeler.ng.editor.client.editor.FormEditorPanelPresenter;
import org.jbpm.formModeler.ng.editor.events.FormModelerEvent;
import org.jbpm.formModeler.ng.editor.events.dataHolders.NewDataHolderEvent;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderBuilderTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderInfo;
import org.jbpm.formModeler.ng.editor.model.dataHolders.RangedDataHolderBuilderTO;
import org.kie.uberfire.client.tables.PagedTable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.Iterator;

@Dependent
public class DataHoldersViewImpl extends Composite implements DataHoldersView {
    private static final int PAGE_SIZE = 10;

    interface DataHoldersViewBinder
            extends
            UiBinder<Widget, DataHoldersViewImpl> {

    }

    private static DataHoldersViewBinder uiBinder = GWT.create(DataHoldersViewBinder.class);

    @Inject
    private Event<FormModelerEvent> formModelerEvent;

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

    private FormEditorPanelPresenter presenter;

    private PagedTable<DataHolderInfo> grid;

    @Override
    public HasData<DataHolderInfo> getDataHoldersGrid() {
        return grid;
    }

    @Override
    public void init(FormEditorPanelPresenter viewPresenter) {
        this.presenter = viewPresenter;
    }

    @PostConstruct
    public void initView() {
        initWidget( uiBinder.createAndBindUi( this ) );

        okButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                DataHolderInfo info = new DataHolderInfo();
                info.setUniqueId(id.getText());
                info.setInputId(inputId.getText());
                info.setOutputId(outputId.getText());
                info.setType(holderType);
                info.setClassName(className);
                info.setRenderColor(color.getText());
                formModelerEvent.fire(new NewDataHolderEvent(presenter.getContext(), info));
            }
        });

        grid = new PagedTable<DataHolderInfo>(PAGE_SIZE);

        TextColumn<DataHolderInfo> id = new TextColumn<DataHolderInfo>() {
            public String getValue( DataHolderInfo row ) {
                return row.getUniqueId();
            }
        };
        grid.addColumn(id, "Holder Id.");

        TextColumn<DataHolderInfo> input = new TextColumn<DataHolderInfo>() {
            public String getValue( DataHolderInfo row ) {
                return row.getInputId();
            }
        };
        grid.addColumn(input, "Input Id.");

        TextColumn<DataHolderInfo> output = new TextColumn<DataHolderInfo>() {
            public String getValue( DataHolderInfo row ) {
                return row.getOutputId();
            }
        };
        grid.addColumn(output, "Output Id.");

        TextColumn<DataHolderInfo> type = new TextColumn<DataHolderInfo>() {
            public String getValue( DataHolderInfo row ) {
                return row.getType();
            }
        };
        grid.addColumn(type, "Type");

        TextColumn<DataHolderInfo> className = new TextColumn<DataHolderInfo>() {
            public String getValue( DataHolderInfo row ) {
                return row.getClassName();
            }
        };
        grid.addColumn(className, "Class");

        final SafeHtmlCell cell = new SafeHtmlCell();
        Column<DataHolderInfo, SafeHtml> holderColor = new Column<DataHolderInfo, SafeHtml>(cell) {
            public SafeHtml getValue( DataHolderInfo row ) {
                SafeHtmlBuilder sb = new SafeHtmlBuilder();
                String html = "<div style='width: 100%; background-color:" + row.getRenderColor() + ";'>&nbsp;</div>";
                sb.appendHtmlConstant(html);
                return sb.toSafeHtml();
            }
        };
        grid.addColumn(holderColor, "");

        holdersList.add(grid);
    }

    @Override
    public void initDataHolderBuilders(DataHolderBuilderTO[] builders) {
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
                            widget.setVisible(i == (currentRow +1));
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
}
