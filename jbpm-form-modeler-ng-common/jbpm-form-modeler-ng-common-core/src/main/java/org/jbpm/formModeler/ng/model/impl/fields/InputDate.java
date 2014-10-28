package org.jbpm.formModeler.ng.model.impl.fields;

import org.jbpm.formModeler.ng.model.ComplexField;
import org.jbpm.formModeler.ng.model.FieldValueMarshaller;
import org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers.DateMarshaller;

import javax.inject.Inject;
import java.util.Date;

public class InputDate extends ComplexField {
    private Integer size = 15;

    @Inject
    private DateMarshaller marshaller;

    @Override
    public String getCode() {
        return "InputDate";
    }

    @Override
    public String getFieldClass() {
        return Date.class.getName();
    }

    @Override
    public FieldValueMarshaller getMarshaller() {
        return marshaller;
    }

    @Override
    public void setMarshaller(FieldValueMarshaller marshaller) {
        if (marshaller instanceof DateMarshaller) this.marshaller = (DateMarshaller) marshaller;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
