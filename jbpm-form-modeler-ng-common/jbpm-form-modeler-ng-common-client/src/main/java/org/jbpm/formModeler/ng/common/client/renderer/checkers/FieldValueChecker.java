package org.jbpm.formModeler.ng.common.client.renderer.checkers;

import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;

import java.awt.image.renderable.RenderContext;

public interface FieldValueChecker {
    FieldCheckResult checkFieldValue(FieldDefinition field, String value, FormContext context);
}
