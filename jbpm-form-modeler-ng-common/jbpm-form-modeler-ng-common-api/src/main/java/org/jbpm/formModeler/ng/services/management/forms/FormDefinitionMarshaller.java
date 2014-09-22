package org.jbpm.formModeler.ng.services.management.forms;

import org.jbpm.formModeler.ng.model.Form;

import java.util.Map;

/**
 * Marshalls forms.
 */
public interface FormDefinitionMarshaller {
    String marshall(Form form);
    Form unmarshall(String marshalledForm, Map<String, Object> context);
}
