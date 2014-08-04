package org.jbpm.formModeler.ng.model.impl;

public class InputTextLongPrimitive extends InputTextShort {

    @Override
    public String getCode() {
        return "InputTextPrimitiveLong";
    }

    @Override
    public String getFieldClass() {
        return long.class.getName();
    }
}
