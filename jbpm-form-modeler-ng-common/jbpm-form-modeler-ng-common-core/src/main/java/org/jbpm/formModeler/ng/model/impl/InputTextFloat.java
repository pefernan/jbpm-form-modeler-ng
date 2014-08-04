package org.jbpm.formModeler.ng.model.impl;

public class InputTextFloat extends InputTextBigDecimal {

    @Override
    public String getCode() {
        return "InputTextFloat";
    }

    @Override
    public String getFieldClass() {
        return Float.class.getName();
    }
}
