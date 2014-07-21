package org.jbpm.formModeler.ng.renderer.client.rendering.renderers;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jbpm.formModeler.ng.renderer.client.rendering.FieldDescription;
import org.jbpm.formModeler.ng.renderer.client.rendering.FormDescription;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;

@ApplicationScoped
public class AlignedFormRenderer extends FormRenderer {
    @Override
    public String getCode() {
        return "aligned";
    }

    @Override
    public Panel generateForm(FormDescription formDescription) {

        ArrayList<ArrayList<FieldDescription>> rows = new ArrayList<ArrayList<FieldDescription>>();

        int maxColumns = 0;

        ArrayList<FieldDescription> aRow = new ArrayList<FieldDescription>();

        for(int i = 0; i < formDescription.getFields().length(); i++) {
            FieldDescription fieldDescription = formDescription.getFields().get(i);
            if (rows.isEmpty() || !fieldDescription.isGrouped()) {
                aRow = new ArrayList<FieldDescription>();
                rows.add(aRow);
            }
            aRow.add(fieldDescription);
            if (maxColumns < aRow.size()) maxColumns = aRow.size();
        }

        Grid formContent = new Grid(rows.size(), maxColumns);


        if (formDescription != null) {
            for (int row = 0; row < rows.size(); row++) {
                aRow = rows.get(row);
                for (int col = 0; col < maxColumns; col ++) {
                    Widget fieldBox = null;
                    if (col < maxColumns && col < aRow.size()) {
                        fieldBox = getFieldBox(formDescription, aRow.get(col));
                    }

                    if (fieldBox == null) {
                        fieldBox = new VerticalPanel();
                    }
                    formContent.setWidget(row, col, fieldBox);
                }
            }
        }

        return formContent;
    }
}
