package org.jbpm.formModeler.ng.model.impl.fields;

import org.jbpm.formModeler.ng.model.FieldValueMarshaller;
import org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers.I18nMarshaller;

import javax.inject.Inject;
import java.util.HashMap;

public class I18nText extends InputText {
    @Inject
    I18nMarshaller marshaller;
    @Override
    public String getCode() {
        return "I18nText";
    }

    @Override
    public String getFieldClass() {
        return HashMap.class.getName();
    }

    @Override
    public FieldValueMarshaller getMarshaller() {
        return marshaller;
    }
}
