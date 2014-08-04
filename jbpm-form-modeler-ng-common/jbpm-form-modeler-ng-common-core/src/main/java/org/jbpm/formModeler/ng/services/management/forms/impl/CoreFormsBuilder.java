package org.jbpm.formModeler.ng.services.management.forms.impl;

import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.model.Form;
import org.jbpm.formModeler.ng.services.LocaleManager;
import org.jbpm.formModeler.ng.services.management.Startable;
import org.jbpm.formModeler.ng.services.management.forms.FieldManager;
import org.jbpm.formModeler.ng.services.management.forms.FormManager;
import org.jbpm.formModeler.ng.services.management.forms.FormSerializationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.InputStream;
import java.util.*;

@ApplicationScoped
public class CoreFormsBuilder implements Startable {
    private Logger log = LoggerFactory.getLogger(CoreFormsBuilder.class);

    @Inject
    private FormManager formManager;

    @Inject
    private FormSerializationManager formSerializationManager;

    @Inject
    private FieldManager fieldManager;

    @Inject
    private LocaleManager localeManager;

    List<Form> systemForms = new ArrayList<Form>();

    @Override
    public void start() throws Exception {
        Map<String, Properties> formResources = new HashMap<String, Properties>();

        for (String lang : localeManager.getPlatformAvailableLangs()) {
            try {
                String key = lang.equals(localeManager.getDefaultLang()) ? "" : "_" + lang;

                InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(getFormResourcesPath(key));

                if (in == null) continue;
                Properties props = new Properties();
                props.load(in);
                formResources.put(lang, props);
            } catch (Exception e) {
                log.warn("Error loading resources form lang \"{}\": {}", lang, e);
            }
        }

        deployFieldTypeForm("default", formResources);
        deployFieldTypesForms(fieldManager.getBasicFields(), formResources);
        formManager.setSystemForms(systemForms);
    }

    protected void deployFieldTypesForms(List<? extends Field> fieldTypes, Map<String, Properties> formResources) {
        if (fieldTypes == null) return;
        for (Field fieldType : fieldTypes) {
            deployFieldTypeForm(fieldType.getCode(), formResources);
        }
    }

    protected void deployFieldTypeForm(String formName, Map<String, Properties> formResources) {
        String formPath = getFormPath(formName);
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(formPath);
            if (is == null) return;
            Form systemForm = formSerializationManager.loadFormFromXML(is, formResources);
            systemForms.add(systemForm);
        } catch (Exception e) {
            log.error("Error reading core form file: " + formPath, e);
        }
    }

    public String getFormPath(String formName) {
        return "org/jbpm/formModeler/ng/forms/" + formName + ".form";
    }

    public String getFormResourcesPath(String lang) {
        return "org/jbpm/formModeler/ng/forms/forms-resources" + lang + ".properties";
    }
}
