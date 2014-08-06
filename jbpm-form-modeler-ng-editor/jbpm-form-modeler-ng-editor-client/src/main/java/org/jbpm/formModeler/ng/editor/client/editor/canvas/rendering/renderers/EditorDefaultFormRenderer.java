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
import org.jbpm.formModeler.ng.common.client.rendering.FieldDescription;
import org.jbpm.formModeler.ng.common.client.rendering.FormDescription;
import org.jbpm.formModeler.ng.common.client.rendering.renderers.DefaultFormRenderer;
import org.jbpm.formModeler.ng.editor.events.FormModelerEvent;
import org.jbpm.formModeler.ng.editor.events.canvas.RefreshCanvasEvent;
import org.jbpm.formModeler.ng.editor.events.canvas.StartEditFieldPropertyEvent;
import org.jbpm.formModeler.ng.editor.events.dataHolders.RefreshHoldersListEvent;
import org.jbpm.formModeler.ng.editor.service.FormEditorService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class EditorDefaultFormRenderer extends DefaultFormRenderer {
    @Inject
    private Event<StartEditFieldPropertyEvent> fieldPropertyEvent;

    @Inject
    private Caller<FormEditorService> editorService;

    @Inject
    private Event<FormModelerEvent> modelerEvent;

    private List<Panel> dropAreas = new ArrayList<Panel>();

    private String ctxUID;
    private FieldDescription selectedField;

    @Override
    public String getCode() {
        return "editor-" + super.getCode();
    }

    @Override
    public Panel generateForm(FormDescription formDescription) {
        VerticalPanel formContent = new VerticalPanel();
        if (formDescription != null) {
            ctxUID = formDescription.getCtxUID();
            boolean first = true;
            HorizontalPanel horizontalPanel = new HorizontalPanel();
            JsArray<FieldDescription> fields = formDescription.getFields();

            if (fields != null && fields.length() > 0) {
                for (int i = 0; i < formDescription.getFields().length(); i++) {
                    FieldDescription fieldDescription = formDescription.getFields().get(i);
                    Widget fieldBox = getFieldBox(formDescription, fieldDescription);
                    if (fieldBox != null) {
                        int position = fieldDescription.getPosition();
                        if (first) {
                            formContent.add(getHorizontalDropArea(0));
                            first = false;
                            formContent.add(horizontalPanel);
                        } else if (!fieldDescription.isGrouped()) {
                            horizontalPanel.add(getVerticalDropArea("grouped", position));
                            formContent.add(getHorizontalDropArea(position));
                            horizontalPanel = new HorizontalPanel();
                            formContent.add(horizontalPanel);
                        }

                        horizontalPanel.add(getVerticalDropArea("pre", position));
                        horizontalPanel.add(fieldBox);
                    }
                }
                horizontalPanel.add(getVerticalDropArea("grouped", fields.length()));
                formContent.add(getHorizontalDropArea(fields.length()));
            }
        }
        return formContent;
    }

    protected Panel getHorizontalDropArea(final int position) {
        SimpleLayoutPanel dropArea = new SimpleLayoutPanel();
        dropArea.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
               Window.alert("adding field to row: newLine-" + position);
                doMoveField(position, "newLine");
            }
        }, ClickEvent.getType());
        dropArea.setWidth("100%");
        dropArea.setHeight("10px");
        dropArea.getElement().getStyle().setBackgroundColor("green");
        dropArea.setVisible(false);
        dropAreas.add(dropArea);
        return dropArea;
    }

    protected Panel getVerticalDropArea(final String modifier, final int position) {
        SimpleLayoutPanel dropArea = new SimpleLayoutPanel();
        dropArea.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Window.alert("adding field to : " + modifier + "-" + position);
                doMoveField(position, modifier);
            }
        }, ClickEvent.getType());
        dropArea.setWidth("10px");
        dropArea.setHeight("30px");
        dropArea.getElement().getStyle().setBackgroundColor("green");
        dropArea.setVisible(false);
        dropAreas.add(dropArea);
        return dropArea;
    }

    protected void doMoveField(int destinationPosition, String modifier) {
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
            }).moveSelectedFieldToFieldPosition(ctxUID, selectedField.getPosition(), destinationPosition, modifier);
        }
    }

    @Override
    protected Widget getFieldBox(final FormDescription form, final FieldDescription field) {
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
                fieldPropertyEvent.fire(new StartEditFieldPropertyEvent(form.getCtxUID(), field.getUid()));
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
                                                   modelerEvent.fire(new RefreshCanvasEvent(form.getCtxUID(), jsonResponse));
                                                   modelerEvent.fire(new RefreshHoldersListEvent(form.getCtxUID()));
                                               }
                                           }
                                       }, new ErrorCallback<Object>() {
                                           @Override
                                           public boolean error(Object message, Throwable throwable) {
                                               Window.alert("Something wong happened: " + message);
                                               return false;
                                           }
                                       }
                    ).removeFieldFromForm(form.getCtxUID(), field.getPosition());
                }
            }
        });
        trash.getElement().getStyle().setPaddingLeft(3, Style.Unit.PX);
        trash.getElement().getStyle().setPaddingRight(3, Style.Unit.PX);

        propertyButtons.add(move);
        propertyButtons.add(edit);
        propertyButtons.add(trash);

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

    private void showDropAreas(FieldDescription field) {
        selectedField = field;
        for (Panel dropArea : dropAreas) {
            dropArea.setVisible(true);
        }
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
