package org.jbpm.formModeler.ng.model;

import java.io.Serializable;

public interface FieldValueMarshaller extends Serializable {
    public String marshallValue(Object value);
    public Object unMarshallValue(String marshalledValue);
}
