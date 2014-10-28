package org.jbpm.formModeler.ng.common.client.rendering.js;

import com.google.gwt.core.client.JavaScriptObject;

public class FieldDefinition extends JavaScriptObject {
    protected FieldDefinition() {
    }

    public final native String getId() /*-{
        return this.id;
    }-*/;

    public final native String getName() /*-{
        return this.name;
    }-*/;

    public final native String getLabel() /*-{
        return this.label;
    }-*/;

    public final native String getCode() /*-{
        return this.code;
    }-*/;

    public final native boolean isRequired() /*-{
        return this.required == "true";
    }-*/;

    public final native boolean isReadOnly() /*-{
        return this.readonly == "true";
    }-*/;

    public final native String getBindingExpression() /*-{
        return this.bindingExpression;
    }-*/;
}
