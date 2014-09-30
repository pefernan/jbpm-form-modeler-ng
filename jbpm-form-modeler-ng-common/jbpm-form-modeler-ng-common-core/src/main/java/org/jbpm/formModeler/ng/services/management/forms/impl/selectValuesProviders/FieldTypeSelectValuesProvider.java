package org.jbpm.formModeler.ng.services.management.forms.impl.selectValuesProviders;

import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;
import org.jbpm.formModeler.ng.services.management.forms.FieldManager;
import org.jbpm.formModeler.ng.services.management.forms.SelectValuesProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Dependent
public class FieldTypeSelectValuesProvider implements SelectValuesProvider {
    @Inject
    FieldManager fieldManager;

    @PostConstruct

    @Override
    public String getName() {
        return "Field Values Provider";
    }

    @Override
    public Map<String, String> getSelectOptions(Field field, FormRenderContext renderContext) {
        Map<String, String> result = new HashMap<String, String>();

        Field editionField = (Field) renderContext.getInputData().get("field");

        if (editionField != null) {
            for (Field compatibleField : fieldManager.getSuitableFields(editionField)) {
                result.put(compatibleField.getCode(), compatibleField.getCode());
            }
        }

        return result;
    }
}
