package org.jbpm.formModeler.ng.renderer.client.panel;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.jbpm.formModeler.ng.common.client.rendering.FormDescription;
import org.jbpm.formModeler.ng.common.client.rendering.FormRendererManager;
import org.jbpm.formModeler.ng.common.client.rendering.Renderer;
import org.jbpm.formModeler.ng.common.client.rendering.renderers.FormRenderer;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.ArrayList;

@Dependent
@Templated("FormDisplayerViewImpl.html")
public class FormDisplayerViewImpl extends Composite implements FormDisplayerPresenter.FormDisplayerView {
    @Inject
    @DataField
    public FormPanel formContent;

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
    @Renderer
    private FormRendererManager formRendererManager;

    private FormDisplayerPresenter presenter;

    @Inject
    private ArrayList<Widget> inputs;

    @Override
    public void load(FormDescription formDescription) {
        formContent.clear();
        if (formDescription == null) {
            Window.alert("Description not found!");
            return;
        }

        FormRenderer renderer = formRendererManager.getRendererByType(formDescription.getDisplayMode());

        Panel content = renderer.generateForm(formDescription);

        if (content != null) {
            formContent.add(content);
            submit.setVisible(true);
        }
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
    }
}
