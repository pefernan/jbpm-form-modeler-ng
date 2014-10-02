package org.jbpm.formModeler.ng.model.impl.fields;

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
