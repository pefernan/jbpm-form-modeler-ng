package org.jbpm.formModeler.ng.services.context.impl.marshalling;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.jbpm.formModeler.ng.model.DataHolder;
import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.model.Form;
import org.jbpm.formModeler.ng.services.LocaleManager;
import org.jbpm.formModeler.ng.services.management.dataHolders.DataHolderBuildConfig;
import org.jbpm.formModeler.ng.services.management.dataHolders.DataHolderManager;
import org.jbpm.formModeler.ng.services.management.forms.FieldManager;
import org.jbpm.formModeler.ng.services.management.forms.FormDefinitionMarshaller;
import org.jbpm.formModeler.ng.services.management.forms.FormManager;
import org.jbpm.formModeler.ng.services.management.forms.utils.BindingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class JSONFormDefinitionMarshaller implements FormDefinitionMarshaller {
    protected Logger log = LoggerFactory.getLogger(JSONFormDefinitionMarshaller.class);

    public static final String ID = "id";
    public static final String TYPE = "type";

    public static final String FORM_NAME = "name";
    public static final String FORM_DISPLAY_MODE = "displayMode";
    public static final String FORM_LABEL_MODE = "labelMode";

    public static final String FIELDS = "fields";
    public static final String FIELD_UID = "uid";
    public static final String FIELD_REQUIRED = "required";
    public static final String FIELD_ROW = "row";
    public static final String FIELD_COLUMN = "column";
    public static final String FIELD_LABEL = "label";
    public static final String FIELD_READONLY = "readonly";
    public static final String FIELD_BINDING_EXPRESSION = "bindingExpression";
    public static final String FIELD_HOLDER_COLOR = "holderColor";
    public static final String FIELD_DATA = "data";

    public static final String DATAHOLDERS = "dataHolders";
    public static final String DATAHOLDER_CLASSNAME = "className";
    public static final String DATAHOLDER_COLOR = "color";

    public static final String STATUS = "status";
    public static final String VALUES = "values";

    @Inject
    private LocaleManager localeManager;

    @Inject
    private FormManager formManager;

    @Inject
    private FieldManager fieldManager;

    @Inject
    private DataHolderManager dataHolderManager;

    @Override
    public String marshall(Form form) {

        if (form == null) return null;

        String result = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JsonFactory f = new JsonFactory();
            JsonGenerator generator = f.createJsonGenerator(baos, JsonEncoding.UTF8);

            generator.writeStartObject();

            generator.writeStringField(ID, form.getId().toString());
            generator.writeStringField(FORM_NAME, form.getName());
            generator.writeStringField(FORM_DISPLAY_MODE, form.getDisplayMode());
            generator.writeStringField(FORM_LABEL_MODE, form.getLabelMode());

            marshallFields(form, generator);
            marshallDataHolders(form, generator);

            generator.writeObjectFieldStart(STATUS);
            if (StringUtils.isEmpty(form.getSerializedStatus())) {
                generator.writeObjectFieldStart(VALUES);
                generator.writeEndObject();
            } else {
                generator.writeFieldName(VALUES);
                generator.writeRawValue(form.getSerializedStatus());
            }
            generator.writeEndObject();

            generator.writeEndObject();

            generator.close();
            result = baos.toString("UTF-8");
        } catch (Exception ex) {
            log.warn("Error marshalling form:", ex);
        }

        return result;
    }

    private void marshallDataHolders(Form form, JsonGenerator generator) throws IOException {
        generator.writeArrayFieldStart(DATAHOLDERS);
        for (DataHolder holder : form.getHolders()) {
            generator.writeStartObject();
            generator.writeStringField(ID, holder.getUniqueId());
            generator.writeStringField(TYPE, holder.getTypeCode());
            generator.writeStringField(DATAHOLDER_CLASSNAME, holder.getClassName());
            generator.writeStringField(DATAHOLDER_COLOR, holder.getRenderColor());
            generator.writeEndObject();
        }
        generator.writeEndArray();
    }

    private void marshallFields(Form form, JsonGenerator generator) throws IOException {
        generator.writeArrayFieldStart(FIELDS);
        int row = 0;
        for (LinkedList<Field> fields : form.getElementsGrid()) {
            int column = 0;
            for (Field field : fields) {
                field.setColumn(column);
                field.setRow(row);

                generator.writeStartObject();
                generator.writeStringField(FIELD_UID, field.getId().toString());
                generator.writeStringField(ID, field.getName());
                generator.writeStringField(TYPE, field.getCode());

                generator.writeObjectFieldStart(FIELD_LABEL);
                for (String lang : field.getLabel().keySet()) {
                    generator.writeStringField(lang, field.getLabel().get(lang));
                    if(lang.equals(localeManager.getDefaultLang())) generator.writeStringField("default", field.getLabel().get(lang));
                }
                generator.writeEndObject();

                generator.writeStringField(FIELD_BINDING_EXPRESSION, field.getBindingExpression());
                generator.writeBooleanField(FIELD_READONLY, field.getReadonly());
                generator.writeBooleanField(FIELD_REQUIRED, field.getFieldRequired());
                generator.writeNumberField(FIELD_ROW, row);
                generator.writeNumberField(FIELD_COLUMN, column);

                String color = "";

                DataHolder dataHolder = BindingUtils.getDataHolderForField(field);
                if (dataHolder != null) color = dataHolder.getRenderColor();
                generator.writeStringField(FIELD_HOLDER_COLOR, color);

                generator.writeObjectFieldStart(FIELD_DATA);

                Map<String, String> properties = field.getCustomProperties();

                if (properties != null) {
                    for (String key : properties.keySet()) {
                        generator.writeStringField(key, properties.get(key));
                    }
                }

                generator.writeEndObject();
                generator.writeEndObject();

                column ++;
            }
            row ++;
        }
        generator.writeEndArray();
    }

    @Override
    public Form unmarshall(String formTemplate, Map<String, Object> context) {
        if (StringUtils.isEmpty(formTemplate)) return null;
        if (context == null) context = new HashMap<String, Object>();

        Form form = null;

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonForm = mapper.readTree(formTemplate);

            form = formManager.createForm(jsonForm.get(FORM_NAME).getTextValue());
            form.setId(Long.decode(jsonForm.get(ID).getTextValue()));
            form.setDisplayMode(jsonForm.get(FORM_DISPLAY_MODE).getTextValue());
            form.setLabelMode(jsonForm.get(FORM_LABEL_MODE).getTextValue());

            unmarshallFields(form, jsonForm.get(FIELDS));
            unmarshallDataHolders(form, jsonForm.get(DATAHOLDERS), context);

            JsonNode values = jsonForm.get(VALUES);
            if (values != null && !values.isNull()) {
                form.setSerializedStatus(values.toString());
            }

        } catch (IOException e) {
            log.error("Error unmarshalling form definition: ", e);
        }
        return form;
    }

    private void unmarshallDataHolders(Form form, JsonNode dataHolders, Map<String, Object> context) {
        if (dataHolders != null && !dataHolders.isNull()) {
            for (Iterator<JsonNode> it = dataHolders.getElements(); it.hasNext();) {
                JsonNode jsonHolder = it.next();
                DataHolderBuildConfig config = new DataHolderBuildConfig(jsonHolder.get(ID).getTextValue(), jsonHolder.get(DATAHOLDER_COLOR).getTextValue(), jsonHolder.get(DATAHOLDER_CLASSNAME).getTextValue());
                config.setAttributes(context);

                DataHolder holder = dataHolderManager.createDataHolderByType(jsonHolder.get(TYPE).getTextValue(), config);

                if (holder != null) form.addDataHolder(holder);
            }
        }
    }

    protected void unmarshallFields(Form form, JsonNode fields) {
        if (fields != null && !fields.isNull()) {
            int lastRow = -1;
            for (Iterator<JsonNode> it = fields.getElements(); it.hasNext();) {
                JsonNode jsonField = it.next();

                String code = jsonField.get(TYPE).getTextValue();

                if (StringUtils.isEmpty(code)) continue;

                Field field = fieldManager.getFieldByCode(code);

                if (field == null) continue;

                field.setId(Long.decode(jsonField.get(FIELD_UID).getTextValue()));
                field.setName(jsonField.get(ID).getTextValue());

                Map<String, String> properties = new HashMap<String, String>();

                for (Iterator<String> fieldsIt = jsonField.getFieldNames(); fieldsIt.hasNext();) {
                    String propName = fieldsIt.next();
                    JsonNode value = jsonField.get(propName);
                    if (value != null && !value.isNull()) {
                        if (FIELD_REQUIRED.equals(propName)) {
                            field.setFieldRequired(value.getBooleanValue());
                        } else if (FIELD_ROW.equals(propName)) {
                            int row = value.getIntValue();
                            field.setRow(value.getIntValue());
                            boolean grouped = lastRow == row;
                            field.setGroupWithPrevious(grouped);
                            if (!grouped) lastRow = row;
                        } else if (FIELD_COLUMN.equals(propName)) {
                            field.setColumn(value.getIntValue());
                        } else if (FIELD_LABEL.equals(propName)) {
                            for (Iterator<String> labelIt = value.getFieldNames(); labelIt.hasNext();) {
                                String lang = labelIt.next();
                                if (lang.equals("default")) lang = localeManager.getDefaultLang();
                                field.getLabel().put(lang, value.get(lang).getTextValue());
                            }
                        } else if (FIELD_READONLY.equals(propName)) {
                            field.setReadonly(value.getBooleanValue());
                        } else if (FIELD_BINDING_EXPRESSION.equals(propName)) {
                            field.setBindingExpression(value.getTextValue());
                        } else {
                            if (!propName.equals(TYPE) && !propName.equals(ID) && !propName.equals(FIELD_UID)) properties.put(propName, value.getTextValue());
                        }
                    }
                }
                field.setCustomProperties(properties);
                form.addField(field);
            }
        }
    }
}
