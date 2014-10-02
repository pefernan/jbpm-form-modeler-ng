package org.jbpm.formModeler.ng.model.impl.fields;

import org.jbpm.formModeler.ng.model.BasicTypeField;
import org.jbpm.formModeler.ng.model.FieldValueMarshaller;
import org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers.BooleanMarshaller;

import javax.inject.Inject;
import java.util.Map;

public class CheckBox extends BasicTypeField {
    @Inject
    private BooleanMarshaller marshaller;

    @Override
    public String getCode() {
        return "CheckBox";
    }

    @Override
    public String getIcon() {
        return "checkbox.png";
    }

    @Override
    public String getFieldClass() {
        return Boolean.class.getName();
    }

    @Override
    public FieldValueMarshaller getMarshaller() {
        return marshaller;
    }

    @Override
    public Map<String, String> getCustomProperties() {
        return null;
    }

    @Override
    public void setCustomProperties(Map<String, String> properties) {
    }
}
