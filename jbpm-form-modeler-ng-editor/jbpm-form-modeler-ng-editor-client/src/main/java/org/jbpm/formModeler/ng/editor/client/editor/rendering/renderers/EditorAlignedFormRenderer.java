package org.jbpm.formModeler.ng.editor.client.editor.rendering.renderers;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jbpm.formModeler.ng.common.client.rendering.FieldDescription;
import org.jbpm.formModeler.ng.common.client.rendering.renderers.AlignedFormRenderer;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EditorAlignedFormRenderer extends AlignedFormRenderer {
    @Override
    public String getCode() {
        return "editor-" + super.getCode();
    }

    @Override
    protected Widget getFieldLabel(FieldDescription field) {
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.add(super.getFieldLabel(field));
        HTML holder = new HTML();
        holder.setHeight("20px");
        holder.setWidth("20px");
        holder.getElement().getStyle().setBackgroundColor(field.getHolderColor());
        horizontalPanel.add(holder);
        return horizontalPanel;
    }
}