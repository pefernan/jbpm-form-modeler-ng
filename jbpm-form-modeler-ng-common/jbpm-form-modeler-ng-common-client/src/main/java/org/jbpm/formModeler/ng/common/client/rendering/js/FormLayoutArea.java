package org.jbpm.formModeler.ng.common.client.rendering.js;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

public class FormLayoutArea extends JavaScriptObject {
    protected FormLayoutArea() {
    }

    public final native String getLabel() /*-{
        return this.label;
    }-*/;

    public final native JsArrayString getElements() /*-{
        return this.elements;
    }-*/;
}
