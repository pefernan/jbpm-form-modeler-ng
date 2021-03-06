package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.resources.client.ImageResource;
import org.jbpm.formModeler.ng.common.client.rendering.InputContainer;
import org.jbpm.formModeler.ng.common.client.rendering.event.FieldChangedEvent;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldOption;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContextStatus;
import org.jbpm.formModeler.ng.common.client.rendering.resources.i18n.FieldTypeLabels;
import org.jbpm.formModeler.ng.common.client.rendering.resources.images.FieldTypeImages;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DropDownFieldRenderer extends FieldRenderer {

    @Override
    public String getCode() {
        return "DropDown";
    }

    @Override
    public InputContainer getFieldInput(final FieldDefinition description, final FormContext context) {
        final ListBox listBox = new ListBox();

        listBox.setName(description.getName());
        listBox.setId(description.getName());

        final FormContextStatus status = context.getContextStatus();

        JsArray<FieldOption> options = status.getFieldOptions(description.getName());
        if (options != null) {
            for (int i = 0; i < options.length(); i++) {
                FieldOption option = options.get(i);
                listBox.addItem(option.getText(), option.getValue());
            }
        }

        listBox.setSelectedValue(status.getFieldValue(description.getName()));

        JSONObject jsonProperties = new JSONObject(description);

        JSONValue size = jsonProperties.get("size");
        if (size != null) {
            listBox.setWidth(size.isString().stringValue() + "em");
        }

        listBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                fieldChanged(description, context, listBox.getValue());
            }
        });
        listBox.setEnabled(!description.isReadOnly());

        InputContainer inputContainer = new InputContainer(listBox, getFieldLabel(description), this.supportsLabel(), description, context.getFormDefinition()) {
            public void setReadOnly(boolean readOnly) {
                listBox.setEnabled(!readOnly);
            }
        };

        return inputContainer;
    }

    @Override
    public ImageResource getImage() {
        return FieldTypeImages.INSTANCE.dropdown();
    }

    public String getLabel() {
        return FieldTypeLabels.INSTANCE.dropdown();
    }
}
