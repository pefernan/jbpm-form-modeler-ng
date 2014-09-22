package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.github.gwtbootstrap.client.ui.CheckBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContextStatus;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Portable
public class CheckboxFieldRenderer extends FieldRenderer {
    @Override
    public String getCode() {
        return "CheckBox";
    }

    @Override
    public Widget getFieldInput(final FieldDefinition description, FormContext context) {
        if (description == null) return null;

        final CheckBox checkBox = new CheckBox(new SafeHtml() {
            @Override
            public String asString() {
                JSONObject jsonLabel = new JSONObject(description.getLabel());

                String locale = LocaleInfo.getCurrentLocale().getLocaleName();

                if (jsonLabel.get(locale).isNull() == null) return jsonLabel.get(locale).isString().stringValue();
                return jsonLabel.get("default").isString().stringValue();
            }
        });
        checkBox.setName(description.getId());
        checkBox.setId(description.getId());

        final FormContextStatus status = context.getContextStatus();

        Boolean value = Boolean.valueOf(status.getFieldValue(description.getId()));

        checkBox.setValue(value);
        checkBox.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent changeEvent) {
                status.setFieldValue(description.getId(), String.valueOf(checkBox.getValue()));
            }
        });
        return checkBox;
    }

    @Override
    public boolean supportsLabel() {
        return true;
    }
}