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
package org.jbpm.formModeler.dataModeler.model;

import org.jbpm.formModeler.dataModeler.integration.DataModelerService;
import org.jbpm.formModeler.ng.model.DataFieldHolder;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;
import org.jbpm.formModeler.ng.services.management.dataHolders.builders.PojoDataHolder;
import org.kie.internal.task.api.ContentMarshallerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataModelerDataHolder extends PojoDataHolder {
    private transient Logger log = LoggerFactory.getLogger(DataModelerDataHolder.class);

    private Class holderClass;

    public DataModelerDataHolder(String holderId, String holderClass, String renderColor) {
        super(holderId, holderClass, renderColor);
    }

    public DataModelerDataHolder(String holderId, Class holderClass, String renderColor) {
        super(holderId, holderClass.getCanonicalName(), renderColor);
        this.holderClass = holderClass;
    }

    @Override
    public Object createInstance(FormRenderContext context) throws Exception {
        ContentMarshallerContext contextMarshaller = (ContentMarshallerContext) context.getAttributes().get("marshaller");
        ClassLoader classLoader = contextMarshaller.getClassloader();
        return createInstance(classLoader.loadClass(getClassName()));
    }

    @Override
    public String getTypeCode() {
        return DataModelerService.HOLDER_TYPE_DATA_MODEL;
    }

    @Override
    public DataFieldHolder getDataFieldHolderById(String fieldHolderId) {
        if (getFieldHolders() != null) {
            for (DataFieldHolder dataFieldHolder : getFieldHolders()) {
                if (dataFieldHolder.getId().equals(fieldHolderId))
                    return dataFieldHolder;
            }
        }
        return null;
    }

    @Override
    protected Class getHolderClass() throws ClassNotFoundException {
        return holderClass;
    }
}
