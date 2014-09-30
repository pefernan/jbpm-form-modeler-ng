package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.github.gwtbootstrap.client.ui.LongBox;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Widget;
import org.jbpm.formModeler.ng.common.client.rendering.event.FieldChangedEvent;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContextStatus;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@ApplicationScoped
public class LongFieldRenderer extends FieldRenderer {
    @Inject
    private Event<FieldChangedEvent> changedEvent;

    @Override
    public String getCode() {
        return "InputTextLong";
    }

    @Override
    public Widget getFieldInput(final FieldDefinition description, final FormContext context) {
        if (description == null) return null;
        final LongBox longBox = new LongBox();
        longBox.setName(description.getId());
        longBox.setId(description.getId());

        final FormContextStatus status = context.getContextStatus();

        String strvalue = status.getFieldValue(description.getId());

        Long value = new Long(0);
        if (strvalue != null && !"".equals(strvalue)) {
            value = Long.decode(strvalue);
        }

        longBox.setValue(value);

        JSONObject jsonProperties = new JSONObject(description.getData());

        JSONValue maxLength = jsonProperties.get("maxLength");

        if (maxLength != null) {
            longBox.setMaxLength(Integer.decode(maxLength.isString().stringValue()));
        }

        JSONValue size = jsonProperties.get("size");
        if (size != null) {
            longBox.setWidth(size.isString().stringValue() + "em");
        }

        longBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                changedEvent.fire(new FieldChangedEvent(context.getCtxUID(), description.getId(),  String.valueOf(longBox.getValue())));
            }
        });
        return longBox;
    }
}
