package org.jbpm.formModeler.ng.model.impl;

import org.jbpm.formModeler.ng.model.FieldValueMarshaller;
import org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers.NumberMarshaller;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigInteger;

public class InputTextBigInteger extends InputText {
    @Inject
    private NumberMarshaller marshaller;

    @PostConstruct
    protected void init() {
        marshaller.setDesiredClassName(getFieldClass());
        marshaller.setPattern("");
    }

    @Override
    public String getCode() {
        return "InputTextBigInteger";
    }

    @Override
    public String getIcon() {
        return "box_number.png";
    }

    @Override
    public String getFieldClass() {
        return BigInteger.class.getName();
    }

    @Override
    public FieldValueMarshaller getMarshaller() {
        return marshaller;
    }
}
