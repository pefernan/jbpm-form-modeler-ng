package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.github.gwtbootstrap.datepicker.client.ui.DateBox;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.Date;

@ApplicationScoped
public class ShortDateFieldRenderer extends FieldRenderer {

    @Override
    public String getCode() {
        return "InputShortDate";
    }

    @Override
    public Widget getFieldInput(final FieldDefinition description, final FormContext context) {
        if (description == null) return null;
        final DateBox datepicker = new DateBox();
        datepicker.setId(description.getId());

        final FormContextStatus status = context.getContextStatus();

        String strvalue = status.getFieldValue(description.getId());

        Date value = null;
        if (strvalue != null && !"".equals(strvalue)) {
            value = new Date(Long.decode(strvalue));
        }

        datepicker.setValue(value);

        JSONObject jsonProperties = new JSONObject(description.getData());

        JSONValue size = jsonProperties.get("size");
        if (size != null) {
            datepicker.setWidth(size.isString().stringValue() + "em");
        }

        datepicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> dateValueChangeEvent) {
                Date value = dateValueChangeEvent.getValue();
                String strvalue = null;
                if (value != null) strvalue = String.valueOf(value.getTime());
                changedEvent.fire(new FieldChangedEvent(context.getCtxUID(), description.getUid(), description.getId(),  strvalue));
            }
        });
        datepicker.setEnabled(!description.isReadOnly());
        return datepicker;
    }

    @Override
    public ImageResource getImage() {
        return FieldTypeImages.INSTANCE.date();
    }

    public String getLabel() {
        return FieldTypeLabels.INSTANCE.number();
    }

    @Override
    public boolean isVisible() {
        return false;
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
