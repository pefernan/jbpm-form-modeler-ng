package org.jbpm.formModeler.ng.common.client.rendering.fields;

import com.github.gwtbootstrap.client.ui.CheckBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Widget;
import org.jbpm.formModeler.ng.common.client.rendering.event.FieldChangedEvent;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContextStatus;
import org.jbpm.formModeler.ng.common.client.rendering.layouts.FormLayoutRenderer;
import org.jbpm.formModeler.ng.common.client.rendering.layouts.utils.FieldLabelHelper;
import org.jbpm.formModeler.ng.common.client.rendering.resources.i18n.FieldTypeLabels;
import org.jbpm.formModeler.ng.common.client.rendering.resources.images.FieldTypeImages;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@ApplicationScoped
public class CheckboxFieldRenderer extends FieldRenderer {
    @Inject
    private Event<FieldChangedEvent> changedEvent;

    @Inject
    private FieldLabelHelper labelHelper;

    @Override
    public String getCode() {
        return "CheckBox";
    }

    @Override
    public Widget getFieldInput(final FieldDefinition description, final FormContext context) {
        if (description == null) return null;

        String label = null;
        if (context.getFormDefinition().getLabelMode().equals(FormLayoutRenderer.LABEL_MODE_DEFAULT)) {
            label = labelHelper.getFieldLabel(description);
        }

        final CheckBox checkBox = new CheckBox(label);

        checkBox.setName(description.getId());
        checkBox.setId(description.getId());

        final FormContextStatus status = context.getContextStatus();

        Boolean value = Boolean.valueOf(status.getFieldValue(description.getId()));

        checkBox.setValue(value);
        checkBox.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent changeEvent) {
                changedEvent.fire(new FieldChangedEvent(context.getCtxUID(), description.getId(),  String.valueOf(checkBox.getValue())));
            }
        });
        checkBox.setEnabled(!description.isReadOnly());
        return checkBox;
    }

    @Override
    public boolean supportsLabel() {
        return true;
    }

    @Override
    public ImageResource getImage() {
        return FieldTypeImages.INSTANCE.checkbox();
    }

    public String getLabel() {
        return FieldTypeLabels.INSTANCE.checkbox();
    }
}