package org.jbpm.formModeler.ng.common.client.rendering.layouts;

import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.ControlLabel;
import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.FormLabel;
import com.github.gwtbootstrap.client.ui.constants.FormType;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.jbpm.formModeler.ng.common.client.rendering.FieldProviderManager;
import org.jbpm.formModeler.ng.common.client.rendering.fields.FieldRenderer;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public abstract class FormLayoutRenderer {
    public static final String LABEL_MODE_BEFORE = "before";
    public static final String LABEL_MODE_LEFT = "left";
    public static final String LABEL_MODE_LEFT_ALIGNED = "left-aligned";

    @Inject
    protected FieldProviderManager providerManager;
    protected Map<String, ControlGroup> controlGroups = new HashMap<String, ControlGroup>();


    public abstract String getCode();
    public abstract Panel generateFormContent(FormContext context);

    public ControlGroup getFieldControlGroup(String fieldId) {
        return controlGroups.get(fieldId);
    }

    public Form generateForm(FormContext context) {
        Form form = new Form();

        String labelMode = context.getFormDefinition().getLabelMode();
        if (!labelMode.equals(LABEL_MODE_BEFORE)) {
            form.setType(FormType.HORIZONTAL);
        } else {
            form.setType(FormType.VERTICAL);
        }

        form.add(generateFormContent(context));

        return form;
    }

    protected Panel getFieldBox(FieldDefinition field, FormContext context) {
        ControlGroup controlGroup = buildFieldControlGroup(field, context);
        if (controlGroup == null) return null;

        VerticalPanel panel = new VerticalPanel();
        panel.add(controlGroup);

        return panel;
    }


    private ControlGroup buildFieldControlGroup(FieldDefinition fieldDefinition, FormContext context) {
        FieldRenderer renderer = getFieldRenderer(fieldDefinition);

        if (renderer == null) return null;

        ControlGroup controlGroup = new ControlGroup();

        ControlLabel label = new ControlLabel();
        label.add(getFieldLabel(fieldDefinition));

        if (context.getFormDefinition().getLabelMode().equals(FormLayoutRenderer.LABEL_MODE_LEFT_ALIGNED)) label.getElement().getStyle().setTextAlign(Style.TextAlign.LEFT);

        controlGroup.add(label);
        controlGroup.add(renderer.getFieldInput(fieldDefinition, context));

        controlGroups.put(fieldDefinition.getUid(), controlGroup);

        return controlGroup;
    }

    protected FieldRenderer getFieldRenderer(FieldDefinition field) {
        FieldRenderer renderer = providerManager.getProviderByType(field.getType());

        if (renderer == null) {
            Window.alert("Unable to find renderer for: " + field.getType());
            return null;
        }
        return renderer;
    }

    protected Widget getFieldLabel(FieldDefinition field) {

        JSONObject jsonLabel = new JSONObject(field.getLabel());

        String locale = LocaleInfo.getCurrentLocale().getLocaleName();

        String label;
        if (jsonLabel.isNull() != null && jsonLabel.keySet().size() == 0) label = "";
        else if (jsonLabel.get(locale).isNull() == null) label = jsonLabel.get(locale).isString().stringValue();
        else label = jsonLabel.get("default").isString().stringValue();

        if (field.isRequired()) label = "* " + label;
        FormLabel fieldLabel = new FormLabel(label);
        fieldLabel.setFor(field.getId());
        return fieldLabel;
    }
}
