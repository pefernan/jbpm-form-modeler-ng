package org.jbpm.formModeler.ng.common.client.rendering;

import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.jbpm.formModeler.ng.common.client.rendering.fields.FieldRenderer;
import org.jbpm.formModeler.ng.common.client.rendering.fields.TextBoxFieldRenderer;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class FieldProviderManager {
    @Inject
    private SyncBeanManager iocManager;

    @Inject
    private HashMap<String, FieldRenderer> providersMap;

    private List<FieldRenderer> visibleRenderers = new ArrayList<FieldRenderer>();

    @Inject
    private TextBoxFieldRenderer defaultProvider;

    @PostConstruct
    private void init() {
        Collection<IOCBeanDef<FieldRenderer>> providers = iocManager.lookupBeans(FieldRenderer.class);
        if (providers != null) {
            for (IOCBeanDef providerDef : providers) {
                FieldRenderer renderer = (FieldRenderer) providerDef.getInstance();
                if (renderer.isVisible()) visibleRenderers.add(renderer);
                providersMap.put(renderer.getCode(), renderer);
            }
        }
    }

    public FieldRenderer getProviderByType(String type) {
        FieldRenderer result = providersMap.get(type);
        if (result == null) return defaultProvider;
        return result;
    }

    public List<FieldRenderer> getVisibleRenderers() {
        return visibleRenderers;
    }
}
