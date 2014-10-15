package org.jbpm.formModeler.ng.common.client.renderer;


import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
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
import org.jbpm.formModeler.ng.common.client.rendering.fields.FieldRenderer;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContextStatus;
import org.jbpm.formModeler.ng.common.client.rendering.layouts.FormLayoutRenderer;
import org.uberfire.mvp.Command;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

public class FormRendererComponent extends AbstractFormRendererComponent {
    interface FormRendererViewBinder
            extends
            UiBinder<Widget, FormRendererComponent> {

    }

    private static FormRendererViewBinder uiBinder = GWT.create(FormRendererViewBinder.class);

    @UiField
    FormPanel formContent;

    @PostConstruct
    public void initView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    protected Panel getFormContainer() {
        return formContent;
    }
}
