package org.jbpm.formModeler.ng.services.management.forms.impl;

import org.apache.commons.lang.SerializationUtils;
import org.jbpm.formModeler.ng.model.Layout;
import org.jbpm.formModeler.ng.model.impl.layouts.DefaultLayout;
import org.jbpm.formModeler.ng.services.management.forms.FormLayoutManager;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FormLayoutManagerImpl implements FormLayoutManager {
    @Inject
    private Instance<Layout> installedLayouts;

    private List<Layout> availableLayouts;

    @Inject
    private DefaultLayout defaultLayout;

    @PostConstruct
    protected void init() {
        availableLayouts = new ArrayList<Layout>();
        for (Layout layout : installedLayouts) {
            availableLayouts.add(layout);
        }
    }

    @Override
    public Layout getLayout(String code) {
        for (Layout layout : availableLayouts) {
            if (layout.getCode().equals(code)) return (Layout) SerializationUtils.clone(layout);
        }
        return (Layout) SerializationUtils.clone(defaultLayout);
    }

    @Override
    public List<Layout> getAvailableLayouts() {
        return availableLayouts;
    }

    @Override
    public DefaultLayout getDefaultLayout() {
        return (DefaultLayout) SerializationUtils.clone(defaultLayout);
    }
}
