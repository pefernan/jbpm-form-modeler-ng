package org.jbpm.formModeler.ng.model.impl.layouts;

import org.jbpm.formModeler.ng.model.FormElement;
import org.jbpm.formModeler.ng.model.LayoutArea;

import java.util.ArrayList;
import java.util.List;

public class DefaultLayoutArea implements LayoutArea {
    private List<Long> elementIds = new ArrayList<Long>();

    public DefaultLayoutArea() {
    }

    @Override
    public List<Long> getElementIds() {
        return elementIds;
    }

    @Override
    public void addElement(FormElement element) {
        if (element != null) addElement(element.getId());
    }

    @Override
    public void addElement(Long elementId) {
        if (elementIds.contains(elementId)) elementIds.remove(elementId);
        elementIds.add(elementId);
    }

    @Override
    public void addElement(int index, FormElement element) {
        if (element != null) addElement(index, element.getId());
    }

    public void addElement(int index, Long elementId) {
        if (elementIds.contains(elementId)) elementIds.remove(elementId);
        if (index > elementIds.size()) elementIds.add(elementId);
        else {
            if (index == elementIds.size()) index --;
            elementIds.add(index, elementId);
        }
    }

    @Override
    public boolean removeElement(Long elementId) {
        return elementIds.remove(elementId);
    }

    @Override
    public boolean containsElement(Long elementId) {
        return elementIds.contains(elementId);
    }

    @Override
    public boolean isEmpty() {
        return elementIds.isEmpty();
    }
}
