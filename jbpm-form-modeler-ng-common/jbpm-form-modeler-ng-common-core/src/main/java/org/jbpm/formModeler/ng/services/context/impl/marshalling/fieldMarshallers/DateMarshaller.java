package org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers;

import org.jbpm.formModeler.ng.model.FieldValueMarshaller;

import java.util.Date;

public class DateMarshaller implements FieldValueMarshaller{
    @Override
    public String marshallValue(Object value) {
        if (value == null) return null;
        return String.valueOf(((Date)value).getTime());
    }

    @Override
    public Object unMarshallValue(String marshalledValue) {
        if (marshalledValue == null || marshalledValue.equals("")) return null;
        return new Date(Long.decode(marshalledValue));
    }
}
