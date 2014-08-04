package org.jbpm.formModeler.ng.model.impl;

import org.jbpm.formModeler.ng.model.BasicTypeField;
import org.jbpm.formModeler.ng.model.FieldValueMarshaller;
import org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers.StringMarshaller;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class InputTextArea extends BasicTypeField {
    private Integer height = 4;
    private Integer width = 20;

    @Inject
    private StringMarshaller marshaller;

    @Override
    public String getCode() {
        return "InputTextArea";
    }

    @Override
    public String getFieldClass() {
        return String.class.getName();    }

    @Override
    public FieldValueMarshaller getMarshaller() {
        return marshaller;
    }

    @Override
    public String getIcon() {
        return "scroll_zone.png";
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @Override
    public Map<String, String> getCustomProperties() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("height", String.valueOf(height));
        result.put("width", String.valueOf(width));
        return result;
    }

    @Override
    public void setCustomProperties(Map<String, String> properties) {
        try {
            height = Integer.decode(properties.get("height"));
        } catch (Exception ex) {
            height = 20;
        }
        try {
            width = Integer.decode(properties.get("width"));
        } catch (Exception ex) {
            width = 4;
        }
    }
}
