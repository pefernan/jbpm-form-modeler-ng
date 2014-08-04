package org.jbpm.formModeler.ng.model.impl;

import java.util.HashMap;

public class I18nText extends InputText {
    @Override
    public String getCode() {
        return "I18nText";
    }

    @Override
    public String getFieldClass() {
        return HashMap.class.getName();
    }
}
