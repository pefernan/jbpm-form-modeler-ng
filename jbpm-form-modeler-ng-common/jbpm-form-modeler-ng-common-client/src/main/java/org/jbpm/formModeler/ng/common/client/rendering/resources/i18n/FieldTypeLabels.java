package org.jbpm.formModeler.ng.common.client.rendering.resources.i18n;


import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface FieldTypeLabels extends Messages {

    public static final FieldTypeLabels INSTANCE = GWT.create(FieldTypeLabels.class);

    public String defaultLabel();

    public String timestamp();

    public String date();

    public String checkbox();

    public String radiobutton();

    public String number();

    public String decimal();

    public String dropdown();

    public String textbox();

    public String textarea();
}
