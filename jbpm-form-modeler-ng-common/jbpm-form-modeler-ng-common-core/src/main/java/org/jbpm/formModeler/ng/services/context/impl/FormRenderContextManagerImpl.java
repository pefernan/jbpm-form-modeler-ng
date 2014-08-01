package org.jbpm.formModeler.ng.services.context.impl;

import org.jbpm.formModeler.ng.services.context.ContextConfiguration;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;
import org.jbpm.formModeler.ng.services.context.FormRenderContextManager;
import org.jbpm.formModeler.ng.services.context.FormRenderContextMarshaller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SessionScoped
public class FormRenderContextManagerImpl implements FormRenderContextManager, Serializable {

    @Inject
    private FormRenderContextMarshaller contextMarshaller;

    protected Map<String, FormRenderContext> formRenderContextMap = new HashMap<String, FormRenderContext>();


    @Override
    public FormRenderContext newContext(ContextConfiguration config) {
        String uid = CTX_PREFFIX + config.getForm().getId() + "_" + System.currentTimeMillis();

        FormRenderContext ctx = new FormRenderContext(uid, config.getForm(), config.getInputData(), config.getOutputData(), config.getLocale());
        ctx.setContextForms(config.getContextForms());
        ctx.getAttributes().putAll(config.getAttributes());
        marshallContext(ctx);
        formRenderContextMap.put(uid, ctx);
        return ctx;
    }

    @Override
    public String marshallContext(FormRenderContext context) {
        if (context == null) return null;

        String marshalledContext = contextMarshaller.marshallContext(context);
        context.setMarshalledCopy(marshalledContext);

        return marshalledContext;
    }

    @Override
    public FormRenderContext getFormRenderContext(String UID) {
        return formRenderContextMap.get(UID);
    }

    @Override
    public void removeContext(String ctxUID) {
        removeContext(formRenderContextMap.get(ctxUID));
    }

    @Override
    public void removeContext(FormRenderContext context) {
        if (context != null) {
            formRenderContextMap.remove(context.getUID());
            if (context.getAttributes() != null) context.getAttributes().clear();
            if (context.getContextForms() != null) context.getContextForms().clear();
            if (context.getInputData() != null) context.getInputData().clear();
            if (context.getOutputData() != null) context.getOutputData().clear();
        }
    }

    @Override
    public void persistContext(FormRenderContext ctx, String ctxJson) throws Exception {
        if (ctx != null) contextMarshaller.unmarshallContext(ctx, ctxJson);
    }

    @Override
    public void persistContext(String ctxUID, String ctxJson) throws Exception {
        persistContext(formRenderContextMap.get(ctxUID), ctxJson);
    }
}
