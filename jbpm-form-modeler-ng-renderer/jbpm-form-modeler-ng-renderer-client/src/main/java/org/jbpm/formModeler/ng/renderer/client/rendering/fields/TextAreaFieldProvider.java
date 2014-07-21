package org.jbpm.formModeler.ng.renderer.client.rendering.fields;

import com.github.gwtbootstrap.client.ui.TextArea;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.renderer.client.rendering.FieldDescription;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Portable
public class TextAreaFieldProvider extends FieldProvider {

    @Override
    public String getCode() {
        return "InputTextArea";
    }

    @Override
    public Widget getFieldInput(final FieldDescription description) {
        if (description == null) return null;
        final TextArea textArea = new TextArea();
        textArea.setName(description.getId());
        textArea.setId(description.getId());
        textArea.setText(description.getValue());
        textArea.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                description.setValue(textArea.getValue());
            }
        });
        return textArea;
    }
}
