package org.jbpm.formModeler.ng.common.client.rendering;

import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.jbpm.formModeler.ng.common.client.rendering.layouts.DefaultFormLayoutRenderer;
import org.jbpm.formModeler.ng.common.client.rendering.layouts.FormLayoutRenderer;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;

@Dependent
public class FormRendererManager {
    @Inject
    protected SyncBeanManager iocManager;

    @Inject
    protected HashMap<String, FormLayoutRenderer> layoutRenderersMap;

    @Inject
    private DefaultFormLayoutRenderer defaultLayoutRenderer;

    @PostConstruct
    private void init() {
        Collection<IOCBeanDef<FormLayoutRenderer>> layouts = iocManager.lookupBeans(FormLayoutRenderer.class);
        if (layouts != null) {
            for (IOCBeanDef layoutDef : layouts) {
                FormLayoutRenderer layoutRenderer = (FormLayoutRenderer) layoutDef.getInstance();
                layoutRenderersMap.put(layoutRenderer.getCode(), layoutRenderer);
            }
        }
    }

    public FormLayoutRenderer getLayoutRendererByType(String type) {
        FormLayoutRenderer result = layoutRenderersMap.get(type);
        if (result == null) return defaultLayoutRenderer;
        return result;
    }
}
