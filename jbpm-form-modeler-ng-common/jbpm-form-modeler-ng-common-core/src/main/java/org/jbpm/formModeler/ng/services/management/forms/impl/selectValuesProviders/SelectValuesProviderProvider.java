package org.jbpm.formModeler.ng.services.management.forms.impl.selectValuesProviders;

import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;
import org.jbpm.formModeler.ng.services.management.forms.SelectValuesProvider;
import org.jbpm.formModeler.ng.services.management.forms.SelectValuesProviderManager;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Dependent
public class SelectValuesProviderProvider implements SelectValuesProvider {
    @Inject
    SelectValuesProviderManager providerManager;

    @PostConstruct

    @Override
    public String getName() {
        return "Select Values Providers";
    }

    @Override
    public Map<String, String> getSelectOptions(Field field, FormRenderContext renderContext) {
        Map<String, String> result = new HashMap<String, String>();

        for (SelectValuesProvider provider : providerManager.getProvidersList()) {
            if (providerManager.getSystemProviders().contains(provider.getClass().getName())) continue;
            result.put(provider.getClass().getName(), provider.getName());
        }

        return result;
    }
}
