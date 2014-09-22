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
package org.jbpm.formModeler.ng.services;

import org.jbpm.formModeler.ng.model.DataHolder;
import org.jbpm.formModeler.ng.model.Form;
import org.jbpm.formModeler.ng.services.management.dataHolders.DataHolderBuildConfig;
import org.jbpm.formModeler.ng.services.management.dataHolders.DataHolderManager;
import org.jbpm.formModeler.ng.services.management.forms.FormManager;
import org.jbpm.formModeler.ng.services.management.forms.FormSerializationManager;
import org.jbpm.formModeler.ng.services.management.forms.impl.CoreFormsBuilder;
import org.jbpm.formModeler.ng.utils.WeldJUnit4Runner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.HashMap;

import static org.junit.Assert.*;

@RunWith(WeldJUnit4Runner.class)
public class FormModelerServicesTest {

    @Inject
    private FormManager formManager;

    @Inject
    private DataHolderManager dataHolderManager;

    @Inject
    private FormSerializationManager formSerializationManager;

    @Inject
    private CoreFormsBuilder formsBuilder;

    @Test
    public void testSystemForms() throws Exception {
        formsBuilder.start();
        formManager.getSystemForms();
        assertNotEquals("There are no forms on FormManager.", formManager.getSystemForms().size(), 0);

    }

    @Test
    public void createForm() throws Exception {
        Form newForm = formManager.createForm("newForm");

        assertNotNull("Unable to create form.", newForm);

        DataHolderBuildConfig config = new DataHolderBuildConfig("string", "red", String.class.getName());

        DataHolder basicHolder = dataHolderManager.createDataHolderByValueType(config, new HashMap<String, Object>());

        assertNotNull("Unable to create basic type DataHolder.", basicHolder);

        config = new DataHolderBuildConfig("pojo1", "blue", Pojo1.class.getName());

        DataHolder pojoHolder = dataHolderManager.createDataHolderByValueType(config, new HashMap<String, Object>());

        assertNotNull("Unable to create pojo DataHolder.", pojoHolder);

        formManager.addAllDataHolderFieldsToForm(newForm, basicHolder);

        assertEquals("Added fields != 1.", newForm.getFieldsCount(), 1);

        formManager.addAllDataHolderFieldsToForm(newForm, pojoHolder);

        assertEquals("Added fields from Pojo != 5.", newForm.getFieldsCount(), 5);

        DataHolderBuildConfig config2 = new DataHolderBuildConfig("boolean", "yellow", Boolean.class.getName());

        DataHolder basicHolder2 = dataHolderManager.createDataHolderByValueType(config2, new HashMap<String, Object>());

        formManager.addDataHolderToForm(newForm, basicHolder2);

        assertEquals("Form dataHolder != 3.", newForm.getHolders().size(), 3);

        formManager.removeDataHolderFromForm(newForm, basicHolder2.getUniqueId());

        assertEquals("Form dataHolder != 2.", newForm.getHolders().size(), 2);

        String xml = formSerializationManager.generateFormXML(newForm);

        assertNotNull("Serialized xml is null.", xml);

        Form serializedForm = formSerializationManager.loadFormFromXML(xml);

        assertNotNull("Serialized form is null.", serializedForm);

        assertTrue("Original Form and Serialized form are not equal.", newForm.equals(serializedForm));

    }
}
