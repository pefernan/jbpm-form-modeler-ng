package org.jbpm.formModeler.ng.model.impl.layouts;

import org.jbpm.formModeler.ng.model.FormElement;
import org.jbpm.formModeler.ng.model.Layout;
import org.jbpm.formModeler.ng.model.LayoutArea;

import javax.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Dependent
public class ColumnLayout implements Layout {
    int columns = 1;
    private List<LayoutArea> areas = new ArrayList<LayoutArea>();

    @Override
    public String getCode() {
        return "column";
    }

    @Override
    public String getName(Locale locale) {
        return "column";
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    @Override
    public List<LayoutArea> getAreas() {
        return areas;
    }

    @Override
    public void addElement(FormElement element) {
        DefaultLayoutArea area;
        if (areas.isEmpty()) area = new DefaultLayoutArea();
        else area = (DefaultLayoutArea) areas.get(0);
        area.addElement(element);
    }

    @Override
    public void addElement(int column, int position, Long fieldId) {
        if (column > columns) return;
        DefaultLayoutArea area;
        if (column >= areas.size()) {
            area = new DefaultLayoutArea();
            areas.add(position, area);
        } else {
            area = (DefaultLayoutArea) areas.get(position);
        }
        area.addElement(fieldId);
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
