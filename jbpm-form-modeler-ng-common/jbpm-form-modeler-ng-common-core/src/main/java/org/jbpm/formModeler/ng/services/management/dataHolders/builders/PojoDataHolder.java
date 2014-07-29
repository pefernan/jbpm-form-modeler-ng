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
package org.jbpm.formModeler.ng.services.management.dataHolders.builders;

import org.apache.commons.lang.StringUtils;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.jbpm.formModeler.ng.model.DataFieldHolder;
import org.jbpm.formModeler.ng.model.DataHolder;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;
import org.jbpm.formModeler.ng.services.management.forms.FieldManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class PojoDataHolder extends DataHolder {
    protected transient FieldManager fieldManager;

    protected Set<DataFieldHolder> dataFieldHolders;

    PojoDataHolder(String uniqueId, String inputId, String outputId, String className, String renderColor) {
        this.uniqueId = uniqueId;
        this.inputId = inputId;
        this.outputId = outputId;
        this.className = className;
        this.renderColor = renderColor;
        this.fieldManager = BeanProvider.getContextualReference(FieldManager.class, true);
    }

    public Object createInstance(FormRenderContext context) throws Exception {
        return createInstance(Class.forName(className));
    }

    protected Object createInstance(Class pojoClass) throws Exception {
        for (Constructor constructor : pojoClass.getConstructors()) {
            if (constructor.getParameterTypes().length == 0) {
                return constructor.newInstance();
            }
        }
        return null;
    }

    @Override
    public void writeValue(Object destination, String propName, Object value) throws Exception {
        if (destination == null) return;
        Field field = destination.getClass().getDeclaredField(propName);

        Method setterMethod = destination.getClass().getMethod("set" + capitalize(propName), new Class[]{field.getType()});
        setterMethod.invoke(destination, new Object[]{value});
    }


    @Override
    public Object readValue(Object source, String propName) throws Exception {
        if (source == null) return null;

        Method getter = source.getClass().getMethod("get" + capitalize(propName));
        Object value = getter.invoke(source);

        return value;
    }

    private String capitalize(String string) {
        if (null == string) return "";
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }

    @Override
    public Set<DataFieldHolder> getFieldHolders() {
        try {
            if (dataFieldHolders == null || dataFieldHolders.size() == 0)
                dataFieldHolders = calculatePropertyNames();
            return dataFieldHolders;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public String getTypeCode() {
        return PojoDataHolderBuilder.HOLDER_TYPE_POJO_CLASSNAME;
    }

    @Override
    public DataFieldHolder getDataFieldHolderById(String fieldHolderId) {
        if (getFieldHolders() != null) {
            for (DataFieldHolder dataFieldHolder : getFieldHolders()) {
                if (dataFieldHolder.getId().equals(fieldHolderId))
                    return dataFieldHolder;
            }
        }
        return null;
    }

    protected Class getHolderClass() throws ClassNotFoundException {
        if (!StringUtils.isEmpty(className)) return Class.forName(className);
        return null;
    }

    private Set<DataFieldHolder> calculatePropertyNames() throws Exception {

        Class clazz = getHolderClass();

        if (clazz == null) {
            return null;
        }

        Set<DataFieldHolder> dataFieldHolders = new TreeSet<DataFieldHolder>();

        Map propertiesDescriptors = new HashMap();
        Method[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String methodName = method.getName();
            Class[] parameterTypes = method.getParameterTypes();
            Class returnType = method.getReturnType();
            if (isValidReturnType(returnType.getName())) {
                String propertyName = getPropertyName(methodName, returnType, parameterTypes);
                if (propertyName != null && Modifier.isPublic(method.getModifiers())) {
                    Map values = (Map) propertiesDescriptors.get(propertyName);
                    if (values == null)
                        propertiesDescriptors.put(propertyName, values = new HashMap());
                    Class paramClazz = parameterTypes.length == 0 ? returnType : parameterTypes[0]; // Relevant
                    // class
                    Boolean[] clazzValues = (Boolean[]) values.get(paramClazz);
                    if (clazzValues == null)
                        values.put(paramClazz, clazzValues = new Boolean[]{Boolean.FALSE, Boolean.FALSE});
                    clazzValues[parameterTypes.length] = Boolean.TRUE;// 0 ->
                    // getter,
                    // 1->
                    // setter
                }
            }
        }
        DataFieldHolder fieldHolder = null;
        for (Iterator it = propertiesDescriptors.keySet().iterator(); it.hasNext(); ) {
            String propertyName = (String) it.next();
            Map propertyValue = (Map) propertiesDescriptors.get(propertyName);
            for (Iterator itMethods = propertyValue.keySet().iterator(); itMethods.hasNext(); ) {
                Class methodClazz = (Class) itMethods.next();
                Boolean[] clazzValues = (Boolean[]) propertyValue.get(methodClazz);
                if (clazzValues[0].booleanValue() && clazzValues[1].booleanValue()) {
                    try {
                        String className = methodClazz.getName();
                        fieldHolder = new DataFieldHolder(this, propertyName, className);
                        dataFieldHolders.add(fieldHolder);
                    } catch (Exception e) {
                        //The
                    }
                    break;
                }
            }
        }
        return dataFieldHolders;
    }

    protected boolean isValidReturnType(String returnType) throws Exception {
        if (returnType == null) return false;
        if ("void".equals(returnType)) return true;
        if (fieldManager.getFieldByClass(returnType) != null) return true;
            //else if ("boolean".equals(returnType)) return true;
        else return false;

    }

    protected String getPropertyName(String methodName, Class returnType, Class[] parameterTypes) {
        String propName = null;
        if (((methodName.startsWith("get") && (parameterTypes.length == 0)) || (methodName.startsWith("set") && parameterTypes.length == 1)) && methodName.length() > 3) {
            propName = String.valueOf(Character.toLowerCase(methodName.charAt(3)));
            if (methodName.length() > 4)
                propName += methodName.substring(4);
        } else if (methodName.startsWith("is") && methodName.length() > 2 && (returnType.equals(Boolean.class) || returnType.equals(boolean.class)) && parameterTypes.length == 0) {
            propName = String.valueOf(Character.toLowerCase(methodName.charAt(2)));
            if (methodName.length() > 3)
                propName += methodName.substring(3);
        }
        return propName;
    }

    @Override
    public boolean isAssignableValue(Object value) {
        if (value == null) return true;
        return value.getClass().getName().equals(this.getClassName());
    }

    @Override
    public boolean canHaveChildren() {
        return true;
    }
}
