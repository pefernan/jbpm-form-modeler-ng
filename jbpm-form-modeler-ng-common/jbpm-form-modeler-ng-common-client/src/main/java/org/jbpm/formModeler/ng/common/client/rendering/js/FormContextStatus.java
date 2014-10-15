package org.jbpm.formModeler.ng.common.client.rendering.js;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class FormContextStatus extends JavaScriptObject {
    protected FormContextStatus() {
    }

    public final native void setFieldValue(String fieldId, String value) /*-{
        this.values[fieldId] = value;
    }-*/;

    public final native String getFieldValue(String fieldId) /*-{
        return this.values[fieldId];
    }-*/;

    public final native JsArray<FieldOption> getFieldOptions(String fieldId) /*-{
        return this.options[fieldId];
    }-*/;

    public final native JavaScriptObject getValues() /*-{
        return this.values;
    }-*/;

    public final native void addWrongField(String fieldId) /*-{
        this.wrongFields.push(fieldId);
    }-*/;

    public final native void removeWrongField(String fieldId) /*-{
        delete this.wrongFields[fieldId];
    }-*/;

    public final native boolean isFieldWrong(String fieldId) /*-{
        return this.wrongFields.indexOf(fieldId) != -1;
    }-*/;

    public final native boolean hasWrongFields() /*-{
        return this.wrongFields.length > 0;
    }-*/;
}
