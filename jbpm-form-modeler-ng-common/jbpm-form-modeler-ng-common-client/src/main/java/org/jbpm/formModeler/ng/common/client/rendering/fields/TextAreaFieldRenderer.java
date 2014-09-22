package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.github.gwtbootstrap.client.ui.TextArea;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContextStatus;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Portable
public class TextAreaFieldRenderer extends FieldRenderer {

    @Override
    public String getCode() {
        return "InputTextArea";
    }

    @Override
    public Widget getFieldInput(final FieldDefinition description, FormContext context) {
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
                status.setFieldValue(description.getId(), textArea.getValue());
            }
        });

        return textArea;
    }
}
