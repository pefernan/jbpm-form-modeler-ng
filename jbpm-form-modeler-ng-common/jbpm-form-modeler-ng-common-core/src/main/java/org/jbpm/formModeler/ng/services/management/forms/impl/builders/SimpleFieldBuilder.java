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
import org.jbpm.formModeler.ng.services.management.forms.FieldBuilder;

import java.util.ArrayList;
import java.util.List;

public class SimpleFieldBuilder implements FieldBuilder {

    @Override
    public List<Field> buildList() {

        List<Field> result = new ArrayList<Field>();

        Field field = new Field("InputText", "java.lang.String", "textbox.png");
        result.add(field);

        field = new Field("InputTextArea", "java.lang.String", "scroll_zone.png");
        result.add(field);

        field = new Field("InputTextCharacter", "java.lang.Character", "textbox.png");
        result.add(field);

        field = new Field("InputTextPrimitiveCharacter", "char", "textbox.png");
        result.add(field);

        field = new Field("InputTextFloat", "java.lang.Float", "box_number.png");
        result.add(field);

        field = new Field("InputTextPrimitiveFloat", "float", "box_number.png");
        result.add(field);

        field = new Field("InputTextDouble", "java.lang.Double", "box_number.png");
        result.add(field);

        field = new Field("InputTextPrimitiveDouble", "double", "box_number.png");
        result.add(field);

        field = new Field("InputTextBigDecimal", "java.math.BigDecimal", "box_number.png");
        result.add(field);

        field = new Field("InputTextBigInteger", "java.math.BigInteger", "box_number.png");
        result.add(field);

        field = new Field("InputTextByte", "java.lang.Byte", "box_number.png");
        result.add(field);

        field = new Field("InputTextPrimitiveByte", "byte", "box_number.png");
        result.add(field);

        field = new Field("InputTextShort", "java.lang.Short", "box_number.png");
        result.add(field);

        field = new Field("InputTextPrimitiveShort", "short", "box_number.png");
        result.add(field);

        field = new Field("InputTextInteger", "java.lang.Integer", "box_number.png");
        result.add(field);

        field = new Field("InputTextPrimitiveInteger", "int", "box_number.png");
        result.add(field);

        field = new Field("InputTextLong", "java.lang.Long", "box_number.png");
        result.add(field);

        field = new Field("InputTextPrimitiveLong", "long", "box_number.png");
        result.add(field);

        field = new Field("CheckBox", "java.lang.Boolean", "checkbox.png");
        result.add(field);

        field = new Field("CheckBoxPrimitiveBoolean", "boolean", "checkbox.png");
        result.add(field);

        field = new Field("HTMLEditor", "java.lang.String", "rich_text_box.png");
        result.add(field);

        field = new Field("InputDate", "java.util.Date", "date_selector.png");
        result.add(field);

        field = new Field("InputShortDate", "java.util.Date", "date_selector.png");
        result.add(field);

        field = new Field("I18nText", "java.util.HashMap", "textbox.png");
        result.add(field);

        field = new Field("I18nHTMLText", "java.util.HashMap", "rich_text_box.png");
        result.add(field);

        return result;
    }
}
