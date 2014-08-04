package org.jbpm.formModeler.ng.model.impl;

public class InputTextDoublePrimitive extends InputTextDouble {

    @Override
    public String getCode() {
        return "InputTextPrimitiveDouble";
    }

    @Override
    public String getFieldClass() {
        return double.class.getName();
    }
}
