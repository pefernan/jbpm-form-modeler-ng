package org.jbpm.formModeler.ng.model;

public interface FieldValueMarshaller {
    public String marshallValue(Object value);
    public Object unMarshallValue(String marshalledValue);
}
