package org.jbpm.formModeler.ng.common.client.rendering;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class FormDescription extends JavaScriptObject {

    protected FormDescription() {
    }

    public final native Long getFormId() /*-{
        return this.formId;
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
