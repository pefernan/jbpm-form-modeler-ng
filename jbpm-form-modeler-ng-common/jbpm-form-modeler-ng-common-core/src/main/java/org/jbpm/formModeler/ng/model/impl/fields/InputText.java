package org.jbpm.formModeler.ng.model.impl.fields;

import org.jbpm.formModeler.ng.model.BasicTypeField;
import org.jbpm.formModeler.ng.model.FieldValueMarshaller;
import org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers.StringMarshaller;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class InputText extends BasicTypeField {
    private Integer size = 15;
    private Integer maxLength = 100;

    @Inject
    private StringMarshaller marshaller;

    @Override
    public String getCode() {
        return "InputText";
    }

    @Override
    public String getIcon() {
        return "textbox.png";
    }

    @Override
    public String getFieldClass() {
        return String.class.getName();
    }

    @Override
    public FieldValueMarshaller getMarshaller() {
        return marshaller;
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

    @Override
    public Map<String, String> getCustomProperties() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("size", String.valueOf(size));
        result.put("maxLength", String.valueOf(maxLength));
        return result;
    }

    @Override
    public void setCustomProperties(Map<String, String> properties) {
        try {
            size = Integer.decode(properties.get("size"));
        } catch (Exception ex) {
            size = 15;
        }
        try {
            maxLength = Integer.decode(properties.get("size"));
        } catch (Exception ex) {
            maxLength = 100;
        }
    }
}
