package org.jbpm.formModeler.ng.model.impl.fields;

import org.jbpm.formModeler.ng.model.FieldValueMarshaller;
import org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers.NumberMarshaller;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;

public class InputTextBigDecimal extends InputText {
    @Inject
    private NumberMarshaller marshaller;

    @PostConstruct
    protected void init() {
        marshaller.setDesiredClassName(getFieldClass());
        marshaller.setPattern("###.##");
    }

    @Override
    public String getCode() {
        return "InputTextBigDecimal";
    }

    @Override
    public String getIcon() {
        return "box_number.png";
    }

    @Override
    public String getFieldClass() {
        return BigDecimal.class.getName();
    }

    @Override
    public FieldValueMarshaller getMarshaller() {
        return marshaller;
    }
}
