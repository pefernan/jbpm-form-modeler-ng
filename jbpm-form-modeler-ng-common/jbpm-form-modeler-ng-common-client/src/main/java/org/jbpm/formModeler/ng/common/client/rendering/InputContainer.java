package org.jbpm.formModeler.ng.common.client.rendering;

import com.github.gwtbootstrap.client.ui.*;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormDefinition;

public abstract class InputContainer {

    protected FormLabel inputLabel;
    protected Widget fieldInput;
    protected HelpBlock helpBlock = new HelpBlock();
    protected String label;
    protected ControlGroup controlGroup = new ControlGroup();
    protected VerticalPanel groupContainer = new VerticalPanel();

    public InputContainer(Widget fieldInput, String label, boolean inputManagesLabel, FieldDefinition fieldDefinition, FormDefinition formDefinition) {
        this.fieldInput = fieldInput;
        this.label = label;

        controlGroup = new ControlGroup();

        if (!(formDefinition.getLabelMode().equals(FormLayoutRenderer.LABEL_MODE_DEFAULT) && inputManagesLabel)) {
            ControlLabel controlLabel = new ControlLabel();
            inputLabel = new FormLabel(label);
            inputLabel.setFor(fieldDefinition.getName());
            controlLabel.add(inputLabel);
            if (formDefinition.getLabelMode().equals(FormLayoutRenderer.LABEL_MODE_LEFT_ALIGNED)) controlLabel.getElement().getStyle().setTextAlign(Style.TextAlign.LEFT);
            controlGroup.add(controlLabel);
        }

        Controls controls = new Controls();
        controls.add(fieldInput);
        controls.add(helpBlock);
        controlGroup.add(controls);
        groupContainer.add(controlGroup);
    }

    public abstract void setReadOnly(boolean readOnly);

    public ControlGroup getControlGroup() {
        return controlGroup;
    }

    public void setVisible(boolean visible) {
        controlGroup.setVisible(visible);
    }

    public void setHelpMessage(String helpMessage) {
        this.helpBlock.setText(helpMessage);
    }

    public void setWrong(boolean wrong) {
        controlGroup.setType(wrong ? ControlGroupType.ERROR : ControlGroupType.NONE);
    }

    public Panel getControlGroupPanel() {
        return groupContainer;
    }
}
