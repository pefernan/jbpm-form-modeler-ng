package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.github.gwtbootstrap.client.ui.IntegerBox;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.common.client.rendering.FieldDescription;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Portable
public class IntegerFieldRenderer extends FieldRenderer {
    @Override
    public String getCode() {
        return "InputTextInteger";
    }

    @Override
    public Widget getFieldInput(final FieldDescription description) {
        if (description == null) return null;
        final IntegerBox intbox = new IntegerBox();
        intbox.setName(description.getId());
        intbox.setId(description.getId());
        intbox.setMaxLength(100);
        intbox.setWidth("25");
        if (description.getValue() != null && !description.getValue().isEmpty()) intbox.setValue(Integer.decode(description.getValue()));
        intbox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                description.setValue(String.valueOf(intbox.getValue()));
            }
        });
        return intbox;
    }
}
