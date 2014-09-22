package org.jbpm.formModeler.ng.common.client.rendering.js;

import com.google.gwt.core.client.JavaScriptObject;

public class FormContext extends JavaScriptObject {

    protected FormContext() {
    }

    public final native String getCtxUID() /*-{
        return this.ctxUID;
    }-*/;

    public final native FormDefinition getFormDefinition() /*-{
        return this.form;
    }-*/;

    public final native FormContextStatus getContextStatus() /*-{
        return this.status;
    }-*/;
}
