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
    private FieldLabelHelper fieldLabelHelper;

    @Inject
    private Event<FieldChangedEvent> changedEvent;

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

    public boolean isEmpty(Object value) {
        return value == null;
    }

    public boolean isValidValue(String value) {
        return true;
    }

    public String getFieldLabel(FieldDefinition field) {
        return fieldLabelHelper.getFieldLabel(field);
    }

    protected void fieldChanged(FieldDefinition definition, FormContext context, String value) {
        changedEvent.fire(new FieldChangedEvent(context.getCtxUID(), definition.getId(), definition.getName(), value));
    }
}
