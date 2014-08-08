package org.jbpm.formModeler.ng.model;

import org.jbpm.formModeler.ng.services.context.FormRenderContext;

import java.io.Serializable;

public interface FieldValueMarshaller extends Serializable {
    public String marshallValue(Object value, FormRenderContext context);
    public Object unMarshallValue(String marshalledValue, Object previousValue, FormRenderContext context);
}
