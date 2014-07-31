package org.jbpm.formModeler.ng.services.context;

import java.io.Serializable;

public interface FormRenderContextMarshaller extends Serializable {
    String marshallContext(FormRenderContext context);
    void unmarshallContext(FormRenderContext context, String marshalledValues);
}
