package org.jbpm.formModeler.ng.common.client.renderer;

import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.jbpm.formModeler.ng.common.client.renderer.checkers.FieldCheckResult;
import org.jbpm.formModeler.ng.common.client.renderer.checkers.FieldValueChecker;
import org.jbpm.formModeler.ng.common.client.rendering.js.FieldDefinition;
import org.jbpm.formModeler.ng.common.client.rendering.js.FormContext;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Dependent
public class FieldValueCheckerManager {
    @Inject
    protected SyncBeanManager iocManager;

    protected List<FieldValueChecker> valueCheckers;

    @PostConstruct
    private void init() {
        Collection<IOCBeanDef<FieldValueChecker>> checkers = iocManager.lookupBeans(FieldValueChecker.class);

        valueCheckers = new ArrayList<FieldValueChecker>();
        if (checkers != null) {
            for (IOCBeanDef checkerDef : checkers) {
                FieldValueChecker checker = (FieldValueChecker) checkerDef.getInstance();
                valueCheckers.add(checker);
            }
        }
    }

    public FieldCheckResult checkFieldValue (FieldDefinition field, String value, FormContext context) {
        for (FieldValueChecker checker : valueCheckers) {
            FieldCheckResult result = checker.checkFieldValue(field, value, context);
            if (result.isWrong()) return result;
        }

        return new FieldCheckResult(false, null);
    }
}
