package org.jbpm.formModeler.ng.editor.events.dataHolders;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jbpm.formModeler.ng.editor.events.FormModelerEvent;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderFieldTO;

@Portable
public class AddFieldFromHolder extends FormModelerEvent {
    private DataHolderFieldTO field;

    public DataHolderFieldTO getField() {
        return field;
    }

    public void setField(DataHolderFieldTO field) {
        this.field = field;
    }
}
