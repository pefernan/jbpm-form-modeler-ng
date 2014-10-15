package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.github.gwtbootstrap.client.ui.DoubleBox;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Widget;
import org.jbpm.formModeler.ng.common.client.rendering.event.FieldChangedEvent;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContextStatus;
import org.jbpm.formModeler.ng.common.client.rendering.resources.i18n.FieldTypeLabels;
import org.jbpm.formModeler.ng.common.client.rendering.resources.images.FieldTypeImages;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DecimalFieldRenderer extends FieldRenderer {
    @Override
    public String getCode() {
        return "InputTextDouble";
    }

    @Override
    public Widget getFieldInput(final FieldDefinition description, final FormContext context) {
        if (description == null) return null;
        final DoubleBox doubleBox = new DoubleBox();
        doubleBox.setName(description.getId());
        doubleBox.setId(description.getId());

        final FormContextStatus status = context.getContextStatus();

        String strvalue = status.getFieldValue(description.getId());

        Double value = 0.0;
        if (strvalue != null && !"".equals(strvalue)) {
            value = Double.valueOf(strvalue);
        }

        doubleBox.setValue(value);

        JSONObject jsonProperties = new JSONObject(description.getData());

        JSONValue maxLength = jsonProperties.get("maxLength");

        if (maxLength != null) {
            doubleBox.setMaxLength(Integer.decode(maxLength.isString().stringValue()));
        }

        JSONValue size = jsonProperties.get("size");
        if (size != null) {
            doubleBox.setWidth(size.isString().stringValue() + "em");
        }

        doubleBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                changedEvent.fire(new FieldChangedEvent(context.getCtxUID(), description.getUid(), description.getId(), doubleBox.getText()));
            }
        });
        doubleBox.setEnabled(!description.isReadOnly());
        return doubleBox;
    }

    @Override
    public ImageResource getImage() {
        return FieldTypeImages.INSTANCE.decimal();
    }

    @Override
    public String getLabel() {
        return FieldTypeLabels.INSTANCE.decimal();
    }

    @Override
    public boolean isValidValue(String value) {
        if (isEmpty(value)) return true;
        try {
            Double doubleValue = Double.valueOf(value);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
