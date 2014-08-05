package org.jbpm.formModeler.ng.process;

/*
 * Copyright 2014 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.inject.Inject;
import org.jbpm.formModeler.ng.model.DataHolder;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;
import org.jbpm.formModeler.ng.services.management.dataHolders.DataHolderBuildConfig;
import org.jbpm.formModeler.ng.services.management.dataHolders.RangedDataHolderBuilder;
import org.jbpm.services.api.DefinitionService;
import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.formModeler.ng.services.context.FormRenderContextManager;
import org.jbpm.formModeler.ng.services.management.dataHolders.DataHolderManager;
import org.jbpm.formModeler.ng.services.management.forms.FormManager;
import org.kie.workbench.common.services.shared.project.KieProject;
import org.kie.workbench.common.services.shared.project.KieProjectService;
import org.uberfire.backend.vfs.Path;

/**
 *
 * @author salaboy
 */
public class ProcessDataHolderBuilder implements RangedDataHolderBuilder {

  @Inject
  private DefinitionService bpmn2Service;
  @Inject
  private RuntimeDataService dataService;

  @Inject
  private FormRenderContextManager contextManager;

  @Inject
  private KieProjectService projectService;

  @Inject
  private FormManager formManager;

  @Inject
  private DataHolderManager dataHolderManager;

  @Override
  public Map<String, String> getHolderSources(String context) {
    FormRenderContext formRenderContext = contextManager.getFormRenderContext(context);
    Path path = (Path) formRenderContext.getAttributes().get("path");
    KieProject project = projectService.resolveProject(path);
    System.out.println("Project: " + project);
    Map<String, String> holders = new HashMap<String, String>();
    holders.put("hiring0", "hiring-start");
    holders.put("hiring1", "hiring-interview task");
    return holders;
  }

  @Override
  public String getId() {
    return "Process";
  }

  @Override
  public String getDataHolderName(Locale locale) {
    ResourceBundle bundle = ResourceBundle.getBundle("org.jbpm.formModeler.ng.dataHolders.process.messages", locale);
    return bundle.getString("dataHolder_process");
  }

  @Override
  public DataHolder buildDataHolder(DataHolderBuildConfig config) {
    FormRenderContext formRenderContext = (FormRenderContext) config.getAttribute("context");
    Path path = (Path)config.getAttribute("path");
    Map<String, String> variables = new HashMap<String, String>();
    variables.put("age", "java.lang.Integer");
    variables.put("user", "org.jbpm.formModeler.ng.renderer.backend.test.User");
    for (String key : variables.keySet()) {

      DataHolderBuildConfig holderConfig = new DataHolderBuildConfig(key, "", key, "red", variables.get(key));
      holderConfig.addAttribute("path", path);
      DataHolder holder = dataHolderManager.createDataHolderByValueType(holderConfig, new HashMap<String, Object>());

      if (holder != null) {
        formManager.addDataHolderToForm(formRenderContext.getForm(), holder);

      }
    }

    return null;
  }

  @Override
  public boolean supportsPropertyType(String type, Map<String, Object> context) {
    return false;
  }

  @Override
  public int getPriority() {
    return 3;
  }

}
