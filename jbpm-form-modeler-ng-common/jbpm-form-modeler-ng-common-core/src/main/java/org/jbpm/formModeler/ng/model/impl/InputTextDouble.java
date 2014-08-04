package org.jbpm.formModeler.ng.model.impl;

public class InputTextDouble extends InputTextBigDecimal {


    @Override
    public String getCode() {
        return "InputTextDouble";
    }

    @Override
    public String getFieldClass() {
        return Double.class.getName();
    }
}
