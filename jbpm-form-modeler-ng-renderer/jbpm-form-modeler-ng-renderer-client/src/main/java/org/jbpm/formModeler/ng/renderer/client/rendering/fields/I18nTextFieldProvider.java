package org.jbpm.formModeler.ng.renderer.client.rendering.fields;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.renderer.client.rendering.FieldDescription;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Portable
public class I18nTextFieldProvider extends FieldProvider {
    @Override
    public String getCode() {
        return "I18nText";
    }

    @Override
    public Widget getFieldInput(FieldDescription description) {
        if (description == null) return null;
        TextBox text = new TextBox();
        text.setName(description.getId());
        text.setId(description.getId());
        text.setMaxLength(4000);
        text.setWidth("25");
        text.setValue(description.getValue());
        return text;
    }
}
