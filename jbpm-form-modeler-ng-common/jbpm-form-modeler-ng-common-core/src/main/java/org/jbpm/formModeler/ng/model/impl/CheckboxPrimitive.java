package org.jbpm.formModeler.ng.model.impl;


public class CheckboxPrimitive extends CheckBox {

    @Override
    public String getCode() {
        return "CheckBoxPrimitiveBoolean";
    }

    @Override
    public String getFieldClass() {
        return boolean.class.getName();
    }
}
