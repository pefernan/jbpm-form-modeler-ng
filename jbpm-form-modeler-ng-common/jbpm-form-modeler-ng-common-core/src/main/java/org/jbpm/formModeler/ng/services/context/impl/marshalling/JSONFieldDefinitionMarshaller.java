package org.jbpm.formModeler.ng.services.context.impl.marshalling;


import org.codehaus.jackson.annotate.JsonIgnoreType;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;
import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.model.FieldValueMarshaller;
import org.jbpm.formModeler.ng.model.Form;
import org.jbpm.formModeler.ng.services.management.forms.FieldDefinitionMarshaller;
import org.jbpm.formModeler.ng.services.management.forms.FieldManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class JSONFieldDefinitionMarshaller implements FieldDefinitionMarshaller {
    private Logger log = LoggerFactory.getLogger(JSONFieldDefinitionMarshaller.class);

    public static final String CODE = "code";
    public static final String FIELDCLASS = "fieldClass";
    public static final String LABEL = "label";

    @Inject
    private FieldManager fieldManager;

    private ObjectMapper objectMapper;

    private FilterProvider filters;

    @PostConstruct
    protected void init() {
        objectMapper = new ObjectMapper();

        objectMapper.getSerializationConfig().addMixInAnnotations(Form.class, MyMixInForForms.class);
        objectMapper.getSerializationConfig().addMixInAnnotations(FieldValueMarshaller.class, MyMixInForFieldMarshaller.class);
        objectMapper.getSerializationConfig().addMixInAnnotations(Map.class, MyMixInForLabel.class);

        String[] ignorableFieldNames = { "form", "marshaller" };
        filters = new SimpleFilterProvider()
                .addFilter("filter properties by name",
                        SimpleBeanPropertyFilter.serializeAllExcept(
                                ignorableFieldNames));
    }

    @Override
    public String marshall(Field field) {
        String result = null;

        try {
            Map<String, String> valuesMap = objectMapper.convertValue(field, new TypeReference<HashMap<String, String>>() {
            });
            ObjectWriter writer = objectMapper.writer(filters);

            String labels = result = writer.writeValueAsString(field.getLabel());

            valuesMap.remove(FIELDCLASS);
            valuesMap.put(LABEL, labels);

            result = writer.writeValueAsString(valuesMap);
        } catch (Exception e) {
            log.warn("Error marshalling field {}: {}", field.getName(), e);
        }
        return result;
    }

    @Override
    public Field unmarshall(String marshalledField) {
        Field result = null;
        try {
            ObjectMapper mapper = new ObjectMapper();

            Map<String, String> valuesMap = mapper.readValue(marshalledField, new TypeReference<HashMap<String,String>>(){});

            Field field = fieldManager.getFieldByCode(valuesMap.get(CODE));

            valuesMap.remove(CODE);
            valuesMap.remove(FIELDCLASS);
            String labels = valuesMap.remove(LABEL);
            Map<String, String> label = mapper.readValue(labels, new TypeReference<HashMap<String,String>>(){});

            result = objectMapper.convertValue(valuesMap, field.getClass());
            result.setLabel(label);

            result.setMarshaller(field.getMarshaller());
        } catch (Exception e) {
            log.warn("Error unmarshalling field: {}", e);
        }

        return  result;
    }

    @JsonIgnoreType
    public class MyMixInForForms {
    }

    @JsonIgnoreType
    public class MyMixInForFieldMarshaller {
    }

    @JsonIgnoreType
    public class MyMixInForLabel {
    }
}
