package org.jbpm.formModeler.ng.common.client.rendering;

import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.jbpm.formModeler.ng.common.client.rendering.fields.FieldRenderer;
import org.jbpm.formModeler.ng.common.client.rendering.fields.TextBoxFieldRenderer;

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
    private HashMap<String, FieldRenderer> providersMap;

    @Inject
    private TextBoxFieldRenderer defaultProvider;

    @PostConstruct
    private void init() {
        Collection<IOCBeanDef<FieldRenderer>> providers = iocManager.lookupBeans(FieldRenderer.class);
        if (providers != null) {
            for (IOCBeanDef providerDef : providers) {
                FieldRenderer provider = (FieldRenderer) providerDef.getInstance();
                providersMap.put(provider.getCode(), provider);
            }
        }
    }

    public FieldRenderer getProviderByType(String type) {
        FieldRenderer result = providersMap.get(type);
        if (result == null) return defaultProvider;
        return result;
    }
}
