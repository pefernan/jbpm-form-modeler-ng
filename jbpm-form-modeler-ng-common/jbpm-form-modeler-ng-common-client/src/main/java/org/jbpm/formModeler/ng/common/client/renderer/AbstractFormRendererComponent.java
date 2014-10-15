package org.jbpm.formModeler.ng.common.client.renderer;

import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import org.jbpm.formModeler.ng.common.client.renderer.checkers.FieldCheckResult;
import org.jbpm.formModeler.ng.common.client.rendering.FormRendererManager;
import org.jbpm.formModeler.ng.common.client.rendering.event.FieldChangedEvent;
import org.jbpm.formModeler.ng.common.client.rendering.fields.FieldRenderer;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContextStatus;
import org.jbpm.formModeler.ng.common.client.rendering.layouts.FormLayoutRenderer;
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
            Panel formContent = getFormContainer();
            formContent.clear();

            context = JsonUtils.safeEval(form);

            status = context.getContextStatus();

            setFormLayoutRenderer(context);

            Panel content = renderer.generateForm(context);

            if (content != null) {
                formContent.add(content);
                return true;
            }
        } catch (Exception ex) {
            Window.alert("Something wrong happened rendering form: " + ex.getMessage());
        }
        return false;
    }


    public void setFormLayoutRenderer(FormContext ctx) {
        renderer = formRendererManager.getLayoutRendererByType(ctx.getFormDefinition().getLayout().getId());
    }

    public void checkFieldValue(@Observes FieldChangedEvent fieldChangedEvent) {
        if (context != null && fieldChangedEvent.getCtxUID().equals(context.getCtxUID())) {
            this.fieldChangedEvent = fieldChangedEvent;
            status.setFieldValue(fieldChangedEvent.getFieldName(), fieldChangedEvent.getNewValue());

            FieldDefinition fieldDefinition = context.getFormDefinition().getFieldDefinition(fieldChangedEvent.getFieldUid());
            FieldCheckResult checkresult = checkerManager.checkFieldValue(fieldDefinition, fieldChangedEvent.getNewValue(), context);

            if (checkresult.isWrong()) {
                renderer.getFieldControlGroup(fieldChangedEvent.getFieldUid()).setType(ControlGroupType.ERROR);
                context.getContextStatus().addWrongField(fieldChangedEvent.getFieldUid());
                Window.alert(checkresult.getMessage());
            } else if (context.getContextStatus().isFieldWrong(fieldChangedEvent.getFieldUid())) {
                renderer.getFieldControlGroup(fieldChangedEvent.getFieldUid()).setType(ControlGroupType.NONE);
                context.getContextStatus().removeWrongField(fieldChangedEvent.getFieldUid());
            }
            if (onFieldChange != null) onFieldChange.execute();
        }
    }

    public String getFormValues() {
        return new JSONObject(status.getValues()).toString();
    }

    public void setOnFieldChange(Command onFieldChange) {
        this.onFieldChange = onFieldChange;
    }

    public FieldChangedEvent getFieldChangedEvent() {
        return fieldChangedEvent;
    }
}
