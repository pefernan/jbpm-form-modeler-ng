package org.jbpm.formModeler.ng.services.management.forms;

import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.model.Form;

public interface FieldDefinitionMarshaller {
    String marshall(Field field);
    Field unmarshall(String marshalledField);
}
