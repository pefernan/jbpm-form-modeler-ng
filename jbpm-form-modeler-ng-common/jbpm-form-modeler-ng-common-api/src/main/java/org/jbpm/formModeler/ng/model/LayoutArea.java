package org.jbpm.formModeler.ng.model;


import java.util.List;

public interface LayoutArea {
    public List<Long> getElementIds();
    public void addElement(FormElement element);
    public boolean removeElement(Long elementId);

    void addElement(Long elementId);
    boolean containsElement(Long elementId);

    boolean isEmpty();

    void addElement(int index, FormElement element);
}
