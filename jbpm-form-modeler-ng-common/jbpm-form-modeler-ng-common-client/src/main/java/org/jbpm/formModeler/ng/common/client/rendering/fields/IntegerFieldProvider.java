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
public class IntegerFieldProvider extends FieldProvider {
    @Override
    public String getCode() {
        return "InputTextInteger";
    }

    @Override
    public Widget getFieldInput(final FieldDescription description) {
        if (description == null) return null;
        final IntegerBox longBox = new IntegerBox();
        longBox.setName(description.getId());
        longBox.setId(description.getId());
        longBox.setMaxLength(100);
        longBox.setWidth("25");
        if (description.getValue() != null && !description.getValue().isEmpty()) longBox.setValue(Integer.decode(description.getValue()));
        longBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                description.setValue(String.valueOf(longBox.getValue()));
            }
        });
        return longBox;
    }
}
