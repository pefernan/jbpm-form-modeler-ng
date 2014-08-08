package org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers;

import org.jbpm.formModeler.ng.model.FieldValueMarshaller;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;

import java.util.Date;

public class DateMarshaller implements FieldValueMarshaller{
    @Override
    public String marshallValue(Object value, FormRenderContext context) {
        if (value == null) return null;
        return String.valueOf(((Date)value).getTime());
    }

    @Override
    public Object unMarshallValue(String marshalledValue, Object previousValue, FormRenderContext context) {
        if (marshalledValue == null || marshalledValue.equals("")) return null;
        return new Date(Long.decode(marshalledValue));
    }
}
