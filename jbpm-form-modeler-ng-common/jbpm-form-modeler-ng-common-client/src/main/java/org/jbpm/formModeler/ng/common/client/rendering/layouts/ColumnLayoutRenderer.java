package org.jbpm.formModeler.ng.common.client.rendering.layouts;

import com.google.gwt.dom.client.Style;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.*;
import org.jbpm.formModeler.ng.common.client.rendering.fields.FieldRenderer;
import org.jbpm.formModeler.ng.common.client.rendering.js.*;
import org.jbpm.formModeler.ng.model.Form;

import javax.enterprise.context.Dependent;

@Dependent
public class ColumnLayoutRenderer extends FormLayoutRenderer {
    @Override
    public String getCode() {
        return "columns";
    }

    @Override
    public Panel generateFormContent(FormContext context) {
        FormDefinition formDefinition = context.getFormDefinition();

        if (formDefinition != null) {
            FormLayoutDefinition layout = formDefinition.getLayout();

            Grid content = new Grid(1, layout.getAreas().length());
            content.setWidth("100%");
            int columns = 2;
            JSONObject object = new JSONObject(layout);
            JSONValue jsonColumns = object.get("columns");
            if (jsonColumns != null && jsonColumns.isNull() == null) columns = (int) jsonColumns.isNumber().doubleValue();
            String width = 100 / columns + "%";
            content.getColumnFormatter().setWidth(0, width);
            content.getColumnFormatter().setWidth(1, width);
            HTMLTable.CellFormatter formatter = content.getCellFormatter();
            for (int i = 0; i < columns; i++) {
                formatter.setVerticalAlignment(0, i, HasVerticalAlignment.ALIGN_TOP);
            }
            for (int i = 0; i < layout.getAreas().length(); i++) {
                FormLayoutArea area = layout.getAreas().get(i);
                content.setWidget(0, i, renderArea(area, i, context));
            }
            return content;
        }
        return new SimplePanel();
    }

    protected Widget renderArea(FormLayoutArea area, int column, FormContext context) {
        FormDefinition formDefinition = context.getFormDefinition();
        VerticalPanel content = new VerticalPanel();
        content.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.TOP);
        content.setWidth("100%");
        renderField(content, column, 0);
        for (int j = 0; j < calculateGridRows(area); j++) {
            FieldDefinition fieldDefinition = formDefinition.getFieldDefinition(area.getElements().get(j));
            Widget fieldbox = getFieldBox(fieldDefinition, context);
            if (fieldbox != null) {
                content.add(fieldbox);
            }
            renderField(content, column, j + 1);
        }
        return content;
    }

    protected int calculateGridRows(FormLayoutArea area) {
        return area.getElements().length();
    }

    public void renderField(Panel content, int areaIndex, int fieldIndex) {

    }
}
