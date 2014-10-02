package org.jbpm.formModeler.ng.model.impl.fields;

public class InputTextShort extends InputTextBigInteger {
    @Override
    public String getCode() {
        return "InputTextShort";
    }

    @Override
    public String getFieldClass() {
        return Short.class.getName();
    }
}
