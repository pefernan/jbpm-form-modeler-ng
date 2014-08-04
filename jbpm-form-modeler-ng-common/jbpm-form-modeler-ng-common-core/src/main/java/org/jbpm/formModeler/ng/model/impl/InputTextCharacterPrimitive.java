package org.jbpm.formModeler.ng.model.impl;

public class InputTextCharacterPrimitive extends InputTextCharacter {

    @Override
    public String getCode() {
        return "InputTextPrimitiveCharacter";
    }

    @Override
    public String getFieldClass() {
        return char.class.getName();
    }
}
