package org.jbpm.formModeler.ng.editor.client.editor.canvas.rendering.renderers;

import com.github.gwtbootstrap.client.ui.base.IconAnchor;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.renderers.DefaultFormRenderer;
import org.jbpm.formModeler.ng.editor.events.FormModelerEvent;
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
public class EditorDefaultFormRenderer extends DefaultFormRenderer {
    @Inject
    private Event<StartEditFieldPropertyEvent> fieldPropertyEvent;

    @Inject
    private Caller<FormEditorService> editorService;

    @Inject
    private Event<FormModelerEvent> modelerEvent;

    private List<Panel> dropAreas = new ArrayList<Panel>();

    private String ctxUID;
    private FieldDefinition selectedField;

    @Override
    public String getCode() {
        return "editor-" + super.getCode();
    }

    @Override
    public Panel generateForm(FormContext context) {
        VerticalPanel formContent = new VerticalPanel();
        FormDefinition formDefinition = context.getFormDefinition();
        if (formDefinition != null) {
            ctxUID = context.getCtxUID();
            boolean first = true;
            HorizontalPanel horizontalPanel = new HorizontalPanel();
            JsArray<FieldDefinition> fields = formDefinition.getFieldDefinitions();

            if (fields != null && fields.length() > 0) {
                FieldDefinition fieldDefinition = null;
                for (int i = 0; i < formDefinition.getFieldDefinitions().length(); i++) {
                    fieldDefinition = formDefinition.getFieldDefinitions().get(i);
                    Widget fieldBox = getFieldBox(fieldDefinition, context);
                    if (fieldBox != null) {
                        if (first) {
                            formContent.add(getHorizontalDropArea(0));
                            first = false;
                            formContent.add(horizontalPanel);
                        } else if (fieldDefinition.getColumn() == 0) {
                            horizontalPanel.add(getVerticalDropArea(fieldDefinition.getRow() - 1, fieldDefinition.getColumn()));
                            formContent.add(getHorizontalDropArea(fieldDefinition.getRow()));
                            horizontalPanel = new HorizontalPanel();
                            formContent.add(horizontalPanel);
                        }
                        horizontalPanel.add(getVerticalDropArea(fieldDefinition.getRow(), fieldDefinition.getColumn()));
                        horizontalPanel.add(fieldBox);
                    }
                }
                if (fieldDefinition != null) horizontalPanel.add(getVerticalDropArea(fieldDefinition.getRow(), fieldDefinition.getColumn() + 1));
                formContent.add(getHorizontalDropArea(-1));
            }
        }
        return formContent;
    }

    protected Panel getHorizontalDropArea(final int row) {
        SimpleLayoutPanel dropArea = new SimpleLayoutPanel();
        dropArea.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                doMoveField(row, 0, true);
            }
        }, ClickEvent.getType());
        dropArea.setWidth("100%");
        dropArea.setHeight("10px");
        dropArea.getElement().getStyle().setBackgroundColor("green");
        dropArea.setVisible(false);
        dropAreas.add(dropArea);
        return dropArea;
    }

    protected Panel getVerticalDropArea(final int row, final int column) {
        SimpleLayoutPanel dropArea = new SimpleLayoutPanel();
        dropArea.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                doMoveField(row, column, false);
            }
        }, ClickEvent.getType());
        dropArea.setWidth("10px");
        dropArea.setHeight("30px");
        dropArea.getElement().getStyle().setBackgroundColor("green");
        dropArea.setVisible(false);
        dropAreas.add(dropArea);
        return dropArea;
    }


    protected void doMoveField(int row, int column, boolean newLine) {
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
            }).moveSelectedFieldToFieldPosition(ctxUID, Long.decode(selectedField.getUid()), row, column, newLine);
        }
    }

    @Override
    protected Widget getFieldBox(final FieldDefinition field, final FormContext context) {
        final HorizontalPanel propertyButtons = new HorizontalPanel();
        propertyButtons.getElement().getStyle().setBackgroundColor("#F5F5F5");
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
                fieldPropertyEvent.fire(new StartEditFieldPropertyEvent(context.getCtxUID(), field.getUid()));
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
                    editorService.call(new RemoteCallback<String>() {
                                           @Override
                                           public void callback(String jsonResponse) {
                                               if (jsonResponse != null) {
                                                   modelerEvent.fire(new RefreshCanvasEvent(context.getCtxUID(), jsonResponse));
                                                   modelerEvent.fire(new RefreshHoldersListEvent(context.getCtxUID()));
                                               }
                                           }
                                       }, new ErrorCallback<Object>() {
                                           @Override
                                           public boolean error(Object message, Throwable throwable) {
                                               Window.alert("Something wong happened: " + message);
                                               return false;
                                           }
                                       }
                    ).removeFieldFromForm(context.getCtxUID(), Long.decode(field.getUid()));
                }
            }
        });
        trash.getElement().getStyle().setPaddingLeft(3, Style.Unit.PX);
        trash.getElement().getStyle().setPaddingRight(3, Style.Unit.PX);

        propertyButtons.add(move);
        propertyButtons.add(edit);
        propertyButtons.add(trash);

        final FlowPanel panel = new FlowPanel();
        panel.add(super.getFieldBox(field, context));

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

    private void showDropAreas(FieldDefinition field) {
        selectedField = field;
        for (Panel dropArea : dropAreas) {
            dropArea.setVisible(true);
        }
    }

    @Override
    protected Widget getFieldLabel(FieldDefinition field) {
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
