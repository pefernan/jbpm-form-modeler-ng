package org.jbpm.formModeler.ng.common.client.rendering;

import com.google.gwt.core.client.JavaScriptObject;

public class FieldDescription extends JavaScriptObject {
    protected FieldDescription() {
    }

    public final native String getUid() /*-{
        return this.uid;
    }-*/;

    public final native String getId() /*-{
        return this.id;
    }-*/;

    public final native String getLabel() /*-{
        return this.label;
    }-*/;

    public final native String getType() /*-{
        return this.type;
    }-*/;

    public final native int getRow() /*-{
        return this.row;
    }-*/;

    public final native int getColumn() /*-{
        return this.column;
    }-*/;

    public final native String getHolderColor() /*-{
        return this.holderColor;
    }-*/;

    public final native boolean isRequired() /*-{
        return this.required;
    }-*/;

    public final native boolean isGrouped() /*-{
        return this.grouped;
    }-*/;

    public final native String getValue() /*-{
        return this.value;
    }-*/;

    public final native void setValue(String value) /*-{
        this.value = value;
    }-*/;

    public final native JavaScriptObject getOptions() /*-{
        return this.options;
    }-*/;
}
