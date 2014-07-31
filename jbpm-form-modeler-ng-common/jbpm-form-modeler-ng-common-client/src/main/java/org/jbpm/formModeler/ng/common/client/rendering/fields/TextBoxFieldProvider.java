package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.common.client.rendering.FieldDescription;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Portable
public class TextBoxFieldProvider extends FieldProvider {
    @Override
    public String getCode() {
        return "InputText";
    }

    @Override
    public Widget getFieldInput(final FieldDescription description) {
        if (description == null) return null;
        final TextBox text = new TextBox();
        text.setName(description.getId());
        text.setId(description.getId());
        text.setMaxLength(4000);
        text.setWidth("25");
        text.setText(description.getValue());
        text.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                description.setValue(text.getValue());
            }
        });
        return text;
    }
}
