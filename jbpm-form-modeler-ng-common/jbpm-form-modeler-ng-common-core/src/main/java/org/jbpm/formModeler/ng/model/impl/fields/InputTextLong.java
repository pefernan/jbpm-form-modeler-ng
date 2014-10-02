package org.jbpm.formModeler.ng.model.impl.fields;

public class InputTextLong extends InputTextBigInteger {
    @Override
    public String getCode() {
        return "InputTextLong";
    }

    @Override
    public String getFieldClass() {
        return Long.class.getName();
    }
}
