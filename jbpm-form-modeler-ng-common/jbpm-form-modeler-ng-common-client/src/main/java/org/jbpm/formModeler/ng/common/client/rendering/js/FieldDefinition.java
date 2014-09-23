package org.jbpm.formModeler.ng.common.client.rendering.js;

import com.google.gwt.core.client.JavaScriptObject;

public class FieldDefinition extends JavaScriptObject {
    protected FieldDefinition() {
    }

    public final native String getUid() /*-{
        return this.uid;
    }-*/;

    public final native String getId() /*-{
        return this.id;
    }-*/;

    public final native JavaScriptObject getLabel() /*-{
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

    public final native JavaScriptObject getData() /*-{
        return this.data;
    }-*/;
}
