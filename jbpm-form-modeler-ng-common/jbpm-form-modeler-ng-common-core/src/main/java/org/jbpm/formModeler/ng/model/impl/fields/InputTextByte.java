package org.jbpm.formModeler.ng.model.impl.fields;

public class InputTextByte extends InputTextBigInteger {
    @Override
    public String getCode() {
        return "InputTextByte";
    }

    @Override
    public String getFieldClass() {
        return Byte.class.getName();
    }
}
