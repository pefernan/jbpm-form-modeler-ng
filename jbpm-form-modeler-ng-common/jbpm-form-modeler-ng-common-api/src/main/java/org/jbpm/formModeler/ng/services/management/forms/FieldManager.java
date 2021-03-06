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

import org.jbpm.formModeler.ng.model.BasicField;
import org.jbpm.formModeler.ng.model.ComplexField;
import org.jbpm.formModeler.ng.model.Field;

import java.io.Serializable;
import java.util.List;

public interface FieldManager extends Serializable {

    List<BasicField> getBasicFields();

    List<ComplexField> getComplexFields();

    List<Field> getSuitableFields(Field field);

    Field getFieldByCode(String typeCode);

    Field getFieldByClass(String classType);

    boolean isVisible(String typeCode);
}
