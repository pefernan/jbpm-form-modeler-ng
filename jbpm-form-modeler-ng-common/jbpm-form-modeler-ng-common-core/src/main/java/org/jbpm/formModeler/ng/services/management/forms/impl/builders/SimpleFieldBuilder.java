/**
 * Copyright (C) 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.formModeler.ng.services.management.forms.impl.builders;

import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.services.context.impl.marshalling.fieldMarshallers.*;
import org.jbpm.formModeler.ng.services.management.forms.FieldBuilder;

import java.util.ArrayList;
import java.util.List;

public class SimpleFieldBuilder implements FieldBuilder {

    @Override
    public List<Field> buildList() {

        List<Field> result = new ArrayList<Field>();

        Field field = new Field("InputText", "java.lang.String", new StringMarshaller(), "textbox.png");
        field.setSize(25);
        field.setMaxLength(4000);
        result.add(field);

        field = new Field("InputTextArea", "java.lang.String", new StringMarshaller(), "scroll_zone.png");
        result.add(field);

        field = new Field("InputTextCharacter", "java.lang.Character", new CharacterMarshaller(), "textbox.png");
        result.add(field);

        field = new Field("InputTextPrimitiveCharacter", "char", new CharacterMarshaller(), "textbox.png");
        result.add(field);

        field = new Field("InputTextFloat", "java.lang.Float", new NumberMarshaller("java.lang.Float", "###.##"), "box_number.png");
        result.add(field);

        field = new Field("InputTextPrimitiveFloat", "float", new NumberMarshaller("float", "###.##"), "box_number.png");
        result.add(field);

        field = new Field("InputTextDouble", "java.lang.Double", new NumberMarshaller("java.lang.Double", "###.##"), "box_number.png");
        result.add(field);

        field = new Field("InputTextPrimitiveDouble", "double", new NumberMarshaller("double", "###.##"), "box_number.png");
        result.add(field);

        field = new Field("InputTextBigDecimal", "java.math.BigDecimal", new NumberMarshaller("java.math.BigDecimal", "###.##"), "box_number.png");
        result.add(field);

        field = new Field("InputTextBigInteger", "java.math.BigInteger", new NumberMarshaller("java.math.BigInteger", ""), "box_number.png");
        result.add(field);

        field = new Field("InputTextByte", "java.lang.Byte", new NumberMarshaller("java.lang.Byte", ""), "box_number.png");
        result.add(field);

        field = new Field("InputTextPrimitiveByte", "byte", new NumberMarshaller("byte", ""), "box_number.png");
        result.add(field);

        field = new Field("InputTextShort", "java.lang.Short", new NumberMarshaller("java.lang.Short", ""), "box_number.png");
        result.add(field);

        field = new Field("InputTextPrimitiveShort", "short", new NumberMarshaller("java.lang.Short", ""), "box_number.png");
        result.add(field);

        field = new Field("InputTextInteger", "java.lang.Integer", new NumberMarshaller("java.lang.Integer", ""), "box_number.png");
        result.add(field);

        field = new Field("InputTextPrimitiveInteger", "int", new NumberMarshaller("int", ""), "box_number.png");
        result.add(field);

        field = new Field("InputTextLong", "java.lang.Long", new NumberMarshaller("java.lang.Long", ""), "box_number.png");
        result.add(field);

        field = new Field("InputTextPrimitiveLong", "long", new NumberMarshaller("java.lang.long", ""), "box_number.png");
        result.add(field);

        field = new Field("CheckBox", "java.lang.Boolean", new BooleanMarshaller(), "checkbox.png");
        result.add(field);

        field = new Field("CheckBoxPrimitiveBoolean", "boolean", new BooleanMarshaller(), "checkbox.png");
        result.add(field);

        field = new Field("HTMLEditor", "java.lang.String", new StringMarshaller(), "rich_text_box.png");
        result.add(field);

        field = new Field("InputDate", "java.util.Date", new DateMarshaller(), "date_selector.png");
        result.add(field);

        field = new Field("InputShortDate", "java.util.Date", new DateMarshaller(), "date_selector.png");
        result.add(field);

        field = new Field("I18nText", "java.util.HashMap", new DefaultMarshaller(), "textbox.png");
        result.add(field);

        field = new Field("I18nHTMLText", "java.util.HashMap", new DefaultMarshaller(), "rich_text_box.png");
        result.add(field);

        return result;
    }
}
