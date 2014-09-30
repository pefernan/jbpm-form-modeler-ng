package org.jbpm.formModeler.ng.services.management.forms;

import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;

import java.util.Map;

public interface SelectValuesProvider {
    String getName();
    Map<String, String> getSelectOptions(Field field, FormRenderContext renderContext);
}
