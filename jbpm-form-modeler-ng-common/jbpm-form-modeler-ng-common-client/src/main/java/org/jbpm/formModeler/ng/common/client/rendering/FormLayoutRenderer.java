package org.jbpm.formModeler.ng.common.client.rendering;

import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.constants.FormType;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
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

    protected Map<String, InputContainer> inputContainers = new HashMap<String, InputContainer>();

    public abstract String getCode();
    public abstract Panel generateFormContent(FormContext context);

    public InputContainer getInputContainer(String fieldId) {
        return inputContainers.get(fieldId);
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

        FieldRenderer renderer = getFieldRenderer(field);

        if (renderer == null) return null;

        InputContainer container = renderer.getFieldInput(field, context);

        inputContainers.put(field.getId(), container);

        return container.getControlGroupPanel();
    }


    protected FieldRenderer getFieldRenderer(FieldDefinition field) {
        FieldRenderer renderer = providerManager.getProviderByType(field.getCode());

        if (renderer == null) {
            Window.alert("Unable to find renderer for: " + field.getCode());
            return null;
        }
        return renderer;
    }

    public FieldProviderManager getProviderManager() {
        return providerManager;
    }
}
