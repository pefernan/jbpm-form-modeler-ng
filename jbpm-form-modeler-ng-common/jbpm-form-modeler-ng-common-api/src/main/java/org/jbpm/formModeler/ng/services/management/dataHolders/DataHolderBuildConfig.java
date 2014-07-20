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
package org.jbpm.formModeler.ng.services.management.dataHolders;

import java.util.HashMap;
import java.util.Map;

public class DataHolderBuildConfig {
    private String holderId;
    private String inputId;
    private String outputId;
    private String renderColor;
    private String className;

    private Map<String, Object> attributes = new HashMap<String, Object>();

    public DataHolderBuildConfig(String holderId, String inputId, String outputId, String renderColor, String className) {
        this.holderId = holderId;
        this.inputId = inputId;
        this.outputId = outputId;
        this.renderColor = renderColor;
        this.className = className;
    }

    public String getHolderId() {
        return holderId;
    }

    public void setHolderId(String holderId) {
        this.holderId = holderId;
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

    public String getRenderColor() {
        return renderColor;
    }

    public void setRenderColor(String renderColor) {
        this.renderColor = renderColor;
    }

    public void addAttribute(String attribute, Object value) {
        this.attributes.put(attribute, value);
    }

    public Object getAttribute(String attribute) {
        return this.attributes.get(attribute);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
