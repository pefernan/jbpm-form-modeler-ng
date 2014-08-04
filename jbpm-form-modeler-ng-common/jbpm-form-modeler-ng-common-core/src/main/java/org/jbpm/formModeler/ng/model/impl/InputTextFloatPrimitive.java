package org.jbpm.formModeler.ng.model.impl;

public class InputTextFloatPrimitive extends InputTextFloat {

    @Override
    public String getCode() {
        return "InputTextPrimitiveFloat";
    }

    @Override
    public String getFieldClass() {
        return float.class.getName();
    }
}
