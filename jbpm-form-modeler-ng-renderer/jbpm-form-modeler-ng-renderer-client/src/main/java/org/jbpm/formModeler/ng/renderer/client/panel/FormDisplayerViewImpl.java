package org.jbpm.formModeler.ng.renderer.client.panel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.jbpm.formModeler.ng.common.client.renderer.FormRendererComponent;
import org.jbpm.formModeler.ng.common.client.rendering.FormRendererManager;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.ArrayList;

@Dependent
@Templated("FormDisplayerViewImpl.html")
public class FormDisplayerViewImpl extends Composite implements FormDisplayerPresenter.FormDisplayerView {
    @Inject
    @DataField
    public SimplePanel formContent;

    @Inject
    @DataField
    private Button button;

    @Inject
    @DataField
    private Button buttonEmpty;

    @Inject
    @DataField
    private Button submit;

    @Inject
    FormRendererComponent rendererComponent;

    @Inject
    private FormRendererManager formRendererManager;

    private FormDisplayerPresenter presenter;

    @Inject
    private ArrayList<Widget> inputs;

    @Override
    public void load(String formDescription) {
        if (formDescription == null) {
            Window.alert("Description not found!");
            return;
        }

        if (rendererComponent.renderFormContent(formDescription)) submit.setVisible(true);
    }

    @Override
    public String getMarshalledForm() {
        return rendererComponent.getFormContent();
    }

    @EventHandler("submit")
    public void submit(ClickEvent event) {
        presenter.submitForm();
    }

    @EventHandler("button")
    public void start(ClickEvent event) {
        presenter.startTest();
    }

    @EventHandler("buttonEmpty")
    public void startEmpty(ClickEvent event) {
        presenter.startEmptyTest();
    }

    @Override
    public void init(FormDisplayerPresenter presenter) {
        this.presenter = presenter;
        button.setText("Start Test");
        buttonEmpty.setText("Start Empty Test");
        submit.setVisible(false);
        submit.setText("Submit");
        formContent.add(rendererComponent);
    }
}
