package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.github.gwtbootstrap.datetimepicker.client.ui.DateTimeBox;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
import java.util.Date;

@ApplicationScoped
public class TimestampFieldRenderer extends FieldRenderer {

    @Override
    public String getCode() {
        return "InputDate";
    }

    @Override
    public InputContainer getFieldInput(final FieldDefinition description, final FormContext context) {
        if (description == null) return null;
        final DateTimeBox datetimepicker = new DateTimeBox();
        datetimepicker.setId(description.getName());

        final FormContextStatus status = context.getContextStatus();

        String strvalue = status.getFieldValue(description.getName());

        Date value = null;
        if (strvalue != null && !"".equals(strvalue)) {
            value = new Date(Long.decode(strvalue));
        }

        datetimepicker.setValue(value);

        JSONObject jsonProperties = new JSONObject(description);

        JSONValue size = jsonProperties.get("size");
        if (size != null) {
            datetimepicker.setWidth(size.isString().stringValue() + "em");
        }

        datetimepicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> dateValueChangeEvent) {
                Date value = dateValueChangeEvent.getValue();
                String strvalue = null;
                if (value != null) strvalue = String.valueOf(value.getTime());
                fieldChanged(description, context, strvalue);
            }
        });
        datetimepicker.setEnabled(!description.isReadOnly());

        InputContainer inputContainer = new InputContainer(datetimepicker, getFieldLabel(description), this.supportsLabel(), description, context.getFormDefinition()) {
            public void setReadOnly(boolean readOnly) {
                datetimepicker.setEnabled(!readOnly);
            }
        };

        return inputContainer;
    }

    @Override
    public ImageResource getImage() {
        return FieldTypeImages.INSTANCE.date();
    }

    public String getLabel() {
        return FieldTypeLabels.INSTANCE.timestamp();
    }

    @Override
    public boolean isValidValue(String value) {
        if (isEmpty(value)) return true;
        try {
            Date date = new Date(Long.valueOf(value));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
