package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Widget;
import org.jbpm.formModeler.ng.common.client.rendering.event.FieldChangedEvent;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldOption;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContextStatus;
import org.jbpm.formModeler.ng.common.client.rendering.resources.i18n.FieldTypeLabels;
import org.jbpm.formModeler.ng.common.client.rendering.resources.images.FieldTypeImages;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@ApplicationScoped
public class DropDownFieldRenderer extends FieldRenderer {

    @Inject
    private Event<FieldChangedEvent> changedEvent;

    @Override
    public String getCode() {
        return "DropDown";
    }

    @Override
    public Widget getFieldInput(final FieldDefinition description, final FormContext context) {
        final ListBox listBox = new ListBox();

        listBox.setName(description.getId());
        listBox.setId(description.getId());

        final FormContextStatus status = context.getContextStatus();

        String value = status.getFieldValue(description.getId());

        JsArray<FieldOption> options = status.getFieldOptions(description.getId());
        if (options != null) {
            for (int i = 0; i < options.length(); i++) {
                FieldOption option = options.get(i);
                listBox.addItem(option.getText(), option.getValue());
            }
        }

        listBox.setSelectedValue(value);

        JSONObject jsonProperties = new JSONObject(description.getData());

        JSONValue size = jsonProperties.get("size");
        if (size != null) {
            listBox.setWidth(size.isString().stringValue() + "em");
        }

        listBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                changedEvent.fire(new FieldChangedEvent(context.getCtxUID(), description.getId(), listBox.getValue()));
            }
        });
        listBox.setEnabled(!description.isReadOnly());
        return listBox;
    }

    @Override
    public ImageResource getImage() {
        return FieldTypeImages.INSTANCE.dropdown();
    }

    public String getLabel() {
        return FieldTypeLabels.INSTANCE.dropdown();
    }
}
