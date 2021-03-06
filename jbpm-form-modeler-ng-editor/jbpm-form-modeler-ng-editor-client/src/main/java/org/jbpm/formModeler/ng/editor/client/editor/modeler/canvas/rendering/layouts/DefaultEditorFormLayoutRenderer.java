package org.jbpm.formModeler.ng.editor.client.editor.modeler.canvas.rendering.layouts;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import org.jbpm.formModeler.ng.common.client.rendering.js.*;
import org.jbpm.formModeler.ng.common.client.rendering.layouts.DefaultFormLayoutRenderer;
import org.jbpm.formModeler.ng.editor.client.editor.modeler.canvas.rendering.layouts.utils.EditorActionsGenerator;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class DefaultEditorFormLayoutRenderer extends DefaultFormLayoutRenderer {
    @Inject
    private EditorActionsGenerator editorActionsGenerator;

    @Override
    public String getCode() {
        return "editor-default";
    }

    @Override
    public Panel generateFormContent(FormContext context) {
        editorActionsGenerator.init(context);
        return super.generateFormContent(context);
    }

    @Override
    protected Panel getFieldBox(final FieldDefinition field, final FormContext context) {
        Panel content = super.getFieldBox(field, context);
        editorActionsGenerator.addActionsHeader(field, content);
        return content;
    }

    @Override
    public void renderArea(Panel content, int index) {
        content.add(editorActionsGenerator.getHorizontalAreaDropArea(index));
    }

    @Override
    public void renderField(Panel content, int areaIndex, int fieldIndex) {
        content.add(editorActionsGenerator.getVerticalFieldDropArea(areaIndex, fieldIndex));
    }
}
