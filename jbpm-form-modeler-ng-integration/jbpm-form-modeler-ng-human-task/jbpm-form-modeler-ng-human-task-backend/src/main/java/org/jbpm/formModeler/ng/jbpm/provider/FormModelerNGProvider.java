package org.jbpm.formModeler.ng.jbpm.provider;

import org.apache.commons.io.IOUtils;
import org.jbpm.console.ng.bd.service.DataServiceEntryPoint;
import org.jbpm.formModeler.ng.services.context.ContextConfiguration;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;
import org.jbpm.formModeler.ng.services.context.FormRenderContextManager;
import org.jbpm.formModeler.ng.services.context.FormRenderContextMarshaller;
import org.jbpm.kie.services.impl.form.FormProvider;
import org.jbpm.kie.services.impl.model.ProcessAssetDesc;
import org.jbpm.services.api.model.ProcessDefinition;
import org.kie.api.task.model.Task;
import org.kie.internal.task.api.model.InternalTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Dependent
public class FormModelerNGProvider implements FormProvider {
    private static Logger logger = LoggerFactory.getLogger(FormModelerNGProvider.class);

    @Inject
    private FormRenderContextManager contextManager;

    @Inject
    private FormRenderContextMarshaller contextMarshaller;

    @Inject
    private DataServiceEntryPoint dataServiceEntryPoint;

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String render(String name, ProcessDefinition process, Map<String, Object> renderContext) {
        InputStream template = null;
        if (((ProcessAssetDesc)process).getForms().containsKey(process.getId())) {
            template = new ByteArrayInputStream(((ProcessAssetDesc)process).getForms().get(process.getId()).getBytes());
        } else if (((ProcessAssetDesc)process).getForms().containsKey(process.getId() + "-taskform.form2")) {
            template = new ByteArrayInputStream(((ProcessAssetDesc)process).getForms().get(process.getId() + "-taskform.form2").getBytes());
        }

        if (template == null) return null;

        try {
            String formDefinition = IOUtils.toString(template, "UTF-8");
            ContextConfiguration configuration = new ContextConfiguration(formDefinition, new HashMap<String, Object>());
            FormRenderContext context = contextManager.newContext(configuration);
            context.getAttributes().put("marshallerContext", renderContext.get("marshallerContext"));
            context.getAttributes().put("forms", buildContextForms(process));

            return contextMarshaller.marshallContext(context);
        } catch (IOException e) {
            logger.warn("Error rendering form for process '{}': {}", process.getName(), e);
        }

        return null;
    }

    @Override
    public String render(String name, Task task, ProcessDefinition process, Map<String, Object> renderContext) {
        Map<String,List<Map<String,String>>> outputs = dataServiceEntryPoint.getTaskOutputAssignments(((ProcessAssetDesc) process).getDeploymentId(), process.getId(), task.getName());
        Map<String, List<Map<String, String>>> inputs = dataServiceEntryPoint.getTaskInputAssignments(((ProcessAssetDesc) process).getDeploymentId(), process.getId(), task.getName());
        InputStream template = null;
        if(task != null && process != null){
            String lookupName = "";
            String formName = ((InternalTask)task).getFormName();
            if(formName != null && !formName.equals("")){
                lookupName = formName;
            }else{
                lookupName = task.getNames().get(0).getText();
            }
            if (((ProcessAssetDesc)process).getForms().containsKey(lookupName)) {
                template = new ByteArrayInputStream(((ProcessAssetDesc)process).getForms().get(lookupName).getBytes());
            } else if (((ProcessAssetDesc)process).getForms().containsKey(lookupName.replace(" ", "")+ "-taskform.form2")) {
                template = new ByteArrayInputStream(((ProcessAssetDesc)process).getForms().get(lookupName.replace(" ", "") + "-taskform.form2").getBytes());
            }
        }

        if (template == null) return null;

        try {
            String formDefinition = IOUtils.toString(template, "UTF-8");
            Map<String, Object> formMappings = new HashMap<String, Object>();

            ContextConfiguration configuration = new ContextConfiguration(formDefinition, formMappings);
            FormRenderContext context = contextManager.newContext(configuration);
            context.getAttributes().put("marshallerContext", renderContext.get("marshallerContext"));
            context.getAttributes().put("forms", buildContextForms(process));
            String taskStatus = task.getTaskData().getStatus().name();
            context.setReadonly(!"InProgress".equals(taskStatus));
            return contextMarshaller.marshallContext(context);
        } catch (IOException e) {
            logger.warn("Error rendering form for process '{}': {}", process.getName(), e);
        }

        return null;
    }


    protected Map<String, String> buildContextForms(ProcessDefinition process) {
        Map<String, String> forms = ((ProcessAssetDesc)process).getForms();

        Map<String, String> ctxForms = new HashMap<String, String>();

        for (Iterator it = forms.keySet().iterator(); it.hasNext();) {
            String key = (String) it.next();
            if (!key.endsWith(".form2")) continue;
            String value = forms.get(key);
            ctxForms.put(key, value);
        }
        return ctxForms;
    }
}
