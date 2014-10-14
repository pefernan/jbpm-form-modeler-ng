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


import org.jbpm.formModeler.ng.model.DataHolder;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class DataHolderManagerImpl implements DataHolderManager {

    @Inject
    private Instance<DataHolderBuilder> holderBuilders;
    private Set<DataHolderBuilder> builders;

    @PostConstruct
    protected void initializeHolders() {

        builders = new TreeSet<DataHolderBuilder>(new Comparator<DataHolderBuilder>() {
            @Override
            public int compare(DataHolderBuilder o1, DataHolderBuilder o2) {
                return o1.getPriority() - o2.getPriority();
            }
        });

        for (DataHolderBuilder holderBuilder : holderBuilders) {
            builders.add(holderBuilder);
        }
    }

    @Override
    public DataHolderBuilder getBuilderByHolderValueType(String valueType, Map<String, Object> context) {
        for (DataHolderBuilder builder : builders) {
            if (builder.supportsPropertyType(valueType, context)) return builder;
        }
        return null;
    }

    @Override
    public DataHolderBuilder getBuilderByBuilderType(String builderId) {
        for (DataHolderBuilder builder : builders) {
            if (builder.getId().equals(builderId)) return builder;
        }
        return null;
    }

    @Override
    public DataHolder createDataHolderByValueType(DataHolderBuildConfig config, Map<String, Object> context) {
        DataHolderBuilder builder = getBuilderByHolderValueType(config.getClassName(), context);
        if (builder == null) return null;

        return builder.buildDataHolder(config);
    }

    @Override
    public DataHolder createDataHolderByType(String type, DataHolderBuildConfig config) {
        DataHolderBuilder builder = getBuilderByBuilderType(type);
        if (builder == null) return null;

        return builder.buildDataHolder(config);
    }

    public Set<DataHolderBuilder> getHolderBuilders() {
        return builders;
    }
}
