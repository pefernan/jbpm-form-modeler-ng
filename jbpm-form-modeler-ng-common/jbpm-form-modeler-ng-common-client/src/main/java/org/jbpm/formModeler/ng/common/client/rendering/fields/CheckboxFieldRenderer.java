package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.github.gwtbootstrap.client.ui.CheckBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import org.jbpm.formModeler.ng.common.client.rendering.FormLayoutRenderer;
import org.jbpm.formModeler.ng.common.client.rendering.InputContainer;
import org.jbpm.formModeler.ng.common.client.rendering.event.FieldChangedEvent;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContextStatus;
import org.jbpm.formModeler.ng.common.client.rendering.layouts.utils.FieldLabelHelper;
import org.jbpm.formModeler.ng.common.client.rendering.resources.i18n.FieldTypeLabels;
import org.jbpm.formModeler.ng.common.client.rendering.resources.images.FieldTypeImages;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CheckboxFieldRenderer extends FieldRenderer {

    @Inject
    private FieldLabelHelper labelHelper;

    @Override
    public String getCode() {
        return "CheckBox";
    }

    @Override
    public InputContainer getFieldInput(final FieldDefinition description, final FormContext context) {
        if (description == null) return null;

        String label = null;
        if (context.getFormDefinition().getLabelMode().equals(FormLayoutRenderer.LABEL_MODE_DEFAULT)) {
            label = getFieldLabel(description);
        }

        final CheckBox checkBox = new CheckBox(label);

        checkBox.setName(description.getName());
        checkBox.setId(description.getName());

        final FormContextStatus status = context.getContextStatus();

        Boolean value = Boolean.valueOf(status.getFieldValue(description.getName()));

        checkBox.setValue(value);
        checkBox.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent changeEvent) {
                fieldChanged(description, context, checkBox.getValue().toString());
            }
        });
        checkBox.setEnabled(!description.isReadOnly());

        InputContainer inputContainer = new InputContainer(checkBox, label, this.supportsLabel(), description, context.getFormDefinition()) {
            public void setReadOnly(boolean readOnly) {
                checkBox.setEnabled(!readOnly);
            }
        };

        return inputContainer;
    }

    @Override
    public boolean supportsLabel() {
        return true;
    }

    @Override
    public ImageResource getImage() {
        return FieldTypeImages.INSTANCE.checkbox();
    }

    public String getLabel() {
        return FieldTypeLabels.INSTANCE.checkbox();
    }
}