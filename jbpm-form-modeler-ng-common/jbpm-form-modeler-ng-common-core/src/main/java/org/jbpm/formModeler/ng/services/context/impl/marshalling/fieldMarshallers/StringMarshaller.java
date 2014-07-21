package org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers;

import org.jbpm.formModeler.ng.model.FieldValueMarshaller;

public class StringMarshaller implements FieldValueMarshaller {
    @Override
    public String marshallValue(Object value) {
        if (value == null) return "";
        return value.toString();
    }

    @Override
    public Object unMarshallValue(String marshalledValue) {
        return marshalledValue;
    }
}
