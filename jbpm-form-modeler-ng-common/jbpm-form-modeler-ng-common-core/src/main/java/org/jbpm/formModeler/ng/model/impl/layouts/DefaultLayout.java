package org.jbpm.formModeler.ng.model.impl.layouts;


import org.jbpm.formModeler.ng.model.FormElement;
import org.jbpm.formModeler.ng.model.Layout;
import org.jbpm.formModeler.ng.model.LayoutArea;

import javax.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Dependent
public class DefaultLayout implements Layout {
    protected List<LayoutArea> areas = new ArrayList<LayoutArea>();

    @Override
    public String getCode() {
        return "default";
    }

    @Override
    public String getName(Locale locale) {
        return "default";
    }

    @Override
    public List<LayoutArea> getAreas() {
        return areas;
    }

    @Override
    public void addElement(FormElement element) {
        DefaultLayoutArea area = new DefaultLayoutArea();
        area.addElement(element);
        areas.add(area);
    }

    @Override
    public void addElement(int row, int column, Long fieldId) {
        DefaultLayoutArea area;
        if (column == -1 || row == areas.size()) {
            area = new DefaultLayoutArea();
            areas.add(row, area);
            area.addElement(fieldId);
        } else {
            area = (DefaultLayoutArea) areas.get(row);
            area.addElement(column, fieldId);
        }

    }

    @Override
    public void removeElement(Long fieldId) {
        for (LayoutArea area : areas) {
            if (area.containsElement(fieldId)) {
                area.removeElement(fieldId);
                if (area.isEmpty()) areas.remove(area);
                return;
            }
        }
    }
}
