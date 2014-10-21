package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.github.gwtbootstrap.client.ui.TextArea;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import org.jbpm.formModeler.ng.common.client.rendering.InputContainer;
import org.jbpm.formModeler.ng.common.client.rendering.event.FieldChangedEvent;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContextStatus;
import org.jbpm.formModeler.ng.common.client.rendering.resources.i18n.FieldTypeLabels;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TextAreaFieldRenderer extends FieldRenderer {

    @Override
    public String getCode() {
        return "InputTextArea";
    }

    @Override
    public InputContainer getFieldInput(final FieldDefinition description, final FormContext context) {
        if (description == null) return null;
        final TextArea textArea = new TextArea();
        textArea.setName(description.getId());
        textArea.setId(description.getId());

        final FormContextStatus status = context.getContextStatus();

        String value = status.getFieldValue(description.getId());

        textArea.setValue(value);

        JSONObject jsonProperties = new JSONObject(description.getData());

        JSONValue height = jsonProperties.get("height");

        if (height != null) {
            textArea.setHeight(height.isString().stringValue() + "em");
        }

        JSONValue size = jsonProperties.get("width");
        if (size != null) {
            textArea.setWidth(size.isString().stringValue() + "em");
        }

        textArea.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                changedEvent.fire(new FieldChangedEvent(context.getCtxUID(), description.getUid(), description.getId(),  textArea.getValue()));
            }
        });
        textArea.setEnabled(!description.isReadOnly());

        InputContainer inputContainer = new InputContainer(textArea, getFieldLabel(description), this.supportsLabel(), description, context.getFormDefinition()) {
            public void setReadOnly(boolean readOnly) {
                textArea.setEnabled(!readOnly);
            }
        };

        return inputContainer;
    }

    public String getLabel() {
        return FieldTypeLabels.INSTANCE.textarea();
    }
}
