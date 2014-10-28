package org.jbpm.formModeler.ng.common.client.renderer.checkers;

import org.jbpm.formModeler.ng.common.client.renderer.checkers.i18n.CheckerConstants;
import org.jbpm.formModeler.ng.common.client.rendering.FieldProviderManager;
import org.jbpm.formModeler.ng.common.client.rendering.fields.FieldRenderer;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.layouts.utils.FieldLabelHelper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class DefaultValueChecker implements FieldValueChecker {
    @Inject
    private FieldLabelHelper fieldLabelHelper;

    @Inject
    private FieldProviderManager fieldProviderManager;

    @Override
    public FieldCheckResult checkFieldValue(FieldDefinition field, String value, FormContext context) {
        FieldRenderer renderer = fieldProviderManager.getProviderByType(field.getCode());
        if (field.isRequired() && renderer.isEmpty(value)) return new FieldCheckResult(true, CheckerConstants.INSTANCE.requiredError(fieldLabelHelper.getFieldLabel(field)));

        if (!renderer.isValidValue(value)) return new FieldCheckResult(true, CheckerConstants.INSTANCE.fieldFormat(fieldLabelHelper.getFieldLabel(field)));

        return new FieldCheckResult(false, null);
    }
}
