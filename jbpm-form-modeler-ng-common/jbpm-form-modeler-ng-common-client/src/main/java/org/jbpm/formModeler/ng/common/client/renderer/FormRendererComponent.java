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
import org.jbpm.formModeler.ng.common.client.rendering.FormDescription;
import org.jbpm.formModeler.ng.common.client.rendering.FormRendererManager;
import org.jbpm.formModeler.ng.common.client.rendering.renderers.FormRenderer;

import javax.annotation.PostConstruct;
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

    private FormDescription description;

    @PostConstruct
    public void initView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public boolean renderFormContent(String form) {
        try {
            FormRenderer renderer = formRendererManager.getRendererByType(description.getDisplayMode());

            formContent.clear();

            description = JsonUtils.safeEval(form);

            Panel content = renderer.generateForm(description);

            if (content != null) {
                formContent.add(content);
                return true;
            }
        } catch (Exception ex) {
            Window.alert("Something wrong happened: " + ex.getMessage());
        }
        return false;
    }

    public String getFormContent() {
        return new JSONObject(description).toString();
    }
}
