package org.jbpm.formModeler.ng.services.management.dataHolders;

import org.jbpm.formModeler.ng.model.DataHolder;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface DataHolderManager extends Serializable {

    Map<String, String> getHolderColors();

    Set<DataHolderBuilder> getHolderBuilders();

    DataHolderBuilder getBuilderByBuilderType(String builderId);

    DataHolder createDataHolderByType(String type, DataHolderBuildConfig config);

    DataHolderBuilder getBuilderByHolderValueType(String valueType, Map<String, Object> context);

    DataHolder createDataHolderByValueType(DataHolderBuildConfig config, Map<String, Object> context);
}
