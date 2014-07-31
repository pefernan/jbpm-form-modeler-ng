package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.github.gwtbootstrap.datepicker.client.ui.DateBox;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.common.client.rendering.FieldDescription;

import javax.enterprise.context.ApplicationScoped;
import java.util.Date;

@ApplicationScoped
@Portable
public class ShortDateFieldProvider extends FieldProvider {
    @Override
    public String getCode() {
        return "InputShortDate";
    }

    @Override
    public Widget getFieldInput(final FieldDescription description) {
        if (description == null) return null;
        final DateBox datepicker = new DateBox();
        datepicker.setId(description.getId());
        datepicker.setWidth("25");
        if (description.getValue() != null && !description.getValue().isEmpty()) datepicker.setValue(new Date(Long.decode(description.getValue())));
        datepicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> dateValueChangeEvent) {
                Date value = dateValueChangeEvent.getValue();
                if (value == null) description.setValue("");
                else description.setValue(String.valueOf(value.getTime()));
            }
        });
        return datepicker;
    }
}
