package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.github.gwtbootstrap.datetimepicker.client.ui.DateTimeBox;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContextStatus;

import javax.enterprise.context.ApplicationScoped;
import java.util.Date;

@ApplicationScoped
@Portable
public class TimestampFieldRenderer extends FieldRenderer {
    @Override
    public String getCode() {
        return "InputDate";
    }

    @Override
    public Widget getFieldInput(final FieldDefinition description, FormContext context) {
        if (description == null) return null;
        final DateTimeBox datetimepicker = new DateTimeBox();
        datetimepicker.setId(description.getId());

        final FormContextStatus status = context.getContextStatus();

        String strvalue = status.getFieldValue(description.getId());

        Date value = null;
        if (strvalue != null && !"".equals(strvalue)) {
            value = new Date(Long.decode(strvalue));
        }

        datetimepicker.setValue(value);

        JSONObject jsonProperties = new JSONObject(description.getData());

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
                status.setFieldValue(description.getId(), strvalue);
            }
        });

        return datetimepicker;
    }
}
