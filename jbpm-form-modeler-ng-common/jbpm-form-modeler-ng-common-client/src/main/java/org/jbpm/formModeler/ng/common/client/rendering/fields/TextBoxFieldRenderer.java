package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.common.client.rendering.FieldDescription;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Portable
public class TextBoxFieldRenderer extends FieldRenderer {
    @Override
    public String getCode() {
        return "InputText";
    }

    @Override
    public Widget getFieldInput(final FieldDescription description) {
        if (description == null) return null;
        final TextBox text = new TextBox();
        text.setName(description.getId());
        text.setId(description.getId());
        JSONObject options = new JSONObject(description.getOptions());

        JSONValue maxLength = options.get("maxLength");

        if (maxLength != null) {
            text.setMaxLength(Integer.decode(maxLength.isString().stringValue()));
        }

        JSONValue size = options.get("size");
        if (size != null) {
            text.setWidth(size.isString().stringValue() + "em");
        }

        text.setText(description.getValue());
        text.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                description.setValue(text.getValue());
            }
        });
        return text;
    }
}
