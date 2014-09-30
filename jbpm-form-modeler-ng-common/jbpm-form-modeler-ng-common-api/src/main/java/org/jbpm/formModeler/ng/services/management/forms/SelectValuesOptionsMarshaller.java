package org.jbpm.formModeler.ng.services.management.forms;

import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;

public interface SelectValuesOptionsMarshaller {
    public String marshallOptionsForField(Field field, FormRenderContext context);
}
