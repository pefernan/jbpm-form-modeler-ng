package org.jbpm.formModeler.ng.common.client.rendering.layouts;

import com.github.gwtbootstrap.client.ui.*;
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
import org.jbpm.formModeler.ng.common.client.rendering.layouts.utils.FieldLabelHelper;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public abstract class FormLayoutRenderer {
    public static final String LABEL_MODE_DEFAULT = "default";
    public static final String LABEL_MODE_LEFT = "left";
    public static final String LABEL_MODE_LEFT_ALIGNED = "left-aligned";

    @Inject
    protected FieldProviderManager providerManager;

    @Inject
    private FieldLabelHelper labelHelper;

    protected Map<String, ControlGroup> controlGroups = new HashMap<String, ControlGroup>();

    public abstract String getCode();
    public abstract Panel generateFormContent(FormContext context);

    public ControlGroup getFieldControlGroup(String fieldId) {
        return controlGroups.get(fieldId);
    }

    public Form generateForm(FormContext context) {
        Form form = new Form();

        String labelMode = context.getFormDefinition().getLabelMode();

        if (labelMode.equals(LABEL_MODE_DEFAULT)) {
            form.setType(FormType.VERTICAL);
        } else {
            form.setType(FormType.HORIZONTAL);
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

        if (!(context.getFormDefinition().getLabelMode().equals(FormLayoutRenderer.LABEL_MODE_DEFAULT) && renderer.supportsLabel())) {
            String fieldLabel = labelHelper.getFieldLabel(fieldDefinition);
            ControlLabel label = new ControlLabel();
            FormLabel formLabel = new FormLabel(fieldLabel);
            formLabel.setFor(fieldDefinition.getId());
            label.add(formLabel);
            if (context.getFormDefinition().getLabelMode().equals(FormLayoutRenderer.LABEL_MODE_LEFT_ALIGNED)) label.getElement().getStyle().setTextAlign(Style.TextAlign.LEFT);
            controlGroup.add(label);
        }

        Controls controls = new Controls();
        controls.add(renderer.getFieldInput(fieldDefinition, context));
        controls.add(new HelpBlock());
        controlGroup.add(controls);

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
}
