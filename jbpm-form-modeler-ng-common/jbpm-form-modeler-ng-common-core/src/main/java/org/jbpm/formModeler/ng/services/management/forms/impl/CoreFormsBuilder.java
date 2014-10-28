package org.jbpm.formModeler.ng.services.management.forms.impl;

import org.apache.commons.io.IOUtils;
import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.model.Form;
import org.jbpm.formModeler.ng.services.LocaleManager;
import org.jbpm.formModeler.ng.services.management.Startable;
import org.jbpm.formModeler.ng.services.management.forms.FieldManager;
import org.jbpm.formModeler.ng.services.management.forms.FormDefinitionMarshaller;
import org.jbpm.formModeler.ng.services.management.forms.FormManager;
import org.jbpm.formModeler.ng.services.management.forms.FormSerializationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@ApplicationScoped
public class CoreFormsBuilder implements Startable {
    private Logger log = LoggerFactory.getLogger(CoreFormsBuilder.class);

    @Inject
    private FormManager formManager;

    @Inject
    private FormSerializationManager formSerializationManager;

    @Inject
    private FormDefinitionMarshaller manager;

    @Inject
    private FieldManager fieldManager;

    @Inject
    private LocaleManager localeManager;

    List<Form> systemForms = new ArrayList<Form>();

    @Override
    public void start() throws Exception {

        deployFieldTypeForm("default");
        deployFieldTypesForms(fieldManager.getBasicFields());
        deployFieldTypesForms(fieldManager.getComplexFields());
        formManager.setSystemForms(systemForms);
    }

    protected void deployFieldTypesForms(List<? extends Field> fieldTypes) {
        if (fieldTypes == null) return;
        for (Field fieldType : fieldTypes) {
            deployFieldTypeForm(fieldType.getCode());
        }
    }

    protected void deployFieldTypeForm(String formName) {
        String formPath = getFormPath(formName);
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(formPath);
            if (is == null) return;

            String formTemplate = IOUtils.toString(is, StandardCharsets.UTF_8.name());

            Form systemForm = manager.unmarshall(formTemplate);

            systemForms.add(systemForm);
        } catch (Exception e) {
            log.error("Error reading core form file: " + formPath, e);
        }
    }

    public String getFormPath(String formName) {
        return "org/jbpm/formModeler/ng/forms/new/" + formName + ".json";
    }
}
