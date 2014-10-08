package org.jbpm.formModeler.ng.model;

import java.io.Serializable;
import java.util.*;

public class Form implements Serializable, Comparable {

    public static final String LABEL_MODE_HIDDEN = "hidden";

    private Long id;

    private String subject;

    private String name;

    private String displayMode = "default";

    private String labelMode = "before";

    private String showMode;

    private Layout layout;

    private List<FormElement> elements = new ArrayList<FormElement>();

    private Set<DataHolder> holders = new TreeSet<DataHolder>();

    private String serializedStatus;


    public Form() {
    }

    public Form(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addDataHolder(DataHolder holder) {
        if (holder == null || holder.getUniqueId() == null || "".equals(holder.getUniqueId().trim())) return;

        if (getDataHolderById(holder.getUniqueId()) != null) {
            holders.remove(holder);
        }
        holders.add(holder);
    }

    public void removeDataHolder(String id) {
        if (id == null || id.trim().length() == 0) return;
        DataHolder holder = getDataHolderById(id);
        if (holder != null) {
            holders.remove(holder);
        }

    }

    public DataHolder getDataHolderById(String srcId) {
        if (srcId == null || srcId.trim().length() == 0) return null;
        if (getHolders() != null) {
            for (DataHolder dataHolder : holders) {
                if (srcId.equals(dataHolder.getUniqueId()))
                    return dataHolder;
            }
        }
        return null;
    }


    public String toString() {
        return "Form [" + getName() + "]";
    }

    public boolean equals(Object other) {
        if (!(other instanceof Form)) return false;
        Form castOther = (Form) other;
        return this.getId().equals(castOther.getId());
    }

    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * Get field by name
     *
     * @param name Desired field name, must be not null
     * @return field by given name or null if it doesn't exist.
     */
    public Field getField(String name) {
        if (name == null || name.trim().length() == 0) return null;
        for (FormElement element : elements) {
            if (name.equals(element.getName())) return (Field) element;
        }
        return null;
    }


    public Field getFieldById(Long id) {
        if (id == null || id < 0) return null;
        for (FormElement element : elements) {
            if (id.equals(element.getId())) return (Field) element;
        }
        return null;
    }

    public Set<DataHolder> getHolders() {
        return holders;
    }

    @Override
    public int compareTo(Object o) {
        return id.compareTo(((Form) o).getId());
    }

    public boolean containsHolder(DataHolder aholder) {
        for (DataHolder holder : holders) {
            if (holder.equals(aholder)) return true;
        }
        return false;
    }

    public boolean addField(Field field) {
        return addField(field, false);
    }

    public boolean addField(Field field, boolean groupWithPrevious) {
        field.setForm(this);
        return elements.add(field);
    }

    public Field deleteField(Long fieldId) {
        if (fieldId != null) {
            for (FormElement element : elements) {
                if (element.getId().equals(fieldId)) {
                    elements.remove(element);
                    layout.removeElement(fieldId);
                    return (Field) element;
                }
            }
        }
        return null;
    }

    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayMode() {
        return this.displayMode;
    }

    public void setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
    }

    public String getLabelMode() {
        return labelMode;
    }

    public void setLabelMode(String labelMode) {
        this.labelMode = labelMode;
    }

    public String getShowMode() {
        return showMode;
    }

    public void setShowMode(String showMode) {
        this.showMode = showMode;
    }

    public List<FormElement> getElements() {
        return elements;
    }

    public String getSerializedStatus() {
        return serializedStatus;
    }

    public void setSerializedStatus(String serializedStatus) {
        this.serializedStatus = serializedStatus;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }
}
