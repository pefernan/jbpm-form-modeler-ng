package org.jbpm.formModeler.ng.common.client.rendering.js;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class FormDefinition extends JavaScriptObject {

    protected FormDefinition() {
    }

    public final native String getCtxUID() /*-{
        return this.ctxUID;
    }-*/;

    public final native Long getId() /*-{
        return this.id;
    }-*/;

    public final native String getDisplayMode() /*-{
        return this.displayMode;
    }-*/;

    public final native String getLabelMode() /*-{
        return this.labelMode;
    }-*/;

    public final native JsArray<FieldDefinition> getFieldDefinitions() /*-{
        return this.fields;
    }-*/;
}