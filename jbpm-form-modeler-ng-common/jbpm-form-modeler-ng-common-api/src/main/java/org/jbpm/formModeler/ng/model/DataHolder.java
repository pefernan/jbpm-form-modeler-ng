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

import org.jbpm.formModeler.ng.services.context.FormRenderContext;

import java.io.Serializable;
import java.util.Set;

public abstract class DataHolder implements Comparable<DataHolder>, Serializable {
    protected String uniqueId;
    protected String inputId;
    protected String outputId;
    protected String className;
    protected String renderColor;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getInputId() {
        return inputId;
    }

    public void setInputId(String inputId) {
        this.inputId = inputId;
    }

    public String getOutputId() {
        return outputId;
    }

    public void setOutputId(String outputId) {
        this.outputId = outputId;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public String getRenderColor() {
        return renderColor;
    }

    public void setRenderColor(String renderColor) {
        this.renderColor = renderColor;
    }

    public abstract String getTypeCode();

    public abstract Object createInstance(FormRenderContext context) throws Exception;

    public abstract Object readValue(Object source, String propName) throws Exception;

    public abstract void writeValue(Object destination, String propName, Object value) throws Exception;

    public abstract boolean isAssignableValue(Object value);

    public abstract boolean canHaveChildren();

    public abstract Set<DataFieldHolder> getFieldHolders();

    public abstract DataFieldHolder getDataFieldHolderById(String id);

    @Override
    public int compareTo(DataHolder holder) {
        return (holder.getUniqueId().compareTo(getUniqueId()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (!(obj instanceof DataHolder)) return false;

        DataHolder holder = (DataHolder) obj;

        return (holder.getUniqueId().equals(getUniqueId()));
    }
}
