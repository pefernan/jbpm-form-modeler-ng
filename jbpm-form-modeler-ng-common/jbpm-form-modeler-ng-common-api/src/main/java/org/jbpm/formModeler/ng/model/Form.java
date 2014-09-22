package org.jbpm.formModeler.ng.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

public class Form implements Serializable, Comparable {
    public static final String RENDER_MODE_FORM = "form";
    public static final String RENDER_MODE_DISPLAY = "display";
    public static final String RENDER_MODE_TEMPLATE_EDIT = "templateEdit";
    public static final String RENDER_MODE_WYSIWYG_FORM = "wysiwyg-form";
    public static final String RENDER_MODE_WYSIWYG_DISPLAY = "wysiwyg-display";

    public static final String LABEL_MODE_UNDEFINED = "undefined";
    public static final String LABEL_MODE_BEFORE = "before";
    public static final String LABEL_MODE_AFTER = "after";
    public static final String LABEL_MODE_RIGHT = "right";
    public static final String LABEL_MODE_LEFT = "left";
    public static final String LABEL_MODE_HIDDEN = "hidden";
    public static final String DISPLAY_MODE_DEFAULT = "default";
    public static final String DISPLAY_MODE_ALIGNED = "aligned";
    public static final String DISPLAY_MODE_TEMPLATE = "template";
    public static final String DISPLAY_MODE_NONE = "none";
    public static final String TEMPLATE_FIELD = "$field";
    public static final String TEMPLATE_LABEL = "$label";

    private Long id;

    private String subject;

    private String name;

    private String displayMode = DISPLAY_MODE_DEFAULT;

    private String labelMode = LABEL_MODE_BEFORE;

    private String showMode;

    private LinkedList<LinkedList<Field>> elementsGrid = new LinkedList<LinkedList<Field>>();

    private int fieldsCount = 0;

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
        if (!elementsGrid.isEmpty()) {
            for (LinkedList<Field> fields : elementsGrid) {
                for (Field field : fields) {
                    if (name.equals(field.getName()))
                        return field;
                }
            }
        }
        return null;
    }


    public Field getFieldById(Long id) {
        if (id == null || id < 0) return null;
        if (!elementsGrid.isEmpty()) {
            for (LinkedList<Field> fields : elementsGrid) {
                for (Field field : fields) {
                    if (id.equals(field.getId()))
                        return field;
                }
            }
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
        LinkedList<Field> fieldRow;
        if (elementsGrid.size() == 0 || !field.getGroupWithPrevious()) {
            fieldRow = new LinkedList<Field>();
            elementsGrid.add(fieldRow);
        } else {
            fieldRow = elementsGrid.getLast();
        }
        field.setForm(this);
        field.setPosition(fieldsCount);
        fieldsCount ++;
        return fieldRow.add(field);
    }

    public Field deleteField(Long fieldId) {
        if (fieldId != null) {
            for (LinkedList<Field> fields : elementsGrid) {
                for (Field field : fields) {
                    if (fieldId.equals(field.getId())) {
                        fields.remove(field);
                        if (fields.isEmpty()) elementsGrid.remove(fields);
                        if (fields.size() == 1) fields.getFirst().setGroupWithPrevious(false);
                        return field;
                    }
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

    public LinkedList<LinkedList<Field>> getElementsGrid() {
        return elementsGrid;
    }

    public int getFieldsCount() {
        int fieldsCount = 0;

        for (LinkedList<Field> fields : elementsGrid) {
            fieldsCount += fields.size();
        }

        return fieldsCount;
    }

    public String getSerializedStatus() {
        return serializedStatus;
    }

    public void setSerializedStatus(String serializedStatus) {
        this.serializedStatus = serializedStatus;
    }
}
