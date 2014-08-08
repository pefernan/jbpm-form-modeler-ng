package org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers;

import org.jbpm.formModeler.ng.model.FieldValueMarshaller;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;

public class BooleanMarshaller implements FieldValueMarshaller {
    @Override
    public String marshallValue(Object value, FormRenderContext context) {
        if (value == null) return Boolean.FALSE.toString();
        return value.toString();
    }

    @Override
    public Object unMarshallValue(String marshalledValue, Object previousValue, FormRenderContext context) {
        return Boolean.valueOf(marshalledValue);
    }
}
