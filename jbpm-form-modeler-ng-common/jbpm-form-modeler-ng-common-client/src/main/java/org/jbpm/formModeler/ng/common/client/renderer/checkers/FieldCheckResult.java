package org.jbpm.formModeler.ng.common.client.renderer.checkers;

public class FieldCheckResult {
    private boolean wrong;
    private String message;

    public FieldCheckResult(boolean wrong, String message) {
        this.wrong = wrong;
        this.message = message;
    }

    public boolean isWrong() {
        return wrong;
    }

    public String getMessage() {
        return message;
    }
}
