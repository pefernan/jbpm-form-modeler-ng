package org.jbpm.formModeler.ng.renderer.client.rendering;

import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.jbpm.formModeler.ng.renderer.client.rendering.renderers.DefaultFormRenderer;
import org.jbpm.formModeler.ng.renderer.client.rendering.renderers.FormRenderer;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;

@ApplicationScoped
public class FormRendererManager {
    @Inject
    private SyncBeanManager iocManager;

    @Inject
    private HashMap<String, FormRenderer> renderersMap;

    @Inject
    private DefaultFormRenderer defaultrenderer;

    @PostConstruct
    private void init() {
        Collection<IOCBeanDef<FormRenderer>> renderers = iocManager.lookupBeans(FormRenderer.class);
        if (renderers != null) {
            for (IOCBeanDef rendererDef : renderers) {
                FormRenderer renderer = (FormRenderer) rendererDef.getInstance();
                renderersMap.put(renderer.getCode(), renderer);
            }
        }
    }

    public FormRenderer getRendererByType(String type) {
        FormRenderer result = renderersMap.get(type);
        if (result == null) return defaultrenderer;
        return result;
    }
}
