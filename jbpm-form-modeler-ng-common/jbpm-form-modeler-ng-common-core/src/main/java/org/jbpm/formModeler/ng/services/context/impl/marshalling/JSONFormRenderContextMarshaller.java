package org.jbpm.formModeler.ng.services.context.impl.marshalling;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.jbpm.formModeler.ng.model.*;
import org.jbpm.formModeler.ng.model.impl.fields.DropDown;
import org.jbpm.formModeler.ng.services.LocaleManager;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;
import org.jbpm.formModeler.ng.services.context.FormRenderContextMarshaller;
import org.jbpm.formModeler.ng.services.management.forms.FormDefinitionMarshaller;
import org.jbpm.formModeler.ng.services.management.forms.SelectValuesOptionsMarshaller;
import org.jbpm.formModeler.ng.services.management.forms.utils.BindingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

@Default
@ApplicationScoped
public class JSONFormRenderContextMarshaller implements FormRenderContextMarshaller {
    public static final String NAMESPACE_SEPARATOR = "-";

    public static final String CTX_UID = "ctxUID";
    public static final String FORM = "form";

    public static final String STATUS = "status";
    public static final String VALUES = "values";
    public static final String WRONG_FIELDS = "wrongFields";
    public static final String OPTIONS = "options";

    private Logger log = LoggerFactory.getLogger(JSONFormRenderContextMarshaller.class);

    @Inject
    private FormDefinitionMarshaller formMarshaller;

    @Inject
    private SelectValuesOptionsMarshaller optionsMarshaller;

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

            generator.writeStartObject();
            generator.writeStringField(CTX_UID, context.getUID());

            if (form == null) {
                form =  formMarshaller.unmarshall(context.getFormTemplate(), context.getAttributes());
                context.setForm(form);
            } else if (StringUtils.isEmpty(context.getFormTemplate())) {
                context.setFormTemplate(formMarshaller.marshall(form));
            }

            generator.writeFieldName(FORM);
            generator.writeRawValue(context.getFormTemplate());

            generator.writeObjectFieldStart(STATUS);

            if (!StringUtils.isEmpty(context.getSerializedStatus())) {
                generator.writeFieldName(VALUES);
                generator.writeRawValue(context.getSerializedStatus());
            } else if (context.getInputData() != null) {
                marshallStatus(form, inputData, "", context, generator);
            } else if (!StringUtils.isEmpty(form.getSerializedStatus())) {
                generator.writeFieldName(VALUES);
                generator.writeRawValue(form.getSerializedStatus());
            } else {
                generator.writeObjectFieldStart(VALUES);
                generator.writeEndObject();
            }
            generator.writeArrayFieldStart(WRONG_FIELDS);
            generator.writeEndArray();

            marshallDropDownOptions(form, "", context, generator);

            generator.writeEndObject();
            generator.writeEndObject();

            generator.close();

            result = baos.toString("UTF-8");
        } catch (Exception ex) {
            log.warn("Error marshalling form:", ex);
        }

        return result;
    }

    private void marshallDropDownOptions(Form form, String namespace, FormRenderContext context, JsonGenerator generator) throws IOException {
        generator.writeObjectFieldStart(OPTIONS);
        for (FormElement field : form.getElements()) {

            if (!(field instanceof DropDown)) continue;

            String fieldId = StringUtils.isEmpty(namespace) ? field.getName() : namespace + NAMESPACE_SEPARATOR + field.getName();

            String options = optionsMarshaller.marshallOptionsForField((Field)field, context);

            if (StringUtils.isEmpty(options)) {
                generator.writeArrayFieldStart(fieldId);
                generator.writeEndArray();
            } else {
                generator.writeFieldName(fieldId);
                generator.writeRawValue(options);
            }
        }
        generator.writeEndObject();
    }

    private void marshallStatus(Form form, Map inputData, String namespace, FormRenderContext context, JsonGenerator generator) throws IOException {
        generator.writeFieldName(VALUES);

        Map<String, Object> mapToMarshall = new HashMap<String, Object>();
        for (FormElement formElement : form.getElements()) {

            Field field = (Field) formElement;

            String fieldId = StringUtils.isEmpty(namespace) ? field.getName() : namespace + NAMESPACE_SEPARATOR + field.getName();

            if (StringUtils.isEmpty(field.getBindingExpression())) {
                mapToMarshall.put(fieldId, null);
                continue;
            }

            DataHolder dataHolder = BindingUtils.getDataHolderForField(field);

            Object value;

            if (dataHolder == null) {
                value = getUnbindedValue(field, inputData);
            } else {
                value = getBindedValue(field, dataHolder, inputData, namespace);
            }

            if (value == null) mapToMarshall.put(fieldId, null);
            else {
                value = field.getMarshaller().marshallValue(value, context);
                mapToMarshall.put(fieldId, value);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, mapToMarshall);
        String valuesJSON = writer.toString();
        generator.writeRawValue(valuesJSON);
        writer.close();
    }

    protected Object getBindedValue(Field field, DataHolder holder, Map<String, Object> bindingData, String namespace) {
        Object value = null;

        try {
            Object bindingValue = bindingData.get(holder.getUniqueId());
            if (bindingValue != null && holder.isAssignableValue(bindingValue)) {
                value =  holder.readValue(bindingValue, BindingUtils.extractBindingExpression(field.getBindingExpression()));
            }
        } catch (Exception e) {
            log.warn("Unable to read value from expression '" + field.getBindingExpression() + "'. Error: ", e);
            value = bindingData.get(field.getBindingExpression());
        }
        return value;
    }

    protected Object getUnbindedValue(Field field, Map<String, Object> bindingData) {
        String bindingExpression = field.getBindingExpression();
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

            Map<String, Object> result = context.getInputData();

            ObjectMapper mapper = new ObjectMapper();

            Map<String, String> valuesMap = mapper.readValue(marshalledValues, new TypeReference<HashMap<String,String>>(){});

            for (String fieldId : valuesMap.keySet()) {
                Field field = form.getField(fieldId);

                if (field != null) {
                    String bindingString = field.getBindingExpression();

                    if (StringUtils.isEmpty(bindingString)) continue;

                    DataHolder dataHolder = BindingUtils.getDataHolderForField(field);

                    Object previousValue;

                    if (dataHolder == null) {
                        previousValue = getUnbindedValue(field, result);
                    } else {
                        previousValue = getBindedValue(field, dataHolder, result, "");
                    }

                    Object value = field.getMarshaller().unMarshallValue(valuesMap.get(fieldId), previousValue, context);

                    boolean simpleBinding = StringUtils.isEmpty(bindingString) || bindingString.indexOf("/") == -1;

                    if (dataHolder == null || simpleBinding) result.put(bindingString, value);
                    else {
                        String holderFieldId = bindingString.substring((dataHolder.getUniqueId() + "/").length());

                        Object holderOutputValue = result.get(dataHolder.getUniqueId());
                        if (holderOutputValue == null || !dataHolder.isAssignableValue(holderOutputValue)) {
                            holderOutputValue = dataHolder.createInstance(context);
                            result.put(dataHolder.getUniqueId(), holderOutputValue);
                        }

                        dataHolder.writeValue(holderOutputValue, holderFieldId, value);
                    }
                }
            }

        } catch (Exception e) {
            log.error("Error unmarshalling values: {}", e);
        }
    }


}
