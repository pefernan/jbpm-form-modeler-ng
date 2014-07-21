package org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers;

import org.jbpm.formModeler.ng.model.FieldValueMarshaller;

public class CharacterMarshaller implements FieldValueMarshaller {
    @Override
    public String marshallValue(Object value) {
        if (value == null) return "";
        return value.toString();
    }

    @Override
    public Object unMarshallValue(String marshalledValue) {
        if (marshalledValue == null || marshalledValue.isEmpty()) return null;
        return new Character(marshalledValue.charAt(0));
    }
}
