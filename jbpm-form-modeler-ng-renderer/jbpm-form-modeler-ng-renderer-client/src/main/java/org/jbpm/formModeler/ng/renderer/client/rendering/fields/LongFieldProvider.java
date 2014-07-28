package org.jbpm.formModeler.ng.renderer.client.rendering.fields;

import com.github.gwtbootstrap.client.ui.LongBox;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.renderer.client.rendering.FieldDescription;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Portable
public class LongFieldProvider extends FieldProvider {
    @Override
    public String getCode() {
        return "InputTextLong";
    }

    @Override
    public Widget getFieldInput(final FieldDescription description) {
        if (description == null) return null;
        final LongBox longBox = new LongBox();
        longBox.setName(description.getId());
        longBox.setId(description.getId());
        longBox.setMaxLength(100);
        longBox.setWidth("25");
        if (description.getValue() != null && !description.getValue().isEmpty()) longBox.setValue(Long.decode(description.getValue()));
        longBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                description.setValue(String.valueOf(longBox.getValue()));
            }
        });
        return longBox;
    }
}
