package org.jbpm.formModeler.ng.model.impl.fields;

public class InputTextInteger extends InputTextBigInteger {
    @Override
    public String getCode() {
        return "InputTextInteger";
    }

    @Override
    public String getFieldClass() {
        return Integer.class.getName();
    }
}
