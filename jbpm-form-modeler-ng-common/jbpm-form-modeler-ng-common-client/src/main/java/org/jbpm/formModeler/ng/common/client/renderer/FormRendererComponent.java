package org.jbpm.formModeler.ng.common.client.renderer;


import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import javax.annotation.PostConstruct;

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
