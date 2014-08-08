/**
 * Copyright (C) 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.formModeler.ng.services.management.forms.impl;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.xerces.parsers.DOMParser;
import org.jbpm.formModeler.ng.model.DataHolder;
import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.model.Form;
import org.jbpm.formModeler.ng.services.LocaleManager;
import org.jbpm.formModeler.ng.services.management.dataHolders.DataHolderBuildConfig;
import org.jbpm.formModeler.ng.services.management.dataHolders.DataHolderManager;
import org.jbpm.formModeler.ng.services.management.forms.FieldManager;
import org.jbpm.formModeler.ng.services.management.forms.FormManager;
import org.jbpm.formModeler.ng.services.management.forms.FormSerializationManager;
import org.jbpm.formModeler.ng.services.management.forms.impl.xml.utils.XMLNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

@ApplicationScoped
public class FormSerializationManagerImpl implements FormSerializationManager {

    protected static final String NODE_FORM = "form";
    protected static final String NODE_FIELD = "field";
    protected static final String NODE_PROPERTY = "property";
    protected static final String NODE_DATA_HOLDER = "dataHolder";

    protected static final String ATTR_ID = "id";
    protected static final String ATTR_INPUT_ID = "inputId";
    protected static final String ATTR_OUT_ID = "outId";
    protected static final String ATTR_POSITION = "position";
    protected static final String ATTR_TYPE = "type";
    protected static final String ATTR_BAG_TYPE = "bag-type";
    protected static final String ATTR_SUPPORTED_TYPE = "supportedType";
    protected static final String ATTR_NAME = "name";
    protected static final String ATTR_VALUE = "value";

    @Inject
    private DataHolderManager dataHolderManager;

    @Inject
    private LocaleManager localeManager;

    protected Logger log = LoggerFactory.getLogger(FormSerializationManager.class);

    @Inject
    protected FormManager formManager;

    @Inject
    protected FieldManager fieldManager;

    public String generateFormXML(Form form) {
        XMLNode rootNode = new XMLNode(NODE_FORM, null);

        try {
            return generateFormXML(form, rootNode);
        } catch (Exception e) {
            log.error("Error serializing form to XML.", e);
            return "";
        }
    }

    @Override
    public String generateHeaderFormFormId(long formId) throws IOException {
        XMLNode rootNode = new XMLNode(NODE_FORM, null);
        rootNode.addAttribute(ATTR_ID, String.valueOf(formId));
        StringWriter sw = new StringWriter();
        rootNode.writeXML(sw, true);

        return sw.toString().replace("/", "").trim();
    }

    @Override
    public Form loadFormFromXML(String xml, Map<String, Object> context) throws Exception {
        if (StringUtils.isBlank(xml)) return null;
        return loadFormFromXML(new InputSource(new StringReader(xml)), context, null);
    }

    @Override
    public Form loadFormFromXML(String xml) throws Exception {
        if (StringUtils.isBlank(xml)) return null;
        return loadFormFromXML(new InputSource(new StringReader(xml)), null);
    }

    @Override
    public Form loadFormFromXML(InputStream is) throws Exception {
        return loadFormFromXML(is, null);
    }

    @Override
    public Form loadFormFromXML(InputStream is, Map<String, Properties> resources) throws Exception {
        return loadFormFromXML(new InputSource(is), resources);
    }

    public Form loadFormFromXML(InputSource source, Map<String, Properties> resources) throws Exception {
        DOMParser parser = new DOMParser();
        parser.parse(source);
        Document doc = parser.getDocument();
        NodeList nodes = doc.getElementsByTagName(NODE_FORM);
        Node rootNode = nodes.item(0);
        return deserializeForm(rootNode, new HashMap<String, Object>(), resources);
    }

    public Form loadFormFromXML(InputSource source,Map<String, Object> context, Map<String, Properties> resources) throws Exception {
        DOMParser parser = new DOMParser();
        parser.parse(source);
        Document doc = parser.getDocument();
        NodeList nodes = doc.getElementsByTagName(NODE_FORM);
        Node rootNode = nodes.item(0);
        return deserializeForm(rootNode, context, resources);
    }

    public Form deserializeForm(Node nodeForm, Map<String, Object> context, Map<String, Properties> resources) throws Exception {
        if (!nodeForm.getNodeName().equals(NODE_FORM)) return null;

        Form form = formManager.createForm();
        form.setId(Long.valueOf(StringEscapeUtils.unescapeXml(nodeForm.getAttributes().getNamedItem(ATTR_ID).getNodeValue())));

        NodeList childNodes = nodeForm.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeName().equals(NODE_PROPERTY)) {
                String propName = node.getAttributes().getNamedItem(ATTR_NAME).getNodeValue();
                String value = StringEscapeUtils.unescapeXml(node.getAttributes().getNamedItem(ATTR_VALUE).getNodeValue());
                if ("subject".equals(propName)) {
                    form.setSubject(value);
                } else if ("name".equals(propName)) {
                    form.setName(value);
                } else if ("displayMode".equals(propName)) {
                    form.setDisplayMode(value);
                } else if ("labelMode".equals(propName)) {
                    form.setLabelMode(value);
                } else if ("showMode".equals(propName)) {
                    form.setShowMode(value);
                }
            } else if (node.getNodeName().equals(NODE_FIELD)) {
                deserializeField(form, node, resources);
            } else if (node.getNodeName().equals(NODE_DATA_HOLDER)) {
                String holderId = getNodeAttributeValue(node, ATTR_ID);
                String holderInputId = getNodeAttributeValue(node, ATTR_INPUT_ID);
                String holderOutId = getNodeAttributeValue(node, ATTR_OUT_ID);
                String holderType = getNodeAttributeValue(node, ATTR_TYPE);
                String holderValue = getNodeAttributeValue(node, ATTR_VALUE);
                String holderRenderColor = getNodeAttributeValue(node, ATTR_NAME);
                String holderSupportedType = getNodeAttributeValue(node, ATTR_SUPPORTED_TYPE);

                if (!StringUtils.isEmpty(holderId) && !StringUtils.isEmpty(holderType) && !StringUtils.isEmpty(holderValue)) {

                    DataHolderBuildConfig config = new DataHolderBuildConfig(holderId, holderInputId, holderOutId, holderRenderColor, holderValue);
                    if (context.get("path") != null)config.addAttribute("path", context.get("path"));
                    if (!StringUtils.isEmpty(holderSupportedType))
                        config.addAttribute(ATTR_SUPPORTED_TYPE, holderSupportedType);

                    DataHolder holder = dataHolderManager.createDataHolderByType(holderType, config);

                    if (!StringUtils.isEmpty(holderId)) form.addDataHolder(holder);
                }
            }
        }
        return form;
    }

    protected String getNodeAttributeValue(Node node, String attributeName) {
        Node attribute = node.getAttributes().getNamedItem(attributeName);
        return attribute != null ? attribute.getNodeValue() : "";
    }

    private void addXMLNode(String propName, String value, XMLNode parent) {
        if (value != null) {
            XMLNode propertyNode = new XMLNode(NODE_PROPERTY, parent);
            propertyNode.addAttribute(ATTR_NAME, propName);
            propertyNode.addAttribute(ATTR_VALUE, value);
            parent.addChild(propertyNode);
        }
    }


    /**
     * Generates the xml representation and mount in rootNode the structure to be included.
     * Fills the XMLNode structure with the form representation and returns the string.
     */
    public String generateFormXML(Form form, XMLNode rootNode) throws Exception {
        rootNode.addAttribute(ATTR_ID, form.getId().toString());

        addXMLNode("subject", form.getSubject(), rootNode);
        addXMLNode("name", form.getName(), rootNode);
        addXMLNode("displayMode", form.getDisplayMode(), rootNode);
        addXMLNode("labelMode", form.getLabelMode(), rootNode);
        addXMLNode("showMode", form.getShowMode(), rootNode);

        int index = 0;
        for (LinkedList<Field> fields : form.getElementsGrid()) {
            for (Field field : fields) {
                generateFieldXML(field, index, rootNode);
                index ++;
            }
        }

        for (DataHolder dataHolder : form.getHolders()) {
            generateDataHolderXML(dataHolder, rootNode);
        }

        StringWriter sw = new StringWriter();
        rootNode.writeXML(sw, true);

        return sw.toString();
    }

    public void deserializeField(Form form, Node nodeField, Map<String, Properties> resources) throws Exception {
        if (!nodeField.getNodeName().equals(NODE_FIELD)) return;

        String code = nodeField.getAttributes().getNamedItem(ATTR_TYPE).getNodeValue();

        if (StringUtils.isEmpty(code)) return;

        Field field = fieldManager.getFieldByCode(code);

        if (field == null) return;

        field.setId(Long.valueOf(nodeField.getAttributes().getNamedItem(ATTR_ID).getNodeValue()));
        field.setName(nodeField.getAttributes().getNamedItem(ATTR_NAME).getNodeValue());
        field.setPosition(Integer.parseInt(nodeField.getAttributes().getNamedItem(ATTR_POSITION).getNodeValue()));

        Map<String, String> properties = new HashMap<String, String>();

        NodeList fieldPropsNodes = nodeField.getChildNodes();
        for (int j = 0; j < fieldPropsNodes.getLength(); j++) {
            Node nodeFieldProp = fieldPropsNodes.item(j);
            if (nodeFieldProp.getNodeName().equals(NODE_PROPERTY)) {
                String propName = nodeFieldProp.getAttributes().getNamedItem(ATTR_NAME).getNodeValue();
                String value = StringEscapeUtils.unescapeXml(nodeFieldProp.getAttributes().getNamedItem(ATTR_VALUE).getNodeValue());
                if (propName != null && value != null) {
                    if ("fieldRequired".equals(propName)) {
                        field.setFieldRequired(Boolean.valueOf(value));
                    } else if ("groupWithPrevious".equals(propName)) {
                        field.setGroupWithPrevious(Boolean.valueOf(value));
                    } else if ("label".equals(propName)) {
                        field.setLabel(deserializeI18nEntrySet(value));
                    } else if ("readonly".equals(propName)) {
                        field.setReadonly(Boolean.valueOf(value));
                    } else if ("inputBinding".equals(propName)) {
                        field.setInputBinding(value);
                    } else if ("outputBinding".equals(propName)) {
                        field.setOutputBinding(value);
                    } else {
                        properties.put(propName, value);
                    }
                }
            }
        }

        field.setCustomProperties(properties);

        if (resources != null) {
            field.setLabel(new HashMap());
            if (resources.containsKey("default")) {
                resources.put(localeManager.getDefaultLang(), resources.remove("default"));
            }
            for (String lang : resources.keySet()) {
                Properties props = resources.get(lang);
                String value = getFieldProperty(form.getName(), field.getName(), "label", props);
                if (!StringUtils.isEmpty(value)) field.getLabel().put(lang, value);
            }
        }

        form.addField(field);
    }

    private String getFieldProperty(String formName, String fieldName, String selector, Properties props) {
        if (props == null) return null;

        String value = props.getProperty(formName + "." + fieldName + "." + selector);

        if (StringUtils.isEmpty(value)) value = props.getProperty(fieldName + "." + selector);

        return value;
    }

    public void generateFieldXML(Field field, int index, XMLNode parent) {
        XMLNode rootNode = new XMLNode(NODE_FIELD, parent);
        rootNode.addAttribute(ATTR_ID, String.valueOf(field.getId()));
        rootNode.addAttribute(ATTR_POSITION, String.valueOf(index));
        rootNode.addAttribute(ATTR_NAME, field.getName());
        rootNode.addAttribute(ATTR_TYPE, field.getCode());

        if (field.getLabel() != null) addXMLNode("label", serializeI18nSet(field.getLabel()), rootNode);
        addXMLNode("readonly", (field.getReadonly() != null ? String.valueOf(field.getReadonly()) : null), rootNode);
        addXMLNode("fieldRequired", (field.getFieldRequired() != null ? String.valueOf(field.getFieldRequired()) : null), rootNode);
        addXMLNode("groupWithPrevious", (field.getGroupWithPrevious() != null ? String.valueOf(field.getGroupWithPrevious()) : null), rootNode);
        if (!StringUtils.isEmpty(field.getInputBinding()))
            addXMLNode("inputBinding", field.getInputBinding(), rootNode);
        if (!StringUtils.isEmpty(field.getOutputBinding()))
            addXMLNode("outputBinding", field.getOutputBinding(), rootNode);

        Map<String, String> properties = field.getCustomProperties();

        if (properties != null) {
            for (String key : properties.keySet()) {
                addXMLNode(key, properties.get(key), rootNode);
            }
        }
        parent.addChild(rootNode);
    }

    public void generateDataHolderXML(DataHolder dataHolder, XMLNode parent) {
        XMLNode rootNode = new XMLNode(NODE_DATA_HOLDER, parent);
        rootNode.addAttribute(ATTR_ID, dataHolder.getUniqueId());
        rootNode.addAttribute(ATTR_INPUT_ID, dataHolder.getInputId());
        rootNode.addAttribute(ATTR_OUT_ID, dataHolder.getOutputId());
        rootNode.addAttribute(ATTR_TYPE, dataHolder.getTypeCode());
        rootNode.addAttribute(ATTR_VALUE, dataHolder.getClassName());
        rootNode.addAttribute(ATTR_NAME, dataHolder.getRenderColor());

        parent.addChild(rootNode);
    }

    protected String[] decodeStringArray(String textValue) {
        if (textValue == null || textValue.trim().length() == 0) return new String[0];
        String[] lista;
        lista = textValue.split("quot;");
        return lista;
    }

    protected String encodeStringArray(String[] value) {
        String cad = "";
        for (int i = 0; i < value.length; i++) {
            cad += "quot;" + value[i] + "quot;";
            i++;
            cad += ",quot;" + value[i] + "quot;";
        }
        return cad;
    }

    public String serializeI18nSet(Map<String, String> valuesMap) {
        String[] values = new String[valuesMap.keySet().size() * 2];

        int i = 0;
        for (String key : valuesMap.keySet()) {
            values[i] = key;
            i++;
            values[i] = valuesMap.get(key);
            i++;
        }

        return encodeStringArray(values);
    }

    public Map deserializeI18nEntrySet(String cadena) {
        String[] values = decodeStringArray(cadena);
        Map<String, String> mapValues = new HashMap();
        for (int i = 0; i < values.length; i = i + 4) {
            String key = values[i + 1];
            String value = "";
            if (i + 3 < values.length) {
                value = values[i + 3];
            }
            if (key.length() == 2) {
                mapValues.put(key, value);
            }

        }
        return mapValues;
    }

}
