package org.jbpm.formModeler.ng.model.impl;

import org.jbpm.formModeler.ng.model.BasicTypeField;
import org.jbpm.formModeler.ng.model.FieldValueMarshaller;
import org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers.StringMarshaller;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class DropDown extends BasicTypeField {
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
    public String getIcon() {
        return "select.png";
    }

    @Override
    public String getFieldClass() {
        return String.class.getName();
    }

    @Override
    public FieldValueMarshaller getMarshaller() {
        return marshaller;
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

    @Override
    public Map<String, String> getCustomProperties() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("width", String.valueOf(width));
        result.put("provider", provider);
        return result;
    }

    @Override
    public void setCustomProperties(Map<String, String> properties) {
        try {
            width = Integer.decode(properties.get("width"));
        } catch (Exception ex) {
            width = 15;
        }
        provider = properties.get("provider");
    }
}
