package org.jbpm.formModeler.ng.model.impl.fields;

import org.jbpm.formModeler.ng.model.BasicTypeField;
import org.jbpm.formModeler.ng.model.FieldValueMarshaller;
import org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers.DateMarshaller;

import javax.inject.Inject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InputDate extends BasicTypeField {
    private Integer size = 15;

    @Inject
    private DateMarshaller marshaller;

    @Override
    public String getCode() {
        return "InputDate";
    }

    @Override
    public String getIcon() {
        return "date_selector.png";
    }

    @Override
    public String getFieldClass() {
        return Date.class.getName();
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

    @Override
    public Map<String, String> getCustomProperties() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("size", String.valueOf(size));
        return result;
    }

    @Override
    public void setCustomProperties(Map<String, String> properties) {
        try {
            size = Integer.decode(properties.get("size"));
        } catch (Exception ex) {
            size = 15;
        }
    }
}
