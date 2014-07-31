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
package org.jbpm.formModeler.ng.services.management.dataHolders.builders;

import org.apache.commons.lang.StringUtils;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.jbpm.formModeler.ng.model.DataFieldHolder;
import org.jbpm.formModeler.ng.model.DataHolder;
import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;
import org.jbpm.formModeler.ng.services.management.forms.FieldManager;

import java.util.Set;
import java.util.TreeSet;

public class BasicTypeDataHolder extends DataHolder {
    private transient Field field;

    protected transient FieldManager fieldManager;

    protected Set<DataFieldHolder> dataFieldHolders;

    BasicTypeDataHolder(String uniqueId, String inputId, String outputId, String className, String renderColor) {
        this.uniqueId = uniqueId;
        this.inputId = inputId;
        this.outputId = outputId;
        this.className = className;
        this.renderColor = renderColor;
        this.fieldManager = BeanProvider.getContextualReference(FieldManager.class, true);
        try {
            this.field = fieldManager.getFieldByClass(className);
        } catch (Exception e) {

        }
    }

    public Object createInstance(FormRenderContext context) throws Exception {
        String className = field.getFieldClass();
        return Class.forName(className).newInstance();
    }

    @Override
    public void writeValue(Object destination, String propName, Object value) throws Exception {
    }

    @Override
    public Object readValue(Object source, String propName) throws Exception {
        return source;
    }

    @Override
    public Set<DataFieldHolder> getFieldHolders() {
        try {
            if (dataFieldHolders == null || dataFieldHolders.size() == 0) {
                dataFieldHolders = new TreeSet<DataFieldHolder>();
                DataFieldHolder datafieldHolder = new DataFieldHolder(this, StringUtils.defaultIfEmpty(inputId, outputId), field.getFieldClass(), field.getIcon());
                dataFieldHolders.add(datafieldHolder);

            }
            return dataFieldHolders;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public String getTypeCode() {
        return BasicTypeHolderBuilder.HOLDER_TYPE_BASIC_TYPE;
    }


    @Override
    public boolean isAssignableValue(Object value) {
        if (value == null) return true;
        try {
            return value.getClass().getName().equals(this.field.getFieldClass()) || Class.forName(this.field.getFieldClass()).isAssignableFrom(value.getClass());
        } catch (ClassNotFoundException e) {
        }
        return false;
    }

    @Override
    public boolean canHaveChildren() {
        return false;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
