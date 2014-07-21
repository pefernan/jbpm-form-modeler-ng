package org.jbpm.formModeler.ng.services.context.impl.marshalling;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
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
import java.util.Map;

@Default
@ApplicationScoped
public class JSONFormRendercontextMarshaller implements FormRenderContextMarshaller {
    public static final String NAMESPACE_SEPARATOR = "-";

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

            generator.writeStartObject();
            generator.writeNumberField("id", form.getId());
            generator.writeStringField("name", form.getName());
            generator.writeStringField("displayMode", form.getDisplayMode());
            generator.writeStringField("labelMode", form.getLabelMode());

            generator.writeArrayFieldStart("fields");
            for (Field field : form.getFormFields()) {

                DataHolder dataHolder = BindingUtils.getFormDataHolderForField(field);

                marshallField(field, dataHolder, context.getData(), context, "", generator);
            }
            generator.writeEndArray();

            generator.writeEndObject();

            generator.close();

            result = baos.toString("UTF-8");
        } catch (Exception ex) {
            log.warn("Error marshalling form: {}", ex);
        }

        return result;
    }

    private void marshallField(Field field, DataHolder dataHolder, Map<String, Object> data, FormRenderContext context, String namespace, JsonGenerator generator) throws IOException {
        generator.writeStartObject();

        generator.writeStringField("id", StringUtils.isEmpty(namespace) ? field.getName() : namespace + NAMESPACE_SEPARATOR + field.getName());
        generator.writeStringField("label", (String) localeManager.localize(field.getLabel()));
        generator.writeStringField("type", field.getCode());
        generator.writeNumberField("position", field.getPosition());
        generator.writeBooleanField("grouped", field.getGroupWithPrevious());

        Object value = null;

        if (dataHolder != null) value = getBindedValue(field, dataHolder, data);
        else value = getUnbindedValue(field, data);

        generator.writeObjectField("value", field.getMarshaller().marshallValue(value));

        generator.writeEndObject();
    }

    protected Object getBindedValue(Field field, DataHolder holder, Map<String, Object> loadData) {
        Object value = null;

        try {
            String expression = field.getOutputBinding();
            Object bindingValue = loadData.get(holder.getUniqueId());
            if (bindingValue == null) loadData.get(holder.getOutputId());
            if (bindingValue == null) {
                expression = field.getInputBinding();
                loadData.get(holder.getInputId());
            }
            if (bindingValue != null && holder.isAssignableValue(bindingValue)) {
                value = holder.readValue(bindingValue, BindingUtils.extractInputExpression(expression));
            }
        } catch (Exception e) {
            log.warn("Unable to read field value '{}' from {}. Error: {}", field, loadData, e);
        }
        return value;
    }

    protected Object getUnbindedValue(Field field, Map<String, Object> bindingData) {

        Object value = null;

        try {
            if (!StringUtils.isEmpty(field.getOutputBinding())) value = getUnbindedValue(field.getOutputBinding(), bindingData);

        } catch (Exception e) {
            log.warn("Error getting value for xpath expression '{}': {}", field.getOutputBinding(), e);
            if (value == null) value = getUnbindedValue(field.getInputBinding(), bindingData);
        }
        return value;
    }

    protected Object getUnbindedValue(String bindingExpression, Map<String, Object> bindingData) {
        if (bindingExpression.indexOf("/") != -1) {

            String root = bindingExpression.substring(0, bindingExpression.indexOf("/"));
            String expression = bindingExpression.substring(root.length() + 1);

            Object object = bindingData.get(root);
            JXPathContext ctx = JXPathContext.newContext(object);
            return ctx.getValue(expression);

        }
        return bindingData.get(bindingExpression);
    }

    @Override
    public void unmarshallContext(FormRenderContext context, String marshalledValues) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
