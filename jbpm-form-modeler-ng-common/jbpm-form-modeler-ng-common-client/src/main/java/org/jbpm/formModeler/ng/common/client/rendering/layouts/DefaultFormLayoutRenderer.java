package org.jbpm.formModeler.ng.common.client.rendering.layouts;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jbpm.formModeler.ng.common.client.rendering.FormLayoutRenderer;
import org.jbpm.formModeler.ng.common.client.rendering.js.*;

import javax.enterprise.context.Dependent;

@Dependent
public class DefaultFormLayoutRenderer extends FormLayoutRenderer {
    @Override
    public String getCode() {
        return "default";
    }

    @Override
    public Panel generateFormContent(FormContext context) {

        VerticalPanel formContent = new VerticalPanel();
        FormDefinition formDefinition = context.getFormDefinition();

        if (formDefinition != null) {
            FormLayoutDefinition layout = formDefinition.getLayout();
            for (int row = 0; row < layout.getAreas().length(); row++) {
                FormLayoutArea area = layout.getAreas().get(row);
                if (area.getElements().length() == 0) continue;
                renderArea(formContent, row);
                HorizontalPanel horizontalPanel = new HorizontalPanel();
                formContent.add(horizontalPanel);
                for (int column = 0; column < area.getElements().length(); column++) {
                    FieldDefinition fieldDefinition = formDefinition.getFieldDefinition(area.getElements().get(column));
                    Widget fieldBox = getFieldBox(fieldDefinition, context);
                    renderField(horizontalPanel, row, column);
                    horizontalPanel.add(fieldBox);
                }
                renderField(horizontalPanel, row, area.getElements().length());
            }
            renderArea(formContent, layout.getAreas().length());
        }

        return formContent;
    }

    public void renderArea(Panel content, int index) {

    }

    public void renderField(Panel content, int areaIndex, int fieldIndex) {

    }
}
