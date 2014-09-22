package org.jbpm.formModeler.ng.common.client.rendering.renderers;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormDefinition;

import javax.enterprise.context.Dependent;

@Dependent
public class DefaultFormRenderer extends FormRenderer {
    @Override
    public String getCode() {
        return "default";
    }

    @Override
    public Panel generateForm(FormContext context) {
        VerticalPanel formContent = new VerticalPanel();

        FormDefinition formDefinition = context.getFormDefinition();

        if (formDefinition != null) {
            HorizontalPanel horizontalPanel = new HorizontalPanel();
            formContent.add(horizontalPanel);
            for(int i = 0; i < formDefinition.getFieldDefinitions().length(); i++) {
                FieldDefinition fieldDefinition = formDefinition.getFieldDefinitions().get(i);
                Widget fieldBox = getFieldBox(fieldDefinition, context);
                if (fieldBox != null) {
                    if (!fieldDefinition.isGrouped()) {
                        horizontalPanel = new HorizontalPanel();
                        formContent.add(horizontalPanel);
                    }
                    horizontalPanel.add(fieldBox);
                }
            }
        }

        return formContent;
    }
}
