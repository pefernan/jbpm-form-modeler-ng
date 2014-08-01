package org.jbpm.formModeler.ng.common.client.rendering.renderers;

import com.github.gwtbootstrap.client.ui.FormLabel;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.jbpm.formModeler.ng.common.client.rendering.FieldDescription;
import org.jbpm.formModeler.ng.common.client.rendering.FieldProviderManager;
import org.jbpm.formModeler.ng.common.client.rendering.FormDescription;
import org.jbpm.formModeler.ng.common.client.rendering.fields.FieldProvider;
import org.jbpm.formModeler.ng.model.Form;

import javax.inject.Inject;

public abstract class FormRenderer {

    @Inject
    protected FieldProviderManager providerManager;

    public abstract String getCode();
    public abstract Panel generateForm(FormDescription formDescription);

    protected Widget getFieldBox(FormDescription form, FieldDescription field) {
        FieldProvider provider = providerManager.getProviderByType(field.getType());

        if (provider == null) {
            Window.alert("Unable to find provider for: " + field.getType());
            return null;
        }

        Widget input = provider.getFieldInput(field);

        if (provider.supportsLabel() || form.getLabelMode().equals(Form.LABEL_MODE_HIDDEN)) return input;

        CellPanel fieldBox;

        if (form.getLabelMode().equals(Form.LABEL_MODE_AFTER)) {
            fieldBox = new VerticalPanel();
            fieldBox.add(input);
            fieldBox.add(getFieldLabel(field));
        } else if (form.getLabelMode().equals(Form.LABEL_MODE_LEFT)) {
            fieldBox = new HorizontalPanel();
            fieldBox.add(getFieldLabel(field));
            fieldBox.add(input);
        } else if (form.getLabelMode().equals(Form.LABEL_MODE_RIGHT)) {
            fieldBox = new HorizontalPanel();
            fieldBox.add(input);
            fieldBox.add(getFieldLabel(field));
        } else {
            fieldBox = new VerticalPanel();
            fieldBox.add(getFieldLabel(field));
            fieldBox.add(input);
        }

        return fieldBox;
    }

    protected Widget getFieldLabel(FieldDescription field) {
        String label = field.getLabel();
        if (field.isRequired()) label = "* " + label;
        FormLabel fieldLabel = new FormLabel(label);
        fieldLabel.setFor(field.getId());
        return fieldLabel;
    }
}
