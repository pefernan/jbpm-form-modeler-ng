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
package org.jbpm.formModeler.ng.services.management.forms;

import org.jbpm.formModeler.ng.model.DataFieldHolder;
import org.jbpm.formModeler.ng.model.DataHolder;
import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.model.Form;

import java.io.Serializable;
import java.util.List;

/**
 * Manager interface form Forms.
 * <p/>It provides the common operations over Forms (creation, edit, delete) and distinct search methods.
 */
public interface FormManager extends Serializable {

    List<Form> getSystemForms();

    Form getFormForFieldEdition(String code);

    Form getFormById(Long id);

    Form createForm();

    Form createForm(String name);

    void changeFieldPosition(Form form, Long fieldId, int row, int column, boolean newLine);

    void moveFirst(Form pForm, Long fieldId);

    void moveLast(Form pForm, Long fieldId);

    public void addDataHolderToForm(Form form, DataHolder holder);

    public void removeDataHolderFromForm(Form form, String holderId);

    public void addAllDataHolderFieldsToForm(Form form, String holderId);

    public void addAllDataHolderFieldsToForm(Form form, DataHolder holder);

    Form getFormByName(String name);

    boolean addDataHolderField(Form form, DataHolder holder, DataFieldHolder field);

    boolean addFieldByType(Form form, String fieldType);

    void setSystemForms(List<Form> systemForms);

    boolean changeFieldType(Form form, Field field, String code);
}
