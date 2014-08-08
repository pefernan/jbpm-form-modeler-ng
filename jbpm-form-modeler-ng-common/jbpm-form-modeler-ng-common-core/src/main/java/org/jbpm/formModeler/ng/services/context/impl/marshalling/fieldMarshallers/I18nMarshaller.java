package org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers;

import org.jbpm.formModeler.ng.model.FieldValueMarshaller;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;

import java.util.HashMap;
import java.util.Map;

public class I18nMarshaller implements FieldValueMarshaller {
    @Override
    public String marshallValue(Object value, FormRenderContext context) {
        if (value == null) return "";
        Object result = ((Map) value).get(context.getCurrentLocale().getLanguage());
        if (result == null) return "";
        return result.toString();
    }

    @Override
    public Object unMarshallValue(String marshalledValue, Object previousValue, FormRenderContext context) {
        Map mapValue = (Map) previousValue;
        if (mapValue == null) mapValue = new HashMap();
        mapValue.put(context.getCurrentLocale().getLanguage(), marshalledValue);
        return mapValue;
    }
}
