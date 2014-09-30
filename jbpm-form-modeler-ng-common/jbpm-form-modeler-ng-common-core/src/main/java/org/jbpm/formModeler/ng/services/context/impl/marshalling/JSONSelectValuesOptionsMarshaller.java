package org.jbpm.formModeler.ng.services.context.impl.marshalling;


import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.model.impl.DropDown;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;
import org.jbpm.formModeler.ng.services.management.forms.SelectValuesOptionsMarshaller;
import org.jbpm.formModeler.ng.services.management.forms.SelectValuesProvider;
import org.jbpm.formModeler.ng.services.management.forms.SelectValuesProviderManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.util.Map;

@ApplicationScoped
public class JSONSelectValuesOptionsMarshaller implements SelectValuesOptionsMarshaller {
    public static final String VALUE = "value";
    public static final String TEXT = "text";

    protected Logger log = LoggerFactory.getLogger(JSONSelectValuesOptionsMarshaller.class);

    @Inject
    SelectValuesProviderManager selectValuesProviderManager;

    @Override
    public String marshallOptionsForField(Field field, FormRenderContext context) {
        if (field == null || context == null) return "";

        String result = "";
        JsonGenerator generator = null;
        try {

            DropDown dropDown = (DropDown) field;

            SelectValuesProvider provider = selectValuesProviderManager.getRangeProviderByType(dropDown.getProvider());

            if (provider != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                JsonFactory f = new JsonFactory();
                generator = f.createJsonGenerator(baos, JsonEncoding.UTF8);

                generator.writeStartArray();

                Map<String, String> options = provider.getSelectOptions(dropDown, context);
                for (String key : options.keySet()) {
                    generator.writeStartObject();
                    generator.writeStringField(VALUE, key);
                    generator.writeStringField(TEXT, options.get(key));
                    generator.writeEndObject();
                }

                generator.writeEndArray();

                generator.close();

                result = baos.toString("UTF-8");
            }
        } catch (Exception ex) {
            log.warn("Error marshalling list options:", ex);
        }

        return result;

    }
}
