/**
 * Copyright (C) 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package org.jbpm.formModeler.ng.editor.client.resources.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface Constants extends Messages {

    public static final Constants INSTANCE = GWT.create(Constants.class);

    public String form_modeler_form();
    public String form_modeler_new_form();
    public String form_modeler_save();
    public String form_modeler_title(String name);
    public String form_modeler_successfully_saved(String name);
    public String form_modeler_cannot_save(String name);
    public String form_modeler_cannot_load_form(String name);
    public String form_modeler_delete();
    public String form_modeler_confirm_delete();
    public String formResourceTypeDescription();

    public String form_modeler_layout();
    public String form_modeler_label_position();
    public String form_modeler_source();
    public String form_modeler_sources();
    public String form_modeler_unrelated_sources();
    public String form_modeler_fields();
    public String form_modeler_fields_palette();
}
