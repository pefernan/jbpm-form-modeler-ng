package org.jbpm.formModeler.ng.model.impl.fields;

import org.jbpm.formModeler.ng.model.FieldValueMarshaller;
import org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers.CharacterMarshaller;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class InputTextCharacter extends InputText {
    @Inject
    private CharacterMarshaller marshaller;

    @PostConstruct
    public void initField() {
        setSize(1);
        setMaxLength(1);
    }

    @Override
    public String getCode() {
        return "InputTextCharacter";
    }

    @Override
    public String getIcon() {
        return "textbox.png";
    }

    @Override
    public String getFieldClass() {
        return Character.class.getName();
    }

    @Override
    public FieldValueMarshaller getMarshaller() {
        return marshaller;
    }
}
