package org.jbpm.formModeler.ng.model.impl.fields;

import org.jbpm.formModeler.ng.model.ComplexField;
import org.jbpm.formModeler.ng.model.FieldValueMarshaller;
import org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers.I18nMarshaller;

import javax.inject.Inject;
import java.util.HashMap;

public class I18nText extends ComplexField {
    private Integer size = 15;
    private Integer maxLength = 100;

    @Inject
    I18nMarshaller marshaller;

    @Override
    public String getCode() {
        return "I18nText";
    }

    @Override
    public String getFieldClass() {
        return HashMap.class.getName();
    }

    @Override
    public FieldValueMarshaller getMarshaller() {
        return marshaller;
    }

    @Override
    public void setMarshaller(FieldValueMarshaller marshaller) {
        if (marshaller instanceof  I18nMarshaller) this.marshaller = (I18nMarshaller) marshaller;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }
}
