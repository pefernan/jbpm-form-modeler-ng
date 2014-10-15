package org.jbpm.formModeler.ng.common.client.renderer.checkers.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface CheckerConstants extends Messages {
    public static final CheckerConstants INSTANCE = GWT.create(CheckerConstants.class);

    public String requiredError(String label);
    public String fieldFormat(String label);
}
