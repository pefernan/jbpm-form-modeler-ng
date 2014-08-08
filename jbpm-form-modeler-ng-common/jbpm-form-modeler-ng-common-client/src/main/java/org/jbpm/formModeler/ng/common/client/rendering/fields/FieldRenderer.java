package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.google.gwt.user.client.ui.Widget;
import org.jbpm.formModeler.ng.common.client.rendering.FieldDescription;

public abstract class FieldRenderer {
    public abstract String getCode();
    public abstract Widget getFieldInput(final FieldDescription description);

    public boolean supportsLabel() {
        return false;
    }
}
