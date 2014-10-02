package org.jbpm.formModeler.ng.services.management.forms;

import org.jbpm.formModeler.ng.model.Layout;

import java.util.List;

public interface FormLayoutManager {
    public Layout getLayout(String code);

    List<Layout> getAvailableLayouts();

    Layout getDefaultLayout();
}
