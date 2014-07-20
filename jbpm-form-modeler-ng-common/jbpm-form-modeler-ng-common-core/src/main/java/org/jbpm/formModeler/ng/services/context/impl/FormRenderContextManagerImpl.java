package org.jbpm.formModeler.ng.services.context.impl;

import org.jbpm.formModeler.ng.services.context.ContextConfiguration;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;
import org.jbpm.formModeler.ng.services.context.FormRenderContextManager;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SessionScoped
public class FormRenderContextManagerImpl implements FormRenderContextManager, Serializable {

    protected Map<String, FormRenderContext> formRenderContextMap = new HashMap<String, FormRenderContext>();

    @Override
    public FormRenderContext newContext(ContextConfiguration config) {
        String uid = CTX_PREFFIX + config.getForm().getId() + "_" + System.currentTimeMillis();

        FormRenderContext ctx = new FormRenderContext(uid, config.getForm(), config.getLoadData());
        ctx.setContextForms((Map<String, Object>) config.getAttribute("forms"));

        formRenderContextMap.put(uid, ctx);
        return ctx;
    }

    @Override
    public FormRenderContext getFormRenderContext(String UID) {
        return formRenderContextMap.get(UID);
    }

    @Override
    public void removeContext(String ctxUID) {
        formRenderContextMap.remove(ctxUID);
    }

    @Override
    public void removeContext(FormRenderContext context) {
        if (context != null) {
            removeContext(context.getUID());
        }
    }

    @Override
    public void persistContext(FormRenderContext ctx) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void persistContext(String ctxUID) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
