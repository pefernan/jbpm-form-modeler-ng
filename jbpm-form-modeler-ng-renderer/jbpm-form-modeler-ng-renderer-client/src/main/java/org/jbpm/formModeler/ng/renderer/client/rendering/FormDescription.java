package org.jbpm.formModeler.ng.renderer.client.rendering;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class FormDescription extends JavaScriptObject {

    protected FormDescription() {
    }

    public final native Long getId() /*-{
        return this.id;
    }-*/;

    public final native String getDisplayMode() /*-{
        return this.displayMode;
    }-*/;

    public final native String getLabelMode() /*-{
        return this.labelMode;
    }-*/;

    public final native JsArray<FieldDescription> getFields() /*-{
        return this.fields;
    }-*/;
}
