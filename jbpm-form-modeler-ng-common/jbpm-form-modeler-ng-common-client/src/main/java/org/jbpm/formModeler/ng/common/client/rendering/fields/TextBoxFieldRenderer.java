package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.resources.client.ImageResource;
import org.jbpm.formModeler.ng.common.client.rendering.InputContainer;
import org.jbpm.formModeler.ng.common.client.rendering.event.FieldChangedEvent;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContextStatus;
import org.jbpm.formModeler.ng.common.client.rendering.resources.i18n.FieldTypeLabels;
import org.jbpm.formModeler.ng.common.client.rendering.resources.images.FieldTypeImages;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TextBoxFieldRenderer extends FieldRenderer {

    @Override
    public String getCode() {
        return "InputText";
    }

    @Override
    public InputContainer getFieldInput(final FieldDefinition description, final FormContext context) {
        if (description == null) return null;
        final TextBox text = new TextBox();
        text.setName(description.getName());
        text.setId(String.valueOf(description.getId()));

        final FormContextStatus status = context.getContextStatus();

        text.setValue(status.getFieldValue(description.getName()));

        JSONObject jsonProperties = new JSONObject(description);

        JSONValue maxLength = jsonProperties.get("maxLength");

        if (maxLength != null) {
            text.setMaxLength(Integer.decode(maxLength.isString().stringValue()));
        }

        JSONValue size = jsonProperties.get("size");
        if (size != null) {
            text.setWidth(size.isString().stringValue() + "em");
        }

        text.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                fieldChanged(description, context, text.getValue());
            }
        });
        text.setEnabled(!description.isReadOnly());

        InputContainer inputContainer = new InputContainer(text, getFieldLabel(description), this.supportsLabel(), description, context.getFormDefinition()) {
            public void setReadOnly(boolean readOnly) {
                text.setEnabled(!readOnly);
            }
        };

        return inputContainer;
    }

    @Override
    public ImageResource getImage() {
        return FieldTypeImages.INSTANCE.textbox();
    }

    public String getLabel() {
        return FieldTypeLabels.INSTANCE.textbox();
    }
}
