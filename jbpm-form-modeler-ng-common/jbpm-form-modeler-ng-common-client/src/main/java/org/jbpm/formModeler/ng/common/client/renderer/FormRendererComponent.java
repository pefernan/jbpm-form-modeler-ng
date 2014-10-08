package org.jbpm.formModeler.ng.common.client.renderer;


import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import org.jbpm.formModeler.ng.common.client.rendering.FormRendererManager;
import org.jbpm.formModeler.ng.common.client.rendering.event.FieldChangedEvent;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContextStatus;
import org.jbpm.formModeler.ng.common.client.rendering.layouts.FormLayoutRenderer;
import org.uberfire.mvp.Command;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

public class FormRendererComponent extends Composite {
    interface FormRendererViewBinder
            extends
            UiBinder<Widget, FormRendererComponent> {

    }

    private static FormRendererViewBinder uiBinder = GWT.create(FormRendererViewBinder.class);

    @UiField
    FormPanel formContent;

    @Inject
    private FormRendererManager formRendererManager;

    private FormContext context;

    private FormContextStatus status;

    private FieldChangedEvent fieldChangedEvent;

    private Command onFieldChange;

    @PostConstruct
    public void initView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public boolean renderFormContent(String form) {
        try {
            formContent.clear();

            context = JsonUtils.safeEval(form);

            status = context.getContextStatus();

            FormLayoutRenderer layoutRenderer = formRendererManager.getLayoutRendererByType(context.getFormDefinition().getLayout().getId());

            Panel content = layoutRenderer.generateForm(context);

            if (content != null) {
                formContent.add(content);
                return true;
            }
        } catch (Exception ex) {
            Window.alert("Something wrong happened rendering form: " + ex.getMessage());
        }
        return false;
    }

    public void checkFieldValue(@Observes FieldChangedEvent fieldChangedEvent) {
        if (context != null && fieldChangedEvent.getCtxUID().equals(context.getCtxUID())) {
            this.fieldChangedEvent = fieldChangedEvent;
            status.setFieldValue(fieldChangedEvent.getFieldId(), fieldChangedEvent.getNewValue());
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
