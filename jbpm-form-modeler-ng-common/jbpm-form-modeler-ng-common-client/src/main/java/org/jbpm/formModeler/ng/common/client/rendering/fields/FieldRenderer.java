package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.google.gwt.resources.client.ImageResource;
import org.jbpm.formModeler.ng.common.client.rendering.InputContainer;
import org.jbpm.formModeler.ng.common.client.rendering.event.FieldChangedEvent;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.layouts.utils.FieldLabelHelper;
import org.jbpm.formModeler.ng.common.client.rendering.resources.i18n.FieldTypeLabels;
import org.jbpm.formModeler.ng.common.client.rendering.resources.images.FieldTypeImages;

import javax.enterprise.event.Event;
import javax.inject.Inject;

public abstract class FieldRenderer {

    @Inject
    protected FieldLabelHelper fieldLabelHelper;

    @Inject
    protected Event<FieldChangedEvent> changedEvent;

    public abstract String getCode();
    public abstract InputContainer getFieldInput(final FieldDefinition description, final FormContext context);

    public boolean supportsLabel() {
        return false;
    }

    public boolean isVisible() {
        return true;
    }

    public ImageResource getImage() {
        return FieldTypeImages.INSTANCE.defaultImage();
    }

    public String getLabel() {
        return FieldTypeLabels.INSTANCE.defaultLabel();
    }

    public boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public boolean isValidValue(String value) {
        return true;
    }

    public String getFieldLabel(FieldDefinition field) {
        return fieldLabelHelper.getFieldLabel(field);
    }
}
