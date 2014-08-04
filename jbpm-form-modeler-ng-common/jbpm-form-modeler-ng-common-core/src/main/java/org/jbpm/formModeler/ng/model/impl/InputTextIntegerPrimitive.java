package org.jbpm.formModeler.ng.model.impl;

public class InputTextIntegerPrimitive extends InputTextShort {

    @Override
    public String getCode() {
        return "InputTextPrimitiveInteger";
    }

    @Override
    public String getFieldClass() {
        return int.class.getName();
    }
}
