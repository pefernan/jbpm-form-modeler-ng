package org.jbpm.formModeler.ng.renderer.client.rendering.fields;

import com.github.gwtbootstrap.client.ui.CheckBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.renderer.client.rendering.FieldDescription;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Portable
public class CheckboxFieldProvider extends FieldProvider {
    @Override
    public String getCode() {
        return "CheckBox";
    }

    @Override
    public Widget getFieldInput(final FieldDescription description) {
        if (description == null) return null;

        final CheckBox checkBox = new CheckBox(new SafeHtml() {
            @Override
            public String asString() {
                return description.getLabel();
            }
        });
        checkBox.setName(description.getId());
        checkBox.setId(description.getId());
        checkBox.setValue(Boolean.valueOf(description.getValue()));
        checkBox.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent changeEvent) {
                Boolean value = checkBox.getValue();
                if (value == null) description.setValue("");
                else description.setValue(value.toString());
            }
        });
        return checkBox;
    }

    @Override
    public boolean supportsLabel() {
        return true;
    }
}