package org.jbpm.formModeler.ng.model.impl.fields;

import org.jbpm.formModeler.ng.model.BasicField;
import org.jbpm.formModeler.ng.model.FieldValueMarshaller;
import org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers.BooleanMarshaller;

import javax.inject.Inject;

public class CheckBox extends BasicField {
    @Inject
    private BooleanMarshaller marshaller;

    @Override
    public String getCode() {
        return "CheckBox";
    }

    @Override
    public String getFieldClass() {
        return Boolean.class.getName();
    }

    @Override
    public FieldValueMarshaller getMarshaller() {
        return marshaller;
    }

    @Override
    public void setMarshaller(FieldValueMarshaller marshaller) {
        if (marshaller instanceof BooleanMarshaller) this.marshaller = (BooleanMarshaller) marshaller;
    }
}
