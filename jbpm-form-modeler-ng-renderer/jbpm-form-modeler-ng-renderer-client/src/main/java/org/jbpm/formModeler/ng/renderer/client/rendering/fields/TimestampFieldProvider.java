package org.jbpm.formModeler.ng.renderer.client.rendering.fields;

import com.github.gwtbootstrap.datetimepicker.client.ui.DateTimeBox;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.renderer.client.rendering.FieldDescription;

import javax.enterprise.context.ApplicationScoped;
import java.util.Date;

@ApplicationScoped
@Portable
public class TimestampFieldProvider extends FieldProvider {
    @Override
    public String getCode() {
        return "InputDate";
    }

    @Override
    public Widget getFieldInput(final FieldDescription description) {
        if (description == null) return null;
        final DateTimeBox datetimepicker = new DateTimeBox();
        datetimepicker.setId(description.getId());
        datetimepicker.setWidth("25");
        if (description.getValue() != null && !description.getValue().isEmpty()) datetimepicker.setValue(new Date(Long.decode(description.getValue())));
        datetimepicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> dateValueChangeEvent) {
                Date value = dateValueChangeEvent.getValue();
                if (value == null)description.setValue("");
                else description.setValue(String.valueOf(value.getTime()));
            }
        });
        return datetimepicker;
    }
}
