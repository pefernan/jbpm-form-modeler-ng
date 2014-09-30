package org.jbpm.formModeler.ng.common.client.rendering.js;

import com.google.gwt.core.client.JavaScriptObject;

public class FieldOption extends JavaScriptObject {
    protected FieldOption() {
    }

    public final native String getValue()/*-{
        return this.value;
    }-*/;

    public final native String getText()/*-{
        return this.text;
    }-*/;
}
