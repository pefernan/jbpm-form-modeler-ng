package org.jbpm.formModeler.ng.renderer.client.rendering;

import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.jbpm.formModeler.ng.renderer.client.rendering.fields.FieldProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;

@ApplicationScoped
public class FieldProviderManager {
    @Inject
    private SyncBeanManager iocManager;

    @Inject
    private HashMap<String, FieldProvider> providersMap;

    @Inject
    private org.jbpm.formModeler.ng.renderer.client.rendering.fields.TextBoxFieldProvider defaultProvider;

    @PostConstruct
    private void init() {
        Collection<IOCBeanDef<FieldProvider>> providers = iocManager.lookupBeans(FieldProvider.class);
        if (providers != null) {
            for (IOCBeanDef providerDef : providers) {
                FieldProvider provider = (FieldProvider) providerDef.getInstance();
                providersMap.put(provider.getCode(), provider);
            }
        }
    }

    public FieldProvider getProviderByType(String type) {
        FieldProvider result = providersMap.get(type);
        if (result == null) return defaultProvider;
        return result;
    }
}
