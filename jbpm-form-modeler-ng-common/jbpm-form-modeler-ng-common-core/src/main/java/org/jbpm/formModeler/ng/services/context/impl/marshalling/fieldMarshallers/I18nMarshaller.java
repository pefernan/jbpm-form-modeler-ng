package org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.jbpm.formModeler.ng.model.FieldValueMarshaller;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class I18nMarshaller implements FieldValueMarshaller {
    private Logger log = LoggerFactory.getLogger(I18nMarshaller.class);

    private static transient ObjectMapper mapper = new ObjectMapper();

    @Override
    public String marshallValue(Object value, FormRenderContext context) {
        if (value == null) return "";
        try {
            return mapper.writer().writeValueAsString(value);
        } catch (IOException e) {
            log.warn("Error marshalling values: {}", e);
        }
        return "";
    }

    @Override
    public Object unMarshallValue(String marshalledValue, Object previousValue, FormRenderContext context) {
        Map mapValue = (Map) previousValue;
        if (mapValue == null) mapValue = new HashMap();

        try {
            return mapper.readValue(marshalledValue, new TypeReference<HashMap<String,String>>(){});
        } catch (IOException e) {
            log.warn("Error unmarshalling values '{}': {}", marshalledValue, e);
        }
        return mapValue;
    }
}
