package org.jbpm.formModeler.ng.common.client.rendering.js;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class FormLayoutDefinition extends JavaScriptObject {
    protected FormLayoutDefinition() {
    }

    public final native Long getId() /*-{
        return this.id;
    }-*/;

    public final native JsArray<FormLayoutArea> getAreas() /*-{
        return this.areas;
    }-*/;


}
