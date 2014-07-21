package org.jbpm.formModeler.ng.renderer.client.rendering.renderers;

import com.github.gwtbootstrap.client.ui.FormLabel;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.jbpm.formModeler.ng.model.Form;
import org.jbpm.formModeler.ng.renderer.client.rendering.FieldDescription;
import org.jbpm.formModeler.ng.renderer.client.rendering.FieldProviderManager;
import org.jbpm.formModeler.ng.renderer.client.rendering.FormDescription;
import org.jbpm.formModeler.ng.renderer.client.rendering.fields.FieldProvider;

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

        FormLabel fieldLabel = new FormLabel(field.getLabel());
        fieldLabel.setFor(field.getId());

        CellPanel fieldBox;

        if (form.getLabelMode().equals(Form.LABEL_MODE_AFTER)) {
            fieldBox = new VerticalPanel();
            fieldBox.add(input);
            fieldBox.add(fieldLabel);
        } else if (form.getLabelMode().equals(Form.LABEL_MODE_LEFT)) {
            fieldBox = new HorizontalPanel();
            fieldBox.add(fieldLabel);
            fieldBox.add(input);
        } else if (form.getLabelMode().equals(Form.LABEL_MODE_RIGHT)) {
            fieldBox = new HorizontalPanel();
            fieldBox.add(input);
            fieldBox.add(fieldLabel);
        } else {
            fieldBox = new VerticalPanel();
            fieldBox.add(fieldLabel);
            fieldBox.add(input);
        }

        return fieldBox;
    }
}
