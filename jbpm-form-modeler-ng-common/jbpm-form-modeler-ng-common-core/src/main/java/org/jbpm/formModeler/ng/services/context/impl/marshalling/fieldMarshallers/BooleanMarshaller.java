package org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers;

import org.jbpm.formModeler.ng.model.FieldValueMarshaller;

public class BooleanMarshaller implements FieldValueMarshaller {
    @Override
    public String marshallValue(Object value) {
        if (value == null) return Boolean.FALSE.toString();
        return value.toString();
    }

    @Override
    public Object unMarshallValue(String marshalledValue) {
        return Boolean.valueOf(marshalledValue);
    }
}
