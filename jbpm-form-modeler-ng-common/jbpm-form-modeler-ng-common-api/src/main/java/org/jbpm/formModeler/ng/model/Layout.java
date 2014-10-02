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
package org.jbpm.formModeler.ng.model;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public interface Layout extends Serializable {
    public String getCode();
    public String getName(Locale locale);

    public List<LayoutArea> getAreas();
    public void addElement(FormElement element);
    void addElement(int row, int column, Long fieldId);

    void removeElement(Long fieldId);
}