package org.jbpm.formModeler.ng.model;


import java.io.Serializable;
import java.util.List;

public interface LayoutArea extends Serializable {
    String getLabel();
    void setLabel(String label);
    public List<Long> getElementIds();
    public void addElement(FormElement element);
    public boolean removeElement(Long elementId);

    void addElement(Long elementId);
    boolean containsElement(Long elementId);

    boolean isEmpty();

    void addElement(int index, FormElement element);
}
