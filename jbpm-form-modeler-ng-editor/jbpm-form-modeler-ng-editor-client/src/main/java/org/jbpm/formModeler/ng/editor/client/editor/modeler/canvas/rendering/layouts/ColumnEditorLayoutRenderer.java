package org.jbpm.formModeler.ng.editor.client.editor.modeler.canvas.rendering.layouts;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import org.jbpm.formModeler.ng.common.client.rendering.js.*;
import org.jbpm.formModeler.ng.common.client.rendering.layouts.ColumnLayoutRenderer;
import org.jbpm.formModeler.ng.editor.client.editor.modeler.canvas.rendering.layouts.utils.EditorActionsGenerator;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class ColumnEditorLayoutRenderer extends ColumnLayoutRenderer {
    @Inject
    private EditorActionsGenerator editorActionsGenerator;

    @Override
    public String getCode() {
        return "editor-columns";
    }

    @Override
    public Panel generateFormContent(FormContext context) {
        editorActionsGenerator.init(context);
        return super.generateFormContent(context);
    }

    @Override
    protected Grid createGrid(FormLayoutDefinition layout) {
        Grid content = super.createGrid(layout);
        content.setHeight("600px");
        return content;
    }

    @Override
    protected void formatCell(Grid grid, int index, int maxColumns) {
        super.formatCell(grid, index, maxColumns);
        grid.getColumnFormatter().getElement(index).getStyle().setBorderStyle(Style.BorderStyle.DASHED);
    }

    @Override
    protected Panel getFieldBox(final FieldDefinition field, final FormContext context) {
        Panel content = super.getFieldBox(field, context);
        editorActionsGenerator.addActionsHeader(field, content);
        return content;
    }

    public void renderField(Panel content, int areaIndex, int fieldIndex) {
        content.add(editorActionsGenerator.getHorizontalFieldDropArea(areaIndex, fieldIndex));
    }
}
