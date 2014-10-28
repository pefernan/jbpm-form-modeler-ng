package org.jbpm.formModeler.ng.common.client.renderer;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import org.jbpm.formModeler.ng.common.client.renderer.checkers.FieldCheckResult;
import org.jbpm.formModeler.ng.common.client.rendering.FormLayoutRenderer;
import org.jbpm.formModeler.ng.common.client.rendering.FormRendererManager;
import org.jbpm.formModeler.ng.common.client.rendering.InputContainer;
import org.jbpm.formModeler.ng.common.client.rendering.event.FieldChangedEvent;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContextStatus;
import org.uberfire.mvp.Command;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

public abstract class AbstractFormRendererComponent extends Composite {

    @Inject
    protected FormRendererManager formRendererManager;

    @Inject
    protected FieldValueCheckerManager checkerManager;

    protected FormLayoutRenderer renderer;
    protected FormContext context;
    protected FormContextStatus status;
    protected FieldChangedEvent fieldChangedEvent;
    protected Command onFieldChange;

    protected abstract Panel getFormContainer();

    public boolean renderFormContent(String form) {
        try {
            context = JsonUtils.safeEval(form);

            status = context.getContextStatus();

            setFormLayoutRenderer(context);

            refresh();
            return true;
        } catch (Exception ex) {
            Window.alert("Something wrong happened rendering form: " + ex.getMessage());
        }
        return false;
    }

    public void setFormLayoutRenderer(FormContext ctx) {
        renderer = formRendererManager.getLayoutRendererByType(ctx.getFormDefinition().getLayout().getId());
    }

    public void refresh() {
        Panel content = renderer.generateForm(context);
        Panel formContent = getFormContainer();
        formContent.clear();

        if (content != null) {
            formContent.add(content);
        }
    }

    public void checkFieldValue(@Observes FieldChangedEvent fieldChangedEvent) {
        if (context != null && fieldChangedEvent.getCtxUID().equals(context.getCtxUID())) {
            this.fieldChangedEvent = fieldChangedEvent;
            status.setFieldValue(fieldChangedEvent.getFieldName(), fieldChangedEvent.getNewValue());

            FieldDefinition fieldDefinition = context.getFormDefinition().getFieldDefinition(fieldChangedEvent.getFieldId());
            FieldCheckResult checkresult = checkerManager.checkFieldValue(fieldDefinition, fieldChangedEvent.getNewValue(), context);

            InputContainer container = renderer.getInputContainer(fieldChangedEvent.getFieldId());
            if (checkresult.isWrong()) {
                container.setWrong(true);
                container.setHelpMessage(checkresult.getMessage());
                context.getContextStatus().addWrongField(fieldChangedEvent.getFieldId());
            } else if (context.getContextStatus().isFieldWrong(fieldChangedEvent.getFieldId())) {
                container.setWrong(false);
                container.setHelpMessage("");
                context.getContextStatus().removeWrongField(fieldChangedEvent.getFieldId());
            }
            if (onFieldChange != null) onFieldChange.execute();
        }
    }

    public FormLayoutRenderer getRenderer() {
        return renderer;
    }

    public boolean hasWrongFields() {
        return status.hasWrongFields();
    }

    public String getFormValues() {
        if (status.hasWrongFields()) return "";
        return new JSONObject(status.getValues()).toString();
    }

    public void setOnFieldChange(Command onFieldChange) {
        this.onFieldChange = onFieldChange;
    }

    public FieldChangedEvent getFieldChangedEvent() {
        return fieldChangedEvent;
    }
}
