package org.jbpm.formModeler.ng.editor.client.editor.rendering.renderers;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;
import org.jbpm.formModeler.ng.common.client.rendering.FieldDescription;
import org.jbpm.formModeler.ng.common.client.rendering.FormDescription;
import org.jbpm.formModeler.ng.common.client.rendering.renderers.DefaultFormRenderer;
import org.jbpm.formModeler.ng.editor.events.canvas.StartEditFieldPropertyEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@ApplicationScoped
public class EditorDefaultFormRenderer extends DefaultFormRenderer {
    @Inject
    private Event<StartEditFieldPropertyEvent> fieldPropertyEvent;

    @Override
    public String getCode() {
        return "editor-" + super.getCode();
    }

    @Override
    protected Widget getFieldBox(final FormDescription form, final FieldDescription field) {
        final HorizontalPanel propertyButtons = new HorizontalPanel();

        Button edit = new Button("Edit");
        edit.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                fieldPropertyEvent.fire(new StartEditFieldPropertyEvent(form.getCtxUID(), field.getUid()));
            }
        });

        propertyButtons.add(edit);

        final FlowPanel panel = new FlowPanel();
        panel.add(super.getFieldBox(form, field));

        panel.addDomHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                panel.add(propertyButtons);
                propertyButtons.getElement().getStyle().setPosition(Style.Position.FIXED);
                propertyButtons.getElement().getStyle().setTop(panel.getAbsoluteTop(), Style.Unit.PX);
                propertyButtons.getElement().getStyle().setLeft(panel.getAbsoluteLeft() + panel.getOffsetWidth() - propertyButtons.getOffsetWidth(), Style.Unit.PX);
            }
        }, MouseOverEvent.getType());
        panel.addDomHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                panel.remove(propertyButtons);
            }
        }, MouseOutEvent.getType());
        return panel;
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
        horizontalPanel.getElement().getStyle().setPaddingRight(10, Style.Unit.PX);
        return horizontalPanel;
    }
}
