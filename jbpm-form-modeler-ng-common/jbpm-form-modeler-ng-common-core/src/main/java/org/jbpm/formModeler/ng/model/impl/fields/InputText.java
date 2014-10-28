package org.jbpm.formModeler.ng.model.impl.fields;

import org.jbpm.formModeler.ng.model.BasicField;
import org.jbpm.formModeler.ng.model.FieldValueMarshaller;
import org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers.StringMarshaller;

import javax.inject.Inject;

public class InputText extends BasicField {
    private Integer size = 15;
    private Integer maxLength = 100;

    @Inject
    private StringMarshaller marshaller;

    @Override
    public String getCode() {
        return "InputText";
    }

    @Override
    public String getFieldClass() {
        return String.class.getName();
    }

    @Override
    public FieldValueMarshaller getMarshaller() {
        return marshaller;
    }

    @Override
    public void setMarshaller(FieldValueMarshaller marshaller) {
        if (marshaller instanceof StringMarshaller) this.marshaller = (StringMarshaller) marshaller;
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
