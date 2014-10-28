package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import org.uberfire.commons.data.Pair;
import org.jbpm.formModeler.ng.common.client.rendering.InputContainer;
import org.jbpm.formModeler.ng.common.client.rendering.event.FieldChangedEvent;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContextStatus;
import org.jbpm.formModeler.ng.common.client.rendering.layouts.utils.FieldLabelHelper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class I18nTextFieldRenderer extends FieldRenderer {

    @Inject
    private FieldLabelHelper labelHelper;

    @Override
    public String getCode() {
        return "I18nText";
    }

    @Override
    public InputContainer getFieldInput(final FieldDefinition description, final FormContext context) {
        if (description == null) return null;
        final TextBox text = new TextBox();
        text.setName(description.getName());
        text.setId(description.getName());

        final FormContextStatus status = context.getContextStatus();

        String strValue = status.getFieldValue(description.getName());
        final JSONObject jsonValue = new JSONObject(JsonUtils.safeEval(strValue));

        Pair<String, String> label = labelHelper.getLabel(jsonValue);

        final String locale = label.getK1();

        text.setValue(label.getK2());

        JSONObject jsonProperties = new JSONObject(description);

        JSONValue maxLength = jsonProperties.get("maxLength");
        if (maxLength != null) {
            text.setMaxLength(Integer.decode(maxLength.isString().stringValue()));
        }

        JSONValue size = jsonProperties.get("size");
        if (size != null) {
            text.setWidth(size.isString().stringValue() + "em");
        }

        text.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                jsonValue.put(locale, new JSONString(text.getValue()));
                fieldChanged(description, context, jsonValue.toString());
            }
        });
        text.setEnabled(!description.isReadOnly());

        InputContainer inputContainer = new InputContainer(text, getFieldLabel(description), this.supportsLabel(), description, context.getFormDefinition()) {
            public void setReadOnly(boolean readOnly) {
                text.setEnabled(!readOnly);
            }
        };

        return inputContainer;
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
