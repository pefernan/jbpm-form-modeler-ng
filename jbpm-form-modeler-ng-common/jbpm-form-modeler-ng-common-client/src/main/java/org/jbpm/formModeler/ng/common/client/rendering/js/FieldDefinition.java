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

    public final native boolean isRequired() /*-{
        return this.required;
    }-*/;

    public final native boolean isReadOnly() /*-{
        return this.readonly;
    }-*/;

    public final native String getBindingExpression() /*-{
        return this.bindingExpression;
    }-*/;

    public final native JavaScriptObject getData() /*-{
        return this.data;
    }-*/;
}
