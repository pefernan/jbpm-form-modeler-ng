package org.jbpm.formModeler.ng.model.impl;

public class InputTextShortPrimitive extends InputTextShort {

    @Override
    public String getCode() {
        return "InputTextPrimitiveShort";
    }

    @Override
    public String getFieldClass() {
        return short.class.getName();
    }
}
