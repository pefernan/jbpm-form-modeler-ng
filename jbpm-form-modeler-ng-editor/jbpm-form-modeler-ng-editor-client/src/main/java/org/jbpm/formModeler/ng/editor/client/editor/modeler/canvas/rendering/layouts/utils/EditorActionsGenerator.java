package org.jbpm.formModeler.ng.editor.client.editor.modeler.canvas.rendering.layouts.utils;

import com.github.gwtbootstrap.client.ui.base.IconAnchor;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.editor.events.FormModelerEvent;
import org.jbpm.formModeler.ng.editor.events.canvas.DeleteFieldEvent;
import org.jbpm.formModeler.ng.editor.events.canvas.RefreshCanvasEvent;
import org.jbpm.formModeler.ng.editor.events.canvas.StartEditFieldPropertyEvent;
import org.jbpm.formModeler.ng.editor.events.dataHolders.RefreshHoldersListEvent;
import org.jbpm.formModeler.ng.editor.service.FormEditorService;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Dependent
public class EditorActionsGenerator {

    private List<Panel> dropAreas = new ArrayList<Panel>();

    private String ctxUID;
    private FieldDefinition selectedField;

    @Inject
    private Event<StartEditFieldPropertyEvent> fieldPropertyEvent;

    @Inject
    private Event<DeleteFieldEvent> deleteFieldEvent;

    @Inject
    private Caller<FormEditorService> editorService;

    @Inject
    private Event<FormModelerEvent> modelerEvent;

    public void init(FormContext context) {
        this.ctxUID = context.getCtxUID();
        dropAreas.clear();
        selectedField = null;
    }

    private void showDropAreas(FieldDefinition field) {
        this.selectedField = field;
        for (Panel dropArea : dropAreas) {
            dropArea.setVisible(true);
        }
    }

    public Panel getHorizontalAreaDropArea(final int row) {
        FlowPanel dropArea = new FlowPanel();
        dropArea.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                doMoveField(row, -1);
            }
        }, ClickEvent.getType());
        dropArea.setWidth("100%");
        dropArea.setHeight("10px");
        dropArea.getElement().getStyle().setBackgroundColor("green");
        dropArea.setVisible(false);
        dropAreas.add(dropArea);
        return dropArea;
    }

    public Panel getVerticalFieldDropArea(final int row, final int column) {
        FlowPanel dropArea = new FlowPanel();
        dropArea.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                doMoveField(row, column);
            }
        }, ClickEvent.getType());
        dropArea.setWidth("10px");
        dropArea.setHeight("30px");
        dropArea.getElement().getStyle().setBackgroundColor("green");
        dropArea.setVisible(false);
        dropAreas.add(dropArea);
        return dropArea;
    }

    public Panel getHorizontalFieldDropArea(final int row, final int column) {
        FlowPanel dropArea = new FlowPanel();
        dropArea.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                doMoveField(row, column);
            }
        }, ClickEvent.getType());
        dropArea.setWidth("100%");
        dropArea.setHeight("10px");
        dropArea.getElement().getStyle().setBackgroundColor("green");
        dropArea.setVisible(false);
        dropAreas.add(dropArea);
        return dropArea;
    }

    protected void doMoveField(int row, int column) {
        if (selectedField != null) {
            editorService.call(new RemoteCallback<String>() {
                                   @Override
                                   public void callback(String jsonResponse) {
                                       if (jsonResponse != null) {
                                           modelerEvent.fire(new RefreshCanvasEvent(ctxUID, jsonResponse));
                                           modelerEvent.fire(new RefreshHoldersListEvent(ctxUID));
                                       }
                                   }
                               }, new ErrorCallback<Object>() {
                                   @Override
                                   public boolean error(Object message, Throwable throwable) {
                                       Window.alert("Something wong happened: " + message);
                                       return false;
                                   }
                               }).moveSelectedFieldToFieldPosition(ctxUID, Long.decode(selectedField.getUid()), row, column);
        }
    }

    public void addActionsHeader(final FieldDefinition field, final Panel fieldBox) {
        final HorizontalPanel propertyButtons = new HorizontalPanel();
        propertyButtons.getElement().getStyle().setBackgroundColor("#F5F5F5");

        HTML holder = new HTML();
        holder.setHeight("20px");
        holder.setWidth("20px");
        holder.getElement().getStyle().setBackgroundColor(field.getHolderColor());

        propertyButtons.add(holder);

        IconAnchor move = new IconAnchor();
        move.setIcon(IconType.MOVE);
        move.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showDropAreas(field);
            }
        });
        move.getElement().getStyle().setPaddingLeft(3, Style.Unit.PX);
        move.getElement().getStyle().setPaddingRight(3, Style.Unit.PX);

        IconAnchor edit = new IconAnchor();
        edit.setIcon(IconType.PENCIL);
        edit.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                fieldPropertyEvent.fire(new StartEditFieldPropertyEvent(ctxUID, field.getUid()));
            }
        });
        edit.getElement().getStyle().setPaddingLeft(3, Style.Unit.PX);
        edit.getElement().getStyle().setPaddingRight(3, Style.Unit.PX);

        IconAnchor trash = new IconAnchor();
        trash.setIcon(IconType.TRASH);
        trash.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (Window.confirm("!!Are you sure?")) {
                    deleteFieldEvent.fire(new DeleteFieldEvent(ctxUID, field.getUid()));
                }
            }
        });
        trash.getElement().getStyle().setPaddingLeft(3, Style.Unit.PX);
        trash.getElement().getStyle().setPaddingRight(3, Style.Unit.PX);

        propertyButtons.add(move);
        propertyButtons.add(edit);
        propertyButtons.add(trash);
        fieldBox.addDomHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                fieldBox.add(propertyButtons);
                propertyButtons.getElement().getStyle().setPosition(Style.Position.FIXED);
                propertyButtons.getElement().getStyle().setTop(fieldBox.getAbsoluteTop(), Style.Unit.PX);
                propertyButtons.getElement().getStyle().setLeft(fieldBox.getAbsoluteLeft() + fieldBox.getOffsetWidth() - propertyButtons.getOffsetWidth(), Style.Unit.PX);
            }
        }, MouseOverEvent.getType());
        fieldBox.addDomHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                fieldBox.remove(propertyButtons);
            }
        }, MouseOutEvent.getType());

    }


}
