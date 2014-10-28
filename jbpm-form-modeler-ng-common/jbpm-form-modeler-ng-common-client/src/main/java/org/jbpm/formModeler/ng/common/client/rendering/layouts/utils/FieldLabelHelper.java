package org.jbpm.formModeler.ng.common.client.rendering.layouts.utils;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.json.client.JSONObject;
import org.uberfire.commons.data.Pair;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;

import javax.enterprise.context.Dependent;

@Dependent
public class FieldLabelHelper {
    public String getFieldLabel(FieldDefinition field) {
        Pair<String, String> labelPair = getLabel(new JSONObject(JsonUtils.safeEval(field.getLabel())));
        String label = labelPair.getK2();
        if (field.isRequired()) label = "* " + label;
        return label;
    }

    public Pair<String, String> getLabel(JSONObject jsonLabel) {
        String locale = LocaleInfo.getCurrentLocale().getLocaleName();
        if (!(jsonLabel.isNull() != null && jsonLabel.keySet().size() == 0))  {
            Pair<String, String> labelValue = getLabel(jsonLabel, locale);
            if (labelValue == null) labelValue = getLabel(jsonLabel, "default");
            if (labelValue == null) labelValue = getLabel(jsonLabel, "en");
            return labelValue;
        }
        return new Pair<String, String>("", "");
    }

    protected Pair<String, String> getLabel(JSONObject jsonLabel, String locale) {
        if (jsonLabel.keySet().contains(locale)) return new Pair<String, String>(locale, jsonLabel.get(locale).isString().stringValue());
        return null;
    }
}
