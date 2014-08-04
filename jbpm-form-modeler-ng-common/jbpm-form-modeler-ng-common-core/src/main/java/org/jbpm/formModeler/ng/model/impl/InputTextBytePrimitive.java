package org.jbpm.formModeler.ng.model.impl;

public class InputTextBytePrimitive extends InputTextByte {
    @Override
    public String getCode() {
        return "InputTextPrimitiveByte";
    }

    @Override
    public String getFieldClass() {
        return byte.class.getName();
    }
}
