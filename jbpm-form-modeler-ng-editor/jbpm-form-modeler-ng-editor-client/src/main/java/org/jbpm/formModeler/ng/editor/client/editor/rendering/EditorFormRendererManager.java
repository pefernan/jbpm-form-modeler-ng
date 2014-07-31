package org.jbpm.formModeler.ng.editor.client.editor.rendering;

import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.jbpm.formModeler.ng.common.client.rendering.FormRendererManager;
import org.jbpm.formModeler.ng.common.client.rendering.renderers.DefaultFormRenderer;
import org.jbpm.formModeler.ng.common.client.rendering.renderers.FormRenderer;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
@ApplicationScoped
@Editor
public class EditorFormRendererManager extends FormRendererManager{

    @Inject @Editor
    private DefaultFormRenderer defaultrenderer;

    @PostConstruct
    private void init() {
        Collection<IOCBeanDef<FormRenderer>> renderers = iocManager.lookupBeans(FormRenderer.class, new AnnotationWrapper(Editor.class) {});
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
