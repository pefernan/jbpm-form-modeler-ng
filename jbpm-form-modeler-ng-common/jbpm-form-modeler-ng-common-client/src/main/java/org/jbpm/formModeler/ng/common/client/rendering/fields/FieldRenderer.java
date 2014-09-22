package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.google.gwt.user.client.ui.Widget;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;

public abstract class FieldRenderer {
    public abstract String getCode();
    public abstract Widget getFieldInput(final FieldDefinition description, final FormContext context);

    public boolean supportsLabel() {
        return false;
    }
}
