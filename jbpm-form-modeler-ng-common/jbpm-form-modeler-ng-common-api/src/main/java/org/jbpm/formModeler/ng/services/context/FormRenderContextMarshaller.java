package org.jbpm.formModeler.ng.services.context;

public interface FormRenderContextMarshaller {
    String marshallContext(FormRenderContext context);
    void unmarshallContext(FormRenderContext context, String marshalledValues);
}
