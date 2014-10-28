package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.github.gwtbootstrap.client.ui.IntegerBox;
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
public class IntegerFieldRenderer extends FieldRenderer {

    @Override
    public String getCode() {
        return "InputTextInteger";
    }

    @Override
    public InputContainer getFieldInput(final FieldDefinition description, final FormContext context) {
        if (description == null) return null;
        final IntegerBox intbox = new IntegerBox();
        intbox.setName(description.getName());
        intbox.setId(description.getName());

        final FormContextStatus status = context.getContextStatus();

        String strvalue = status.getFieldValue(description.getName());

        Integer value = 0;
        if (strvalue != null && !"".equals(strvalue)) {
            value = Integer.decode(strvalue);
        }

        intbox.setValue(value);

        JSONObject jsonProperties = new JSONObject(description);

        JSONValue maxLength = jsonProperties.get("maxLength");

        if (maxLength != null) {
            intbox.setMaxLength(Integer.decode(maxLength.isString().stringValue()));
        }

        JSONValue size = jsonProperties.get("size");
        if (size != null) {
            intbox.setWidth(size.isString().stringValue() + "em");
        }

        intbox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                fieldChanged(description, context, intbox.getText());
            }
        });
        intbox.setEnabled(!description.isReadOnly());

        InputContainer inputContainer = new InputContainer(intbox, getFieldLabel(description), this.supportsLabel(), description, context.getFormDefinition()) {


            public void setReadOnly(boolean readOnly) {
                intbox.setEnabled(!readOnly);
            }
        };
        return inputContainer;
    }

    @Override
    public ImageResource getImage() {
        return FieldTypeImages.INSTANCE.number();
    }

    public String getLabel() {
        return FieldTypeLabels.INSTANCE.number();
    }

    @Override
    public boolean isValidValue(String value) {
        if (isEmpty(value)) return true;
        try {
            Integer intValue = Integer.valueOf(value);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
