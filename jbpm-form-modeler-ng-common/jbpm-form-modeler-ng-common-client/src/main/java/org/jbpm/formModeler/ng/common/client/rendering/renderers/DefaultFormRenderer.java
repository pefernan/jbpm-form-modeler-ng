package org.jbpm.formModeler.ng.common.client.rendering.renderers;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jbpm.formModeler.ng.common.client.rendering.FieldDescription;
import org.jbpm.formModeler.ng.common.client.rendering.FormDescription;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;

@Dependent
public class DefaultFormRenderer extends FormRenderer {
    @Override
    public String getCode() {
        return "default";
    }

    @Override
    public Panel generateForm(FormDescription formDescription) {
        VerticalPanel formContent = new VerticalPanel();

        if (formDescription != null) {
            HorizontalPanel horizontalPanel = new HorizontalPanel();
            formContent.add(horizontalPanel);
            for(int i = 0; i < formDescription.getFields().length(); i++) {
                FieldDescription fieldDescription = formDescription.getFields().get(i);
                Widget fieldBox = getFieldBox(formDescription, fieldDescription);
                if (fieldBox != null) {
                    if (!fieldDescription.isGrouped()) {
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
