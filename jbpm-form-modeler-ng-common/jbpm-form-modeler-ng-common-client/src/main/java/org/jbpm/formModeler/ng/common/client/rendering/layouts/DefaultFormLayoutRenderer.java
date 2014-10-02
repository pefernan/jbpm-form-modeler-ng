package org.jbpm.formModeler.ng.common.client.rendering.layouts;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jbpm.formModeler.ng.common.client.rendering.js.*;

import javax.enterprise.context.Dependent;

@Dependent
public class DefaultFormLayoutRenderer extends FormLayoutRenderer {
    @Override
    public String getCode() {
        return "default";
    }

    @Override
    public Panel generateForm(FormContext context) {

        VerticalPanel formContent = new VerticalPanel();
        FormDefinition formDefinition = context.getFormDefinition();

        if (formDefinition != null) {
            FormLayoutDefinition layout = formDefinition.getLayout();
            for (int i = 0; i < layout.getAreas().length(); i++) {
                FormLayoutArea area = layout.getAreas().get(i);
                HorizontalPanel horizontalPanel = new HorizontalPanel();
                formContent.add(horizontalPanel);
                for (int j = 0; j < area.getElements().length(); j++) {
                    FieldDefinition fieldDefinition = formDefinition.getFieldDefinition(area.getElements().get(j));
                    Widget fieldBox = getFieldBox(fieldDefinition, context);
                    horizontalPanel.add(fieldBox);
                }
            }
        }

        return formContent;
    }
}
