package org.jbpm.formModeler.ng.common.client.rendering.js;

import com.google.gwt.core.client.JavaScriptObject;

public class FormContextStatus extends JavaScriptObject {
    protected FormContextStatus() {
    }

    public final native void setFieldValue(String fieldId, String value) /*-{
        this.values[fieldId] = value;
    }-*/;

    public final native String getFieldValue(String fieldId) /*-{
        return this.values[fieldId];
    }-*/;

    public final native JavaScriptObject getValues() /*-{
        return this.values;
    }-*/;
}
