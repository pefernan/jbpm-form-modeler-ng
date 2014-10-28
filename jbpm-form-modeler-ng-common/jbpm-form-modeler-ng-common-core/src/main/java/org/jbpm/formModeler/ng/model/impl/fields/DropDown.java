package org.jbpm.formModeler.ng.model.impl.fields;

import org.jbpm.formModeler.ng.model.BasicField;
import org.jbpm.formModeler.ng.model.FieldValueMarshaller;
import org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers.StringMarshaller;

import javax.inject.Inject;

public class DropDown extends BasicField {
    public static final String CODE = "DropDown";

    private Integer width = 15;
    private String provider;

    @Inject
    private StringMarshaller marshaller;

    @Override
    public String getCode() {
        return CODE;
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

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
