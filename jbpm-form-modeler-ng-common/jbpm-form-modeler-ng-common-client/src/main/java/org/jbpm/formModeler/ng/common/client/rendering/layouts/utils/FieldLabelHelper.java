package org.jbpm.formModeler.ng.common.client.rendering.layouts.utils;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.json.client.JSONObject;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;

import javax.enterprise.context.Dependent;

@Dependent
public class FieldLabelHelper {
    public String getFieldLabel(FieldDefinition field) {
        JSONObject jsonLabel = new JSONObject(field.getLabel());

        String locale = LocaleInfo.getCurrentLocale().getLocaleName();

        String label;
        if (jsonLabel.isNull() != null && jsonLabel.keySet().size() == 0) label = "";
        else if (jsonLabel.get(locale).isNull() == null) label = jsonLabel.get(locale).isString().stringValue();
        else label = jsonLabel.get("default").isString().stringValue();

        if (field.isRequired()) label = "* " + label;
        return label;
    }
}
