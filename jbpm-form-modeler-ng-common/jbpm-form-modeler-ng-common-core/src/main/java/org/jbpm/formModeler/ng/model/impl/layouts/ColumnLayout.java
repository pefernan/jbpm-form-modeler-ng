package org.jbpm.formModeler.ng.model.impl.layouts;

import org.jbpm.formModeler.ng.model.FormElement;
import org.jbpm.formModeler.ng.model.Layout;
import org.jbpm.formModeler.ng.model.LayoutArea;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Dependent
public class ColumnLayout implements Layout {
    private List<LayoutArea> areas = new ArrayList<LayoutArea>();

    @Override
    public String getCode() {
        return "columns";
    }

    @Override
    public String getName(Locale locale) {
        return "columns";
    }

    @Override
    public List<LayoutArea> getAreas() {
        return areas;
    }

    @Override
    public void addElement(FormElement element) {
        if (element != null) addElement(element.getId());
    }

    @Override
    public void addElement(Long elementId) {
        DefaultLayoutArea area;
        if (areas.isEmpty()) {
            area = new DefaultLayoutArea();
            areas.add(area);
        }
        else area = (DefaultLayoutArea) areas.get(0);
        area.addElement(elementId);
    }

    @Override
    public void addElement(int column, int position, Long fieldId) {
        if (column >= areas.size()) {
            DefaultLayoutArea area = new DefaultLayoutArea();
            areas.add(column, area);
            area.addElement(fieldId);
        } else {
            DefaultLayoutArea area = (DefaultLayoutArea) areas.get(column);
            if(area.isEmpty()) area.addElement(fieldId);
            else area.addElement(position, fieldId);
        }
    }

    @Override
    public void removeElement(Long fieldId) {
        for (LayoutArea area : areas) {
            if (area.containsElement(fieldId)) {
                area.removeElement(fieldId);
                return;
            }
        }
    }
}
