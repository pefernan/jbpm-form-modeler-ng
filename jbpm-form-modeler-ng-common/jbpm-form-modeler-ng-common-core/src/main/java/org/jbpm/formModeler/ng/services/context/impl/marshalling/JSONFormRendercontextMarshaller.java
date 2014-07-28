package org.jbpm.formModeler.ng.services.context.impl.marshalling;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.jbpm.formModeler.ng.model.DataHolder;
import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.model.Form;
import org.jbpm.formModeler.ng.services.LocaleManager;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;
import org.jbpm.formModeler.ng.services.context.FormRenderContextMarshaller;
import org.jbpm.formModeler.ng.services.management.forms.utils.BindingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Default
@ApplicationScoped
public class JSONFormRendercontextMarshaller implements FormRenderContextMarshaller {
    public static final String NAMESPACE_SEPARATOR = "-";

    public static final String FIELDS = "fields";

    private Logger log = LoggerFactory.getLogger(JSONFormRendercontextMarshaller.class);

    @Inject
    private LocaleManager localeManager;

    @Override
    public String marshallContext(FormRenderContext context) {

        String result = "";
        JsonGenerator generator = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JsonFactory f = new JsonFactory();
            generator = f.createJsonGenerator(baos, JsonEncoding.UTF8);

            Form form = context.getForm();

            Map<String, Object> inputData = context.getInputData();
            Map<String, Object> outputData = context.getOutputData();

            Map<String, Object> loadedObjects = new HashMap<String, Object>();

            generator.writeStartObject();
            generator.writeNumberField("id", form.getId());
            generator.writeStringField("name", form.getName());
            generator.writeStringField("displayMode", form.getDisplayMode());
            generator.writeStringField("labelMode", form.getLabelMode());

            marshallFields(form, inputData, outputData, loadedObjects, "", context, generator);

            generator.writeEndObject();

            generator.close();

            result = baos.toString("UTF-8");
        } catch (Exception ex) {
            log.warn("Error marshalling form: {}", ex);
        }

        return result;
    }

    private void marshallFields(Form form, Map inputData, Map outputData, Map loadedObjects, String namespace, FormRenderContext context, JsonGenerator generator) throws IOException {
        generator.writeArrayFieldStart(FIELDS);
        for (Field field : form.getFormFields()) {

            String inputExperession = field.getInputBinding();
            String outputExpression = field.getOutputBinding();

            boolean hasInput = !StringUtils.isEmpty(inputExperession);
            boolean hasOutput = !StringUtils.isEmpty(outputExpression);

            if (!hasInput && !hasOutput) continue;

            boolean readFromInput = (hasInput && !hasOutput) || (hasInput && outputData.isEmpty());

            DataHolder dataHolder = BindingUtils.getFormDataHolderForField(field);

            Object value;

            if (dataHolder == null) {
                if (readFromInput) value = getUnbindedValue(inputExperession, inputData);
                else value = getUnbindedValue(outputExpression, outputData);
            } else {
                Object loadedObject = loadedObjects.get(dataHolder.getUniqueId());
                if (loadedObject == null) {
                    if (outputData.isEmpty()) loadedObject = inputData.get(dataHolder.getInputId());
                    else loadedObject = outputData.get(dataHolder.getOutputId());
                    loadedObjects.put(dataHolder.getUniqueId(), loadedObject);
                }
                if (readFromInput) value = getBindedValue(field, dataHolder, inputExperession, inputData, loadedObjects, "");
                else value = getBindedValue(field, dataHolder, outputExpression, outputData, loadedObjects, namespace);
            }


            marshallField(field, value, context, "", generator);
        }
        generator.writeEndArray();
    }

    private void marshallField(Field field, Object value, FormRenderContext context, String namespace, JsonGenerator generator) throws IOException {
        generator.writeStartObject();

        generator.writeStringField("id", StringUtils.isEmpty(namespace) ? field.getName() : namespace + NAMESPACE_SEPARATOR + field.getName());
        generator.writeStringField("label", (String) localeManager.localize(field.getLabel(), context.getCurrentLocale()));
        generator.writeStringField("type", field.getCode());
        generator.writeNumberField("position", field.getPosition());
        generator.writeBooleanField("grouped", field.getGroupWithPrevious());

        generator.writeObjectField("value", field.getMarshaller().marshallValue(value));

        generator.writeEndObject();
    }

    protected Object getBindedValue(Field field, DataHolder holder, String bindingExpression, Map<String, Object> bindingData, Map loadedObjects, String namespace) {
        Object value = null;

        try {
            Object bindingValue = loadedObjects.get(holder.getUniqueId());
            if (bindingValue != null && holder.isAssignableValue(bindingValue)) {
                if (!loadedObjects.containsKey(holder.getUniqueId())) loadedObjects.put(holder.getUniqueId(), bindingValue);
                value =  holder.readValue(bindingValue, BindingUtils.extractInputExpression(bindingExpression));
            }
        } catch (Exception e) {
            log.warn("Unable to read value from expression '" + bindingExpression + "'. Error: ", e);
            value = bindingData.get(bindingExpression);
        }
        return value;
    }

    protected Object getUnbindedValue(String bindingExpression, Map<String, Object> bindingData) {
        if (bindingExpression.indexOf("/") != -1) {
            try {
                String root = bindingExpression.substring(0, bindingExpression.indexOf("/"));
                String expression = bindingExpression.substring(root.length() + 1);

                Object object = bindingData.get(root);
                JXPathContext ctx = JXPathContext.newContext(object);
                return ctx.getValue(expression);
            } catch (Exception e) {
                log.warn("Error getting value for xpath xpression '{}': {}", bindingExpression, e);
            }
        }
        return bindingData.get(bindingExpression);
    }

    @Override
    public void unmarshallContext(FormRenderContext context, String marshalledValues) {

        try {
            Form form = context.getForm();

            Map<String, Object> result = context.getOutputData();

            ObjectMapper mapper = new ObjectMapper();

            JsonNode node = mapper.readTree(marshalledValues);

            JsonNode fields = node.get("fields");

            for (int i = 0; i< fields.size(); i++) {
                JsonNode jsonField = fields.get(i);
                String fieldName = jsonField.get("id").getTextValue();

                Field field = form.getField(fieldName);

                if (field != null) {
                    String bindingString = field.getOutputBinding();

                    if (StringUtils.isEmpty(bindingString)) continue;

                    boolean simpleBinding = StringUtils.isEmpty(bindingString) || bindingString.indexOf("/") == -1;

                    DataHolder holder = BindingUtils.getFormDataHolderForField(field);

                    Object value = field.getMarshaller().unMarshallValue(jsonField.get("value").getTextValue());

                    if (holder == null || simpleBinding) result.put(bindingString, value);
                    else {
                        String holderFieldId = bindingString.substring((holder.getOutputId() + "/").length());

                        Object holderOutputValue = result.get(holder.getOutputId());
                        if (holderOutputValue == null || !holder.isAssignableValue(holderOutputValue)) {
                            holderOutputValue = context.getInputData().get(holder.getInputId());
                            if (holderOutputValue == null || !holder.isAssignableValue(holderOutputValue)) holderOutputValue = holder.createInstance(context);
                            result.put(holder.getOutputId(), holderOutputValue);
                        }

                        holder.writeValue(holderOutputValue, holderFieldId, value);
                    }
                }

            }

        } catch (Exception e) {
            log.error("Error unmarshalling values: {}", e);
        }
    }
}
