package org.jbpm.formModeler.ng.services.management.forms.impl;

import org.jbpm.formModeler.ng.services.management.forms.SelectValuesProvider;
import org.jbpm.formModeler.ng.services.management.forms.SelectValuesProviderManager;
import org.jbpm.formModeler.ng.services.management.forms.impl.selectValuesProviders.FieldTypeSelectValuesProvider;
import org.jbpm.formModeler.ng.services.management.forms.impl.selectValuesProviders.SelectValuesProviderProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
public class SelectValuesProviderManagerImpl implements SelectValuesProviderManager, Serializable {

    @Inject
    private Instance<SelectValuesProvider> providers;

    private List<SelectValuesProvider> providerList;
    private List<String> systemProviders;

    @PostConstruct
    protected void init() {
        providerList = new ArrayList<SelectValuesProvider>();
        for (SelectValuesProvider provider : providers) {
            providerList.add(provider);
        }
        systemProviders = new ArrayList<String>();
        systemProviders.add(SelectValuesProviderProvider.class.getName());
        systemProviders.add(FieldTypeSelectValuesProvider.class.getName());
    }

    @Override
    public SelectValuesProvider getRangeProviderByType(String className) {
        for (SelectValuesProvider provider : providerList) {
            if (provider.getClass().getName().equals(className)) return provider;
        }
        return null;
    }

    @Override
    public List<SelectValuesProvider> getProvidersList() {
        return providerList;
    }

    @Override
    public List<String> getSystemProviders() {
        return systemProviders;
    }
}
